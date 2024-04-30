package com.example.marketing.model.dto;

import com.example.marketing.model.entities.stock.TypeIdInfo;
import lombok.Data;

import java.util.List;
@Data
public class TypeIdDTO {
    private long id;
    private String name;
    private String note;
    private String type; // idCustom | idAuto
    private String preCode; // từ viết tắt của idAuto
    private List<TypeIdInfo> infos;

}
