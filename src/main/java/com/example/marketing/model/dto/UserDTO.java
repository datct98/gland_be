package com.example.marketing.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String username;
    private boolean admin;
    private String email;
    private Long departmentId;
    private String departmentName;
    private String phone;
    private Date createdAt;
    private String role;

    public UserDTO(Long id, String username, boolean admin, Long departmentId) {
        this.id = id;
        this.username = username;
        this.admin = admin;
        this.departmentId = departmentId;
    }

    public UserDTO(Long id, String username, boolean admin, String email, Long departmentId, String departmentName, Date createdAt) {
        this.id = id;
        this.username = username;
        this.admin = admin;
        this.email = email;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.createdAt = createdAt;
    }

    public UserDTO(Long id, String username, boolean admin, Long departmentId, String role) {
        this.id = id;
        this.username = username;
        this.admin = admin;
        this.departmentId = departmentId;
        this.role = role;
    }
}
