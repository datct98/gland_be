package com.example.marketing.model.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "History_Work")
@Data
public class HistoryWork {
    @Id
    private String id;

}
