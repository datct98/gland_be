package com.example.marketing.model.dto;

import com.example.marketing.model.entities.script_setting.TaskInfo;
import lombok.Data;

import java.util.List;

@Data
public class TaskScriptDTO {
    private Long taskId;

    private Long scriptId;

    private String nameTask;

    private Double hourDefault;
    private Boolean idAuto;
    private Boolean idCustom;
    //Từ viết tắt
    //private String acronym;
    private String preCode;

    private List<RoleScriptDTO> scriptDtos;

    private List<TaskInfo> infos;


}

