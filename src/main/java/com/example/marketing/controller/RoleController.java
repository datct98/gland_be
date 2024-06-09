package com.example.marketing.controller;

import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.Role;
import com.example.marketing.model.entities.Work;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.service.RoleService;
import com.example.marketing.util.Constant;
import com.example.marketing.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

@RestController
@RequestMapping("/api/role")
@Slf4j
public class RoleController {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private RoleService roleService;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(description = "Used to insert or update. If id != null, it means update. Else insert")
    @PostMapping
    public ResponseEntity<?> configRole (@RequestHeader(name="Authorization") String token,
                                         @RequestBody Role body){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        String responseCode = roleService.configRole(body);
        if(!Constant.STATUS_SUCCESS.equals(responseCode)){
            return ResponseEntity.badRequest().body(Constant.MESSAGE_ERR.get(responseCode));
        }
        return ResponseEntity.ok("Thao tác thành công");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping
    public ResponseEntity<?> getRoleByUser(@RequestHeader(name="Authorization") String token,
                                       @RequestParam long userId){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        Role role = roleService.getRoleByUserId(userId);
        if(role != null){
           return ResponseEntity.ok(new DataResponse<>(role));
        }
        log.error("cant find role of userId: "+userId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không tìm thấy phân quyền");
    }
}
