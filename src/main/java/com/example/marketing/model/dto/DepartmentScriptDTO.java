package com.example.marketing.model.dto;

import com.example.marketing.model.entities.Script;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentScriptDTO {
    private long id;
    private String name;
    private String createdBy;
    private Date createdAt;
    private List<Script> scripts;

}
