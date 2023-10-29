package com.example.marketing.model.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="User")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @Column(name= "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String username;

    String fullName;

    String password;

    String phone;

    String email;

    String status;
    @Column(name = "script_id")
    long scriptId;

    @Column(name = "department_id")
    long departmentId;

    @Column(name = "store_id")
    long storeId;
    @Column(name = "is_admin")
    private boolean admin;

    @Column(name = "created_by")
    private String createdBy;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Transient
    private String departmentName;

    public User(long id, String username, String fullName, String phone, String email, String status, String departmentName) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.status = status;
        this.departmentName = departmentName;
    }
}
