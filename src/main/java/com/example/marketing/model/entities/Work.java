package com.example.marketing.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Document(collection = "Work")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Work {
    @Id
    @Field(name = "_id")
    private String id;
    @Field(name = "task_id")
    private long taskId;
    private String acronym;
    private long departmentId;
    private String departmentName;
    private long scriptId;
    private String scriptName;
    private String code;
    private String data;
    @Field(name = "created_at")
    private Date createdAt;
    @Field(name = "created_by")
    private String createdBy;
}
