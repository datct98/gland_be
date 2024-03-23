package com.example.marketing.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaskScriptDTO {
    private Long taskId;

    private Long scriptId;

    private String nameTask;

    private Double hourDefault;
    //Từ viết tắt
    private String acronym;

    private List<RoleScriptDTO> scriptDtos;


}

