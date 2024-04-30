package com.example.marketing.model.entities.stock;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "Type_Id_Info")
@Entity
public class TypeIdInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type_id")
    private Long typeId;
    private String field;
    @Column(name = "data_type")
    private String dataType;
}
