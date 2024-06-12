package com.example.marketing.model.dto;

import com.example.marketing.model.entities.Role;
import com.example.marketing.model.entities.Script;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DepartmentDTO {
    private Long departmentId;
    private String departmentName;
    private String noteDepartment;
    private Date createdDateDepartment;
    private List<Script> scripts;
    private Role role;

}
