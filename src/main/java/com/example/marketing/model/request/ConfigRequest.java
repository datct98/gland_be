package com.example.marketing.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConfigRequest {
    private String name;
    private Long storeId;
    private Integer action;
    private Integer days;
    private Double hour;
}
