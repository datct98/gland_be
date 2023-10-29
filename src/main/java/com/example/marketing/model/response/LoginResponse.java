package com.example.marketing.model.response;
import com.example.marketing.model.entities.Script;
import com.example.marketing.model.entities.Store;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LoginResponse {
    private boolean admin;
    private String accessToken;
    private List<Script> scripts;
    private List<Store> stores;

    public LoginResponse(String accessToken, List<Script> scripts, boolean admin, List<Store> stores) {
        this.accessToken = accessToken;
        this.scripts = scripts;
        this.admin = admin;
        this.stores = stores;
    }
}
