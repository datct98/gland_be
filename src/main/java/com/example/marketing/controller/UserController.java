package com.example.marketing.controller;

import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.User;
import com.example.marketing.model.entities.UserDetail;
import com.example.marketing.model.request.LoginRequest;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.model.response.LoginResponse;
import com.example.marketing.repository.UserRepository;
import com.example.marketing.service.AccountService;
import com.example.marketing.util.Constant;
import com.example.marketing.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RestController;
import java.util.Base64;

@RestController
@RequestMapping("/api/authen")
@Slf4j
public class UserController {
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/register")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createUser(@RequestBody User body,
                                     @RequestHeader(name="Authorization") String token) throws Exception {
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO != null){
            if("leader".equalsIgnoreCase(body.getRole()) && !userDTO.isAdmin()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DataResponse<>(403, "Not allow"));
            }
            String responseCode = accountService.createUser(body, userDTO.getUsername());
            if(responseCode.equals(Constant.STATUS_SUCCESS)){
                if(StringUtils.isNotEmpty(body.getMain())){
                    Page<UserDTO> userPage = userRepository.findAllByDepartmentIdOrderByCreatedAtDesc(null, PageRequest.of(0, 10));
                    return ResponseEntity.ok(new DataResponse<>(userPage.getContent(), userPage.getTotalPages()));
                }
                Page<UserDTO> userPage = userRepository.findAllByDepartmentIdOrderByCreatedAtDesc(body.getDepartmentId(), PageRequest.of(0, 10));
                return ResponseEntity.ok(new DataResponse<>(userPage.getContent(), userPage.getTotalPages()));
            }

            return ResponseEntity.badRequest().body(new DataResponse<>(Integer.parseInt(responseCode), Constant.MESSAGE_ERR.get(responseCode)));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/signing")
    public ResponseEntity<?> authenticateUser2(@RequestBody LoginRequest loginRequest) {

        // Xác thực từ username và password.
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            // Nếu không xảy ra exception tức là thông tin hợp lệ
            // Set thông tin authentication vào Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetail detail = (UserDetail) authentication.getPrincipal();

            // Trả về jwt cho người dùng.
            String jwt = jwtUtil.generateToken(detail);
            User user = detail.getUser();
            return ResponseEntity.ok(new LoginResponse(user.isAdmin(), jwt, user.getRole()));

        } catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.ok(new DataResponse<>(401,"Đăng nhập không thành công"));
        }
    }

}
