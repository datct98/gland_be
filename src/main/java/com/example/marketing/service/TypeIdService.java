package com.example.marketing.service;

import com.example.marketing.model.entities.stock.TypeId;
import com.example.marketing.repository.TypeIdRepository;
import com.example.marketing.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TypeIdService {
    @Autowired
    private TypeIdRepository typeIdRepository;

    public String modify(TypeId body){
        TypeId typeId;
        if(body.getId() == null){
            typeId = new TypeId();
        } else {
            typeId = typeIdRepository.findById(body.getId()).orElse(null);
            if(typeId == null){
                return "Không tìm thấy Type Id :"+body.getId();
            }
        }
        typeId.setName(body.getName());
        typeId.setNote(body.getNote());
        typeId.setDepartmentId(body.getDepartmentId());
        typeIdRepository.save(typeId);
        return Constant.STATUS_SUCCESS;

    }
}
