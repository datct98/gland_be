package com.example.marketing.model.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "Data_Connection")
@Entity
public class DataConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // workId
    @Column(name = "id_from")
    private String idFrom;
    // scriptId
    @Column(name = "id_to")
    private long idTo;
    private Boolean connected;
}
