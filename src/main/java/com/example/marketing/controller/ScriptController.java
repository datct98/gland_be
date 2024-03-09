package com.example.marketing.controller;

import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.Script;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.service.AccountService;
import com.example.marketing.service.ScriptService;
import com.example.marketing.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scripts")
@Slf4j
public class ScriptController {
    @Autowired
    private ScriptService scriptService;
    @Autowired
    private JWTUtil jwtUtil;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(description = "Used to modifyScript. If id == null it means create. Else update ")
    @PostMapping
    public ResponseEntity<?> modifyScript(@RequestHeader(name="Authorization") String token,
                                          @RequestBody Script body){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        Page<Script> scripts = scriptService.modifyScript(body, userDTO.getUsername());
        if(scripts == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DataResponse<>(HttpStatus.BAD_REQUEST.value(), "Thao tác thất bại, vui lòng thử lại!"));
        }
        return ResponseEntity.ok(new DataResponse<>(scripts.getContent(), scripts.getTotalPages()));
    }
}
