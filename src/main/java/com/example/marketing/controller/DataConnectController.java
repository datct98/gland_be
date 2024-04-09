package com.example.marketing.controller;

import com.example.marketing.model.dto.DepartmentDTO;
import com.example.marketing.model.dto.DepartmentScriptDTO;
import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.Script;
import com.example.marketing.model.entities.Work;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.WorkRepository;
import com.example.marketing.service.DepartmentService;
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
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/data-connection")
@Slf4j
public class DataConnectController {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private WorkRepository workRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @Operation(description = "Collect data from id")
    @GetMapping
    public ResponseEntity<?> collectData(@RequestHeader(name="Authorization") String token,
                                         @RequestParam Integer pageSize,
                                         @RequestParam Integer pageNum){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        // Check is admin -> authorization
        if(!userDTO.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DataResponse<>(HttpStatus.FORBIDDEN.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));

        pageNum = pageNum == null ? 0 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        Page<Work> works = workRepository.findAll(PageRequest.of(pageNum, pageSize));
        return ResponseEntity.ok(works.getContent());
    }
}
