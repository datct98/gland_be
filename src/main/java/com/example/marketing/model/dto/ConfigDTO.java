package com.example.marketing.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ConfigDTO {
    private long id;
    private String name;
    private Date createdAt;
    private String createdBy;
    private boolean status;
    private int days;
    private double hour;

    public ConfigDTO(String name, Date createdAt, String createdBy, boolean status) {
        this.name = name;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.status = status;
    }

    public ConfigDTO(long id, String name,  Date createdAt,String createdBy, boolean status, int days, double hour) {
        this.id = id;
        this.name = name;
        this.createdAt = new Date();
        this.createdBy = createdBy;
        this.status = status;
        this.days = days;
        this.hour = hour;
    }

    public ConfigDTO(long id, String name, Date createdAt, String createdBy, boolean status) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.status = status;
    }
}
