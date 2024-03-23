package com.example.marketing.service;

import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.User;
import com.example.marketing.repository.UserRepository;
import com.example.marketing.repository.WalletRepository;
import com.example.marketing.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AccountService {
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private WalletRepository walletRepository;

    public String createUser(User body, String createdBy){
        User user = userRepository.findByUsername(body.getUsername());
        if(user != null){
            return Constant.ACCOUNT_EXISTED;
        }
        body.setPassword(passwordEncoder.encode(body.getPassword()));
        body.setCreatedBy(StringUtils.isNotEmpty(createdBy)?createdBy: body.getCreatedBy());
        body.setStatus(true);
        try {
            userRepository.save(body);
            return Constant.STATUS_SUCCESS;
        } catch (Exception e){
            log.error("#AccountService - createUser fail - {}", e.getMessage());
            return Constant.SYS_ERR;
        }
    }

    public Page<UserDTO> findByDepartmentId(Long departmentId, Integer pageNum){
        Page<UserDTO> userPage = userRepository.findAllByDepartmentIdOrderByCreatedAtDesc
                (departmentId, PageRequest.of(pageNum, Constant.PAGE_SIZE));
        return userPage;
    }

}
