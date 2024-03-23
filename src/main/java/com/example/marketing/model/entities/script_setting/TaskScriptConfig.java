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
@Table(name = "Task_Script_Config")
@Entity
public class TaskScript {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "task_id")
    private long taskId;
    @Column(name = "script_id")
    private long scriptId;
    @ColumnDefault("false")
    private boolean created;
    @ColumnDefault("false")
    private boolean assigned;

    @ColumnDefault(value = "true")
    private boolean status;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
}
