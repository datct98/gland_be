package com.example.marketing.controller;
import com.example.marketing.model.entities.User;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.UserRepository;
import com.example.marketing.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-account")
public class AccountController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/all")
    public ResponseEntity<?> getAllAccounts(@RequestHeader(name="Authorization") String token,
                                            @RequestParam(required = false) long storeId,
                                            @RequestParam(required = false) Long departmentId,
                                            @RequestParam(required = false) int pageNum) throws Exception {
        token= token.replace("Bearer ","");
        if(jwtUtil.validateToken(token)){
            Page<User> userPage;
            if(departmentId== null)
                userPage = userRepository.findAllByStoreId(storeId, PageRequest.of(pageNum, 10));
            else userPage = userRepository.findAllByDepartmentId(departmentId, PageRequest.of(pageNum, 10));
            return ResponseEntity.ok(new DataResponse<>(userPage.getContent(), userPage.getTotalPages()));
        }
        throw new Exception("Un-authentication");
    }

}
