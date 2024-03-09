package com.example.marketing.service;

import com.example.marketing.model.dto.DepartmentScriptDTO;
import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.Department;
import com.example.marketing.repository.DepartmentRepository;
import com.example.marketing.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public void modifyDepartmentService(Department body, String username){
        // Tồn tại thì update
        if(body.getId()!=null){
            body.setCreatedBy(username);
            //body.setStatus(true);
            departmentRepository.save(body);
        } else {
            // Thêm mới
            Department department = new Department();
            department.setStatus(true);
            department.setCreatedBy(username);
            department.setName(body.getName());
            department.setNote(body.getNote());
            departmentRepository.save(department);
        }

    }

    public Page<DepartmentScriptDTO> getDepartments(UserDTO userDTO, int pageNum){
        return departmentRepository.findAllByCreatedBy(userDTO.getUsername(), userDTO.isAdmin(), PageRequest.of(pageNum, Constant.PAGE_SIZE));
    }
}
