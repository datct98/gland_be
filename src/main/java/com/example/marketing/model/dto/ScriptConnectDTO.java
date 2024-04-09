package com.example.marketing.model.dto;

import lombok.Data;

@Data
public class ScriptConnectDTO {
    private long id;
    private String name;
    private Boolean connected;

    public ScriptConnectDTO(long id, String name, Boolean connected) {
        this.id = id;
        this.name = name;
        this.connected = connected;
    }
}
