package com.example.marketing.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class ConditionRequest {
    private String status;
    private int officeId;
    private int positionId;
    private Date createAt;
}
