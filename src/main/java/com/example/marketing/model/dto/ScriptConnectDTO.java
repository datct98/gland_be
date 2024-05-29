package com.example.marketing.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ScriptConnectDTO {
    private long id;
    private String name;
    private String departmentName;
    private Long departmentId;
    private Boolean connected;
    private Boolean assigned;
    private Boolean committed;
    private Date createdDt;

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

    public ScriptConnectDTO(long id, String name, String departmentName, Long departmentId, Date createdDt) {
        this.id = id;
        this.name = name;
        this.departmentName = departmentName;
        this.departmentId = departmentId;
        this.createdDt = createdDt;
    }
}
