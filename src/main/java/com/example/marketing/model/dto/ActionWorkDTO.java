package com.example.marketing.model.dto;

import lombok.Data;

@Data
public class ActionWorkDTO {
    // accept | reject | assign
    private String action;
    private String reason;
    private Long scriptId;
    private String assignee;
}
