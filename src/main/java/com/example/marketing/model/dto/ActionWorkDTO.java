package com.example.marketing.model.dto;

import lombok.Data;

@Data
public class ActionWorkDTO {
    // accept | reject
    private String action;
    private String reason;
    private Long scriptId;
}
