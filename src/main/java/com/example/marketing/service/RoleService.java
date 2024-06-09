package com.example.marketing.service;

import com.example.marketing.model.entities.Role;
import com.example.marketing.repository.RoleRepository;
import com.example.marketing.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public String configRole(Role body){
        try {
            Role role = getRoleByUserId(body.getUserId());
            if(role == null){
                body.setId(UUID.randomUUID().toString());
                roleRepository.save(body);
            } else {
                role.setDepartmentAlloweds(body.getDepartmentAlloweds());
                roleRepository.save(role);
            }
            return Constant.STATUS_SUCCESS;
        } catch (Exception e){
            log.error("#RoleService - configRole Fail Err: {}", e.getMessage());
            return Constant.SYS_ERR;
        }
    }

    public Role getRoleByUserId(long userId){
        return roleRepository.findAllByUserId(userId);
    }
}
