package com.example.marketing.model.entities.stock;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.persistence.Id;

@Document(collection = "Data_Stock")
@Data
public class DataStock {
    @Id
    @Field(name = "_id")
    private String id;
    @Field(name = "id_auto")
    private String idAuto;
    @Field(name = "pre_code")
    private String preCode;
    @Field(name = "id_custom")
    private String idCustom;
    @Field(name = "department_id")
    private Long departmentId;
    @Field(name = "department_name")
    private String departmentName;
    @Field(name = "script_id")
    private Long scriptId;
    @Field(name = "script_name")
    private String scriptName;
    @Field(name = "type_id")
    private String typeId;
    @Field(name = "type_name")
    private String typeName;
    private String data;
    @Field(name = "created_by")
    private String createdBy;
}
