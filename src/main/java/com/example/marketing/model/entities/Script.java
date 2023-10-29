package com.example.marketing.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Script {
    @Id
    @Column(name= "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "department_id")
    private long departmentId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Transient
    private String department;
    @Transient
    private RoleTask roleTask;

    public Script(long id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public Script(long id, String name, long departmentId, String department) {
        this.id = id;
        this.name = name;
        this.departmentId = departmentId;
        this.department = department;
    }

    public Script(long id, String name, long departmentId, String department, RoleTask roleTask) {
        this.id = id;
        this.name = name;
        this.departmentId = departmentId;
        this.department = department;
        this.roleTask = roleTask;
    }
}
