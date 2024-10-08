package com.example.marketing.service;

import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.User;
import com.example.marketing.repository.UserRepository;
import com.example.marketing.repository.WalletRepository;
import com.example.marketing.util.Constant;
import com.example.marketing.util.RoleName;
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
        if(StringUtils.isEmpty(body.getRole())){
            body.setRole("staff");
        }
        try {
            userRepository.save(body);
            return Constant.STATUS_SUCCESS;
        } catch (Exception e){
            log.error("#AccountService - createUser fail - {}", e.getMessage());
            return Constant.SYS_ERR;
        }
    }

    public String editUser(User body){
        User user = userRepository.findByUsername(body.getUsername());
        if(user == null){
            return Constant.ACCOUNT_NOT_EXISTED;
        }
        if(StringUtils.isNotEmpty(body.getPassword())){
            user.setPassword(passwordEncoder.encode(body.getPassword()));
        }
        user.setDepartmentId(body.getDepartmentId());
        user.setEmail(body.getEmail());
        if(StringUtils.isEmpty(body.getRole())){
            user.setRole("staff");
        } else {
            user.setRole(body.getRole());
        }
        try {
            userRepository.save(user);
            return Constant.STATUS_SUCCESS;
        } catch (Exception e){
            log.error("#AccountService - createUser fail - {}", e.getMessage());
            return Constant.SYS_ERR;
        }
    }

    public Page<UserDTO> findByDepartmentId(Long departmentId, Integer pageNum, Integer pageSize){
        Page<UserDTO> userPage = userRepository.findAllByDepartmentIdOrderByCreatedAtDesc
                (departmentId, PageRequest.of(pageNum, pageSize));
        return userPage;
    }

    public String deleteUser(long id, UserDTO dto){
        if(!dto.isAdmin() && !RoleName.LEADER.name().equalsIgnoreCase(dto.getRole())){
            return Constant.MESSAGE_ER.PERMISSION_DENIED;
        }
        User user = userRepository.findByIdAndAdminFalse(id);
        if(user == null){
            return Constant.ACCOUNT_NOT_EXISTED;
        }
        userRepository.delete(user);
        return Constant.STATUS_SUCCESS;
    }

}
