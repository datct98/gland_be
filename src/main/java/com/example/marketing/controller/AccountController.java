package com.example.marketing.controller;

import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.User;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.service.AccountService;
import com.example.marketing.util.Constant;
import com.example.marketing.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@Slf4j
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private ModelMapper modelMapper;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @Operation(description = "Used to get list employees. If departmentId != null, it means getAll. Else get the department's employees")
    @GetMapping
    public ResponseEntity<?> getUsersByDepartment(@RequestHeader(name="Authorization") String token,
                                                  @RequestParam(required = false) Integer pageNum,
                                                  @RequestParam(required = false) Integer pageSize,
                                                  @RequestParam(required = false) Long departmentId){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }


        pageNum = (pageNum == null? 0 : pageNum);
        pageSize = (pageSize == null? 10 : pageSize);
        Page<UserDTO> users = accountService.findByDepartmentId(departmentId, pageNum, pageSize);
        //List<UserDTO> userDTOS = users.getContent().stream().map(e-> modelMapper.map(e, UserDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(new DataResponse<>(users.getContent(), users.getTotalPages()));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(description = "Used to edit employee.")
    @PostMapping
    public ResponseEntity<?> editAccount(@RequestHeader(name="Authorization") String token,
                                         @RequestBody User body){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        String responseCode = accountService.editUser(body);
        if(responseCode.equals(Constant.ACCOUNT_NOT_EXISTED)){
            return ResponseEntity.badRequest().body("Tài khoản không tồn tại");
        }
        return ResponseEntity.status(Integer.parseInt(responseCode)).body(Constant.MESSAGE_ERR.get(responseCode));
    }

}
