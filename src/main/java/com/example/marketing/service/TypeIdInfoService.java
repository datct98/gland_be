package com.example.marketing.service;

import com.example.marketing.model.dto.TypeIdDTO;
import com.example.marketing.model.entities.stock.TypeId;
import com.example.marketing.model.entities.stock.TypeIdInfo;
import com.example.marketing.repository.TypeIdInfoRepository;
import com.example.marketing.repository.TypeIdRepository;
import com.example.marketing.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeIdInfoService {
    @Autowired
    private TypeIdInfoRepository typeIdInfoRepository;
    @Autowired
    private TypeIdRepository typeIdRepository;

    public String modify(TypeIdDTO body){
        TypeId typeId = typeIdRepository.findById(body.getId()).orElse(null);
        if(typeId == null){
            return "Không tìm thấy Type id: "+body.getId();
        }
        typeId.setName(body.getName());
        typeId.setNote(body.getNote());
        typeId.setType(body.getType());
        // Chỉ Check khi precode truyền vào != preCode hiện tại
        if(StringUtils.isNotEmpty(body.getPreCode()) && !body.getPreCode().equalsIgnoreCase(typeId.getPreCode())){
            List<TypeId> typeIds = typeIdRepository.findAllByPreCode(body.getPreCode());
            if(typeIds.size()>0){
                return "Từ viết tắt ("+body.getPreCode()+") đã tồn tại";
            }
        }
        typeId.setPreCode(body.getPreCode());
        typeIdRepository.save(typeId);

        List<TypeIdInfo> infos = body.getInfos();
        List<TypeIdInfo> infoNews = new ArrayList<>();
        for(TypeIdInfo info: infos){
            TypeIdInfo infoNew;
            if(info.getId() == null || info.getId() == 0){
                infoNew = new TypeIdInfo();
            } else {
                infoNew = typeIdInfoRepository.findById(info.getId()).orElse(null);
                if(infoNew == null){
                    return "Không tìm thấy id info: "+info.getId();
                }
            }
            infoNew.setDataType(info.getDataType());
            infoNew.setField(info.getField());
            infoNew.setTypeId(info.getTypeId());
            infoNews.add(infoNew);
        }
        typeIdInfoRepository.saveAll(infoNews);
        return Constant.STATUS_SUCCESS;
    }

    public TypeIdDTO getDetail(long id){
        TypeId typeId = typeIdRepository.findById(id).orElse(null);
        if(typeId == null){
            return null;
        }
        List<TypeIdInfo> infos = typeIdInfoRepository.findAllByTypeId(id);
        TypeIdDTO dto = new TypeIdDTO();
        dto.setId(typeId.getId());
        dto.setName(typeId.getName());
        dto.setNote(typeId.getNote());
        dto.setType(typeId.getType());
        dto.setPreCode(typeId.getPreCode());
        dto.setInfos(infos);
        return dto;
    }

    public String deleteTypeIdInfo(long id){
        TypeIdInfo info = typeIdInfoRepository.findById(id).orElse(null);
        if(info == null){
            return "Không tìm thấy Type id: "+id;
        }
        typeIdInfoRepository.delete(info);
        return Constant.STATUS_SUCCESS;
    }
}
