package com.example.marketing.service;

import com.example.marketing.model.entities.ConfigSystem;
import com.example.marketing.model.entities.DataConnection;
import com.example.marketing.model.entities.Work;
import com.example.marketing.repository.ConfigSystemRepository;
import com.example.marketing.repository.DataConnectRepository;
import com.example.marketing.repository.DataStockRepository;
import com.example.marketing.repository.TaskInfoRepository;
import com.example.marketing.repository.WorkRepository;
import com.example.marketing.util.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkService {
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private ConfigSystemRepository configSystemRepository;
    @Autowired
    private DataConnectRepository dataConnectRepository;
    @Autowired
    private TaskInfoRepository taskInfoRepository;
    @Autowired
    private DataStockRepository dataStockRepository;


    public String modifyWork(Work body, String createdBy){
        //Work work;
        try {
            if(StringUtils.isNotEmpty(body.getId())){

                Work work = workRepository.findById(body.getId()).orElse(null);
                if(work == null ){
                    log.error("#createWork khong tim thay Work id ={}", body.getId());
                    return Constant.WORK_NOT_EXISTED;
                }
                work.setData(body.getData());
                workRepository.save(work);
            } else {
                Work work = new Work();
                work.setId(UUID.randomUUID().toString());
                work.setIdWork(work.getId());
                work.setCreatedBy(createdBy);
                work.setCreatedAt(new Date(System.currentTimeMillis()));
                work.setData(body.getData());

                work.setTaskId(body.getTaskId());
                work.setDepartmentId(body.getDepartmentId());
                work.setDepartmentName(body.getDepartmentName());
                work.setScriptId(body.getScriptId());
                work.setScriptName(body.getScriptName());

                ConfigSystem config = configSystemRepository.findById(1L).orElse(null);
                if(config == null){
                    log.error("#modifyWork ConfigSystem is not found");
                    return Constant.CONFIG_SYSTEM_NOT_EXISTED;
                }
                ObjectMapper objectMapper = new ObjectMapper();
                work.setIdCustom(body.getIdCustom());
                work.setIncome(body.getIncome());
                work.setSpending(body.getSpending());
                workRepository.save(work);
            }

        } catch (Exception e){
            log.error("#createWork - Error: {}", e.getMessage());
            return Constant.SYS_ERR;
        }

        return Constant.STATUS_SUCCESS;
    }

    public Page<Work> getWorksConnected(Pageable pageable, long taskId, long scriptId){
        // tìm tất cả các nhiệm vụ kêts nối đến kịch bản hiện tại
        List<DataConnection> dataConnections = dataConnectRepository.findAllByIdToAndConnected(scriptId, true);
        List<String> ids = dataConnections.stream().map(DataConnection::getIdFrom).collect(Collectors.toList());
        Page<Work> works = workRepository.findAllByTaskIdAndIdIn(taskId, ids, pageable);
        return works;
    }


}
