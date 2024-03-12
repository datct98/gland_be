package com.example.marketing.model.entities.script_setting;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "hour_efault", columnDefinition = "DOUBLE PRECISION DEFAULT 24.0")
    private double hourDefault;
    //Từ viết tắt
    private String acronym;
}
