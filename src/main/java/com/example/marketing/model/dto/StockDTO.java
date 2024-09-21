package com.example.marketing.model.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class StockDTO {
    long typeId;
    String typeName;
    //idCustom or idAuto
    String idDataStock;
    @Field(name = "created_by")
    private String createdBy;
}
