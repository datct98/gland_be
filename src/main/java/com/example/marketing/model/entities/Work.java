package com.example.marketing.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Document(collection = "Work")
@Data
public class Work {
    @Id
    //@Field(name = "_id")
    private String id;
    @Field(name = "task_id")
    private long taskId;
    @Field(name = "id_work")
    private String idWork;
    private String acronym;
    private long departmentId;
    private String departmentName;
    private long scriptId;
    private String scriptName;
    @Field(name = "id_custom")
    private String idCustom;
    @Field(name = "id_auto")
    private String idAuto;
    private String income;
    private String spending;
    private String data;
    @Field(name = "created_at")
    private Date createdAt;
    @Field(name = "created_by")
    private String createdBy;
    private String assignee;

    private List<Stock>idStocks;
}
@Data
class Stock{
    long typeId;
    String typeName;
    //idCustom or idAuto
    String idDataStock;
}
