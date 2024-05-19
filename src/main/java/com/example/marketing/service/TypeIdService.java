package com.example.marketing.service;

import com.example.marketing.model.entities.stock.TypeId;
import com.example.marketing.repository.TypeIdRepository;
import com.example.marketing.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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

    public String delete(long id){
        TypeId typeId = typeIdRepository.findById(id).orElse(null);
        if(typeId == null){
            log.error("#TypeIdService delete - cant find id: "+id);
            return Constant.MESSAGE_ER.TYPE_ID_NULL;
        }
        typeIdRepository.delete(typeId);
        return Constant.STATUS_SUCCESS;
    }
}
