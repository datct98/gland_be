package com.example.marketing.model.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document(collection = "Role")
@Data
public class Role {
    @Id
    private String id;
    private long userId;
    private String username;
    private List<DepartmentAllowed> departmentAlloweds;

}
@Data
class DepartmentAllowed{
    private String departmentName;
    private Long departmentId;
    private UserAllowed userAllowed;
    private WalletAllowed walletAllowed;
    private DataStockAllowed dataStockAllowed;
    private ScriptAllowed scriptAllowed;
}

@Data
class UserAllowed{
    private Boolean isViewed;
    private Boolean isCreated;
    private Boolean isConfiged;
}

@Data
class WalletAllowed{
    private Boolean isViewed;
    private Boolean isCreated;
    private Boolean isConfiged;
}

@Data
class DataStockAllowed{
    private Boolean isViewed;
    private Boolean isCreated;
    private Boolean isConfiged;
}

@Data
class ScriptAllowed{
    private Boolean isViewed;
    private Boolean isCreated;
    private Boolean isConfiged;
    private List<MissionsAllowed> missionsAllowed;
}

@Data
class MissionsAllowed{
    private Long missionId;
    private String missionName;
    private Boolean isViewed;
    private Boolean isCreated;
    private Boolean isConfiged;
}
