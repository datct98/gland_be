package com.example.marketing.model.response;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LoginResponse {
    private boolean admin;
    private String accessToken;
    private String role;

    public LoginResponse(boolean admin, String accessToken) {
        this.admin = admin;
        this.accessToken = accessToken;
    }

    public LoginResponse(boolean admin, String accessToken, String role) {
        this.admin = admin;
        this.accessToken = accessToken;
        this.role = role;
    }
}
