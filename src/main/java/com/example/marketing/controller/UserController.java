package com.example.marketing.controller;

import com.example.marketing.model.dto.Token;
import com.example.marketing.model.entities.Script;
import com.example.marketing.model.entities.Store;
import com.example.marketing.model.entities.User;
import com.example.marketing.model.entities.UserDetail;
import com.example.marketing.model.request.LoginRequest;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.model.response.LoginResponse;
import com.example.marketing.repository.ScriptRepository;
import com.example.marketing.repository.StoreRepository;
import com.example.marketing.repository.UserRepository;
import com.example.marketing.service.AccountService;
import com.example.marketing.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api-authen")
@Slf4j
public class UserController {
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ScriptRepository scriptRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private UserRepository userRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/register")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createUser(@RequestBody User body,
                                     @RequestHeader(name="Authorization") String token) throws Exception {
        if(jwtUtil.validateToken(token.replace("Bearer ",""))){
            String username = jwtUtil.getUsernameFromJwt(token.replace("Bearer ",""));
            if(accountService.createUser(body, username).equals("00")){
                Page<User> userPage;
                if(body.getDepartmentId()==0)
                    userPage = userRepository.findAllByStoreId(body.getStoreId(), PageRequest.of(0, 10));
                else userPage = userRepository.findAllByDepartmentId(body.getDepartmentId(), PageRequest.of(0, 10));
                return ResponseEntity.ok(new DataResponse<>(userPage.getContent(), userPage.getTotalPages()));
            }

            return ResponseEntity.ok(new DataResponse<>(HttpStatus.CREATED.value(), accountService.createUser(body, username)));
        }
        throw new Exception("Un-authentication");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/signing")
    public ResponseEntity<?> authenticateUser2(@RequestBody LoginRequest loginRequest) {

        // Xác thực từ username và password.
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            new String(Base64.getDecoder().decode(loginRequest.getPassword()))

                    )
            );
            // Nếu không xảy ra exception tức là thông tin hợp lệ
            // Set thông tin authentication vào Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetail detail = (UserDetail) authentication.getPrincipal();

            // Trả về jwt cho người dùng.
            String jwt = jwtUtil.generateToken(detail);
            User user = detail.getUser();
            List<Script> scripts = scriptRepository.findAllByNameAndDepartment(user.getScriptId());
            List<Store> stores = storeRepository.findAllByCreatedBy(user.getUsername());
            return ResponseEntity.ok(new LoginResponse(jwt, scripts, user.isAdmin(), stores));

        } catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.ok(new DataResponse<>(401,"Đăng nhập không thành công"));
        }
    }

}
