package com.example.marketing.model.entities.script_setting;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@Table(name = "Task_Info")
@Entity
public class TaskInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String field;
    @Column(name = "data_type")
    private String dataType;
    @Column(name = "require_input")
    private Boolean requireInput;
    @Column(name = "display_on_list")
    private Boolean displayOnList;
    @Column(name = "display_on_filter")
    private Boolean displayOnFilter;
    @Column(name = "allow_search")
    private Boolean allowSearch;
    @Column(name = "id_custom")
    private Boolean idCustom;

    @Column(name = "task_id")
    private Long taskId;
    @ColumnDefault("true")
    private Boolean status;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
}
