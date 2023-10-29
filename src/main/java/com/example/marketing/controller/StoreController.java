package com.example.marketing.controller;

import com.example.marketing.model.entities.Store;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.StoreRepository;
import com.example.marketing.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api-store")
public class StoreController {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<?> createStore(@RequestHeader(name="Authorization") String token,
                                         @RequestBody Store store) throws Exception {

        token= token.replace("Bearer ","");
        if(jwtUtil.validateToken(token)){
            store.setCreatedBy(jwtUtil.getUsernameFromJwt(token));
            storeRepository.save(store);
            return ResponseEntity.ok(new DataResponse<>
                    (storeRepository.findAllByCreatedBy(jwtUtil.getUsernameFromJwt(token))));
        }
        throw new Exception("Un-authentication");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getStores(@RequestHeader(name="Authorization") String token) throws Exception {

        token= token.replace("Bearer ","");
        if(jwtUtil.validateToken(token)){
            List<Store> stores = storeRepository.findAllByCreatedBy(jwtUtil.getUsernameFromJwt(token));
            return ResponseEntity.ok(new DataResponse<>(stores));
        }
        throw new Exception("Un-authentication");
    }
}
