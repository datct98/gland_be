package com.example.marketing.controller;

import com.example.marketing.model.dto.DepartmentScriptDTO;
import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.Department;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.DepartmentRepository;
import com.example.marketing.service.DepartmentService;
import com.example.marketing.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

@RestController
@RequestMapping("/api/department")
@Slf4j
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private JWTUtil jwtUtil;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(description = "Used to insert or update. If id != null, it means update. Else insert")
    @PostMapping
    public ResponseEntity<?> modifyDepartment(@RequestBody Department body,
                                              @RequestHeader(name="Authorization") String token) throws Exception{
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        departmentService.modifyDepartmentService(body, userDTO.getUsername());
        return ResponseEntity.ok(new DataResponse<>(HttpStatus.OK.value(), "Thực hiện thành công", departmentRepository.findAllByStatus(true)));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping
    public ResponseEntity<?> getDepartments(@RequestHeader(name="Authorization") String token,
                                            @RequestParam(required = false) Integer pageNum) throws Exception{
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        pageNum = (pageNum == null? 0: pageNum);
        Page<DepartmentScriptDTO> departments = departmentService.getDepartments(userDTO, pageNum);
        return ResponseEntity.ok(new DataResponse<>(departments.getContent(), departments.getTotalPages()));
    }


}
