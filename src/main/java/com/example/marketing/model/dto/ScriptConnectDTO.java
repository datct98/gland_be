package com.example.marketing.model.dto;

import lombok.Data;

@Data
public class ScriptConnectDTO {
    private long id;
    private String name;
    private Boolean connected;
    private Boolean assigned;
    private Boolean committed;

    public ScriptConnectDTO(long id, String name, Boolean connected) {
        this.id = id;
        this.name = name;
        this.connected = connected;
    }

    public ScriptConnectDTO(long id, String name, Boolean assigned, Boolean committed) {
        this.id = id;
        this.name = name;
        this.assigned = assigned;
        this.committed = committed;
    }

    public ScriptConnectDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
