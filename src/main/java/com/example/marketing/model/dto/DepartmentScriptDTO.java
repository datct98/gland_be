package com.example.marketing.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentScriptDTO {
    private Long departmentId;
    private String departmentName;
    private Long scriptId;
    private String scriptName;
    private String noteDepartment;
    private String noteScript;
    private Date createdDateDepartment;
    private Date createdDateScript;


}
