package com.example.marketing.model.dto;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
@Data
public class TaskStatusDTO {
    private long taskId;

    private long scriptId;

    private boolean created;

    private boolean assigned;
}
