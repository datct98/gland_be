package com.example.marketing.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private long id;
    private String username;
    private boolean admin;
    private String email;
    private Long departmentId;
    private String departmentName;
    private String phone;
    private Date createdAt;

    public UserDTO(Long id, String username, boolean admin) {
        this.id = id;
        this.username = username;
        this.admin = admin;
    }

    public UserDTO(Long id, String username, boolean admin, String email, String departmentName, Date createdAt) {
        this.id = id;
        this.username = username;
        this.admin = admin;
        this.email = email;
        this.departmentName = departmentName;
        this.createdAt = createdAt;
    }
}
