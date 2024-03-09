package com.example.marketing.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepartmentScriptDTO {
    private Long departmentId;
    private String departmentName;
    private Long scriptId;
    private String scriptName;

}
