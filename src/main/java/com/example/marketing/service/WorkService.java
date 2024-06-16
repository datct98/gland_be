package com.example.marketing.service;

import com.example.marketing.model.dto.ActionWorkDTO;
import com.example.marketing.model.entities.ConfigSystem;
import com.example.marketing.model.entities.DataConnection;
import com.example.marketing.model.entities.Work;
import com.example.marketing.model.entities.script_setting.Task;
import com.example.marketing.model.entities.script_setting.TaskInfo;
import com.example.marketing.repository.ConfigSystemRepository;
import com.example.marketing.repository.DataConnectRepository;
import com.example.marketing.repository.DataStockRepository;
import com.example.marketing.repository.TaskInfoRepository;
import com.example.marketing.repository.TaskRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private TaskRepository taskRepository;
    @Autowired
    private TaskInfoRepository taskInfoRepository;
    @Autowired
    private DataConnectService dataConnectService;


    public String modifyWork(Work body, String createdBy){
        //Work work;
        try {
            if(StringUtils.isNotEmpty(body.getId())){

                Work work = workRepository.findById(body.getId()).orElse(null);
                if(work == null ){
                    log.error("#createWork khong tim thay Work id ={}", body.getId());
                    return Constant.WORK_NOT_EXISTED;
                }

                work.setIdStocks(body.getIdStocks());
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
                work.setIdStocks(body.getIdStocks());
                work.setStatus("");

                Task task = taskRepository.findById(work.getTaskId()).orElse(null);
                if(task == null){
                    log.error("#modifyWork task is not found id: "+work.getTaskId());
                    return Constant.TASK_NOT_EXISTED;
                }
                if("idAuto".equalsIgnoreCase(task.getTypeId())){
                    ConfigSystem config = configSystemRepository.findById(1L).orElse(null);
                    if(config == null){
                        log.error("#modifyWork ConfigSystem is not found");
                        return Constant.CONFIG_SYSTEM_NOT_EXISTED;
                    }
                    work.setIdAuto(task.getPreCode()+config.getIdWorkAuto());
                    config.setIdWorkAuto(config.getIdWorkAuto()+1);
                    configSystemRepository.save(config);
                    ObjectMapper objectMapper = new ObjectMapper();
                    //Vì client k nhập đk idAuto nên cần lưu lại trong data field
                    if(StringUtils.isNotEmpty(body.getData())){
                        log.info("#modifyWork edit data field of work");
                        Map<String, String> map = objectMapper.readValue(body.getData(), HashMap.class);
                        Set<String> keys = map.keySet();
                        List<Long> ids = keys.stream().map(Long::parseLong).collect(Collectors.toList());
                        List<TaskInfo> infos = taskInfoRepository.findAllByIdIn(ids);
                        for (TaskInfo info: infos){
                            if(info.getIdAuto()!= null && info.getIdAuto()){
                                map.put(info.getId()+"", work.getIdAuto());
                                break;
                            }
                        }
                        work.setData(objectMapper.writeValueAsString(map));
                    }
                } else {
                    Work workCheck = workRepository.findByIdCustomIsOrIdAutoIs(body.getIdCustom(), body.getIdCustom());
                    if(workCheck != null){
                        log.error("#modifyWork id input: "+ body.getIdCustom()+" existed");
                        return Constant.ID_CUSTOM_EXISTED;
                    }
                    work.setIdCustom(body.getIdCustom());
                }
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
        List<String> status = List.of("Rejected", "");
        // tìm tất cả các nhiệm vụ kêts nối đến kịch bản hiện tại
        List<DataConnection> dataConnections = dataConnectRepository.findAllByIdToAndConnected(scriptId, true);
        List<String> ids = dataConnections.stream().map(DataConnection::getIdFrom).collect(Collectors.toList());
        Page<Work> works = workRepository.findAllByTaskIdAndIdInAndStatusNotIn(taskId, ids, status, pageable);
        return works;
    }


    public String actionWork(String id, String username, ActionWorkDTO request){
        Work work =workRepository.findById(id).orElse(null);
        if(work == null){
           return Constant.WORK_NOT_EXISTED;
        }
        if("accept".equalsIgnoreCase(request.getAction())){
            work.setStatus("Accepted");
            work.setAssignee(username);
        } else if("reject".equalsIgnoreCase(request.getAction())){
            work.setStatus("Rejected");
            work.setRejectReason(request.getReason());
            dataConnectService.unConnected(work.getId(), request.getScriptId());
        } else if("assign".equalsIgnoreCase(request.getAction())){
            work.setStatus("Assigned");
            work.setAssignee(request.getAssignee());
            dataConnectService.createNewConnect(id, request.getScriptId());
            //dataConnectService.unConnected(work.getId(), request.getScriptId());
        }

        workRepository.save(work);
        return Constant.STATUS_SUCCESS;
    }
}
