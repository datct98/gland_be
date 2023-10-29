package com.example.marketing.model.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "role_task")
public class RoleTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "script_id")
    private long scriptId;
    @Column(name = "type_task_id")
    private long typeTaskId;
    @Column(name = "my_job")
    private Boolean myJob;
    @Column(name = "assigned_job")
    private Boolean assignedJob;

}
