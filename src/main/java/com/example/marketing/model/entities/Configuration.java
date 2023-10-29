package com.example.marketing.model.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@Entity
@Table
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    private int type; //1-Tragj thái, 2-Thông tin

    @Column(name = "created_by")
    private String createdBy; //username

    @Column(name = "is_require")
    private boolean isRequire;
    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "data_type")
    private String dataType; // kiểu dữ liệu

    @Column(name = "type_task_id")
    private long typeTaskId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
}
