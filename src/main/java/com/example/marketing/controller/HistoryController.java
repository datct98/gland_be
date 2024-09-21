package com.example.marketing.controller;

import com.example.marketing.model.dto.DepartmentDTO;
import com.example.marketing.model.dto.DepartmentScriptDTO;
import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.HistoryWork;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.service.HistoryWorkService;
import com.example.marketing.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/histories")
@Slf4j
public class HistoryController {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private HistoryWorkService historyWorkService;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping
    public ResponseEntity<?> getHistories(@RequestHeader(name="Authorization") String token,
                                            @RequestParam String workId,
                                            @RequestParam(required = false) Integer pageNum,
                                          @RequestParam(required = false) Integer pageSize) throws Exception{
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        pageNum = pageNum == null?  0 : pageNum;
        pageSize = pageSize == null ? 0 : pageSize;
        Page<HistoryWork> page = historyWorkService.getHistories(workId, pageSize, pageNum);
        return ResponseEntity.ok(new DataResponse<>(page.getContent(), page.getTotalPages()));

    }

}
