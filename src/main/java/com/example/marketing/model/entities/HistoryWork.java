package com.example.marketing.model.entities;

import com.example.marketing.model.dto.StockDTO;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Document(collection = "History_Work")
@Data
public class HistoryWork {
    @Id
    private String id;
    @Field(name = "id_work")
    private String idWork;
    private List<StockDTO> stocks;
    @Field(name = "created_by")
    private String createdBy;
    @Field(name = "created_at")
    private Date createdAt;


}
