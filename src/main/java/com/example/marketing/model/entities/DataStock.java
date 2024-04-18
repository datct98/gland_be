package com.example.marketing.model.entities;

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
    private long departmentId;
    @Field(name = "department_name")
    private String departmentName;
    @Field(name = "script_id")
    private long scriptId;
    @Field(name = "script_name")
    private String scriptName;
}
