package com.example.marketing.service;

import com.example.marketing.model.entities.ConfigSystem;
import com.example.marketing.model.entities.Work;
import com.example.marketing.repository.ConfigSystemRepository;
import com.example.marketing.repository.WorkRepository;
import com.example.marketing.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class WorkService {
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private ConfigSystemRepository configSystemRepository;


    public String modifyWork(Work body, String createdBy){
        Work work;
        try {
            if(StringUtils.isNotEmpty(body.getId())){
                work = workRepository.findById(body.getId()).orElse(null);
                if(work == null){
                    log.error("#createWork khong tim thay Work id ={}", body.getId());
                    return Constant.WORK_NOT_EXISTED;
                }
                work.setData(body.getData());
            } else {
                work = new Work();
                work.setId(UUID.randomUUID().toString());
                work.setCreatedBy(createdBy);
                work.setCreatedAt(new Date(System.currentTimeMillis()));
                work.setData(body.getData());
                work.setTaskId(body.getTaskId());

                ConfigSystem config = configSystemRepository.findById(1L).orElse(null);
                if(config == null){
                    log.error("#modifyWork ConfigSystem is not found");
                    return Constant.CONFIG_SYSTEM_NOT_EXISTED;
                }
                config.setIdWorkAuto(config.getIdWorkAuto()+1);
                configSystemRepository.save(config);
                work.setCode(body.getAcronym()+config.getIdWorkAuto());
                work.setDepartmentId(body.getDepartmentId());
                work.setDepartmentName(body.getDepartmentName());
                work.setScriptId(body.getScriptId());
                work.setScriptName(body.getScriptName());
            }
            workRepository.save(work);
        } catch (Exception e){
            log.error("#createWork - Error: {}", e.getMessage());
            return Constant.SYS_ERR;
        }

        return Constant.STATUS_SUCCESS;
    }


}
