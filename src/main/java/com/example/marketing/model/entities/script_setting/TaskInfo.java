package com.example.marketing.model.entities.script_setting;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private boolean requireInput;
    @Column(name = "display_on_list")
    private boolean displayOnList;
    @Column(name = "display_on_filter")
    private boolean displayOnFilter;
    @Column(name = "allow_search")
    private boolean allowSearch;
}
