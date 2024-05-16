package com.example.marketing.model.entities.script_setting;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
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
@Table
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "hour_default", columnDefinition = "DOUBLE PRECISION DEFAULT 24.0")
    private Double hourDefault;
    //Từ viết tắt
    //private String acronym;
    @Column(name = "pre_code")
    private String preCode;
    // idAuto | idCustom
    @Column(name = "type_id")
    private String typeId;
    @Column(name = "script_id")
    private long scriptId;

    @ColumnDefault("true")
    private boolean status;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
}
