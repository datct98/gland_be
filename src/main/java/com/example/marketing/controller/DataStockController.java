package com.example.marketing.controller;

import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.DataStock;
import com.example.marketing.model.entities.Work;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.DataStockRepository;
import com.example.marketing.repository.WorkRepository;
import com.example.marketing.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/api/data-stock")
@Slf4j
public class DataStockController {
    @Autowired
    private DataStockRepository dataStockRepository;
    @Autowired
    private JWTUtil jwtUtil;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping
    public ResponseEntity<?> getDataStock (@RequestHeader(name="Authorization") String token,
                                       @RequestParam(required = false) String preCode,
                                       @RequestParam(required = false)  Integer pageNum,
                                       @RequestParam(required = false)  Integer pageSize){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }

        pageNum = pageNum == null ? 0 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        Page<DataStock> page;
        if(StringUtils.isNotEmpty(preCode))
            page = dataStockRepository.findAllByPreCode(preCode,PageRequest.of(pageNum, pageSize));
        else page = dataStockRepository.findAll(PageRequest.of(pageNum, pageSize));
        return ResponseEntity.ok(page.getContent());
    }
}
