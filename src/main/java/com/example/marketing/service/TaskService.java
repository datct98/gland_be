package com.example.marketing.service;

import com.example.marketing.model.dto.RoleScriptDTO;
import com.example.marketing.model.dto.TaskScriptDTO;
import com.example.marketing.model.entities.script_setting.Task;
import com.example.marketing.model.entities.script_setting.TaskScriptConfig;
import com.example.marketing.repository.TaskRepository;
import com.example.marketing.repository.TaskScriptConfigRepository;
import com.example.marketing.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskScriptConfigRepository taskScriptConfigRepository;

    public String modifyTask(TaskScriptDTO body){
        try{
            Task task;
            if(body.getTaskId() == null){
                task = new Task();
                task.setName(body.getNameTask());
                task.setAcronym(body.getAcronym());
                task.setHourDefault(body.getHourDefault());
                task.setScriptId(body.getScriptId());
            } else {
                task = taskRepository.findById(body.getTaskId()).orElse(null);
                if(task == null){
                    return Constant.TASK_NOT_EXISTED;
                }
                task.setHourDefault(body.getHourDefault()!=null?
                        body.getHourDefault(): task.getHourDefault());
                task.setName(StringUtils.isNotEmpty(body.getNameTask())? body.getNameTask(): task.getName());
                task.setAcronym(StringUtils.isNotEmpty(body.getAcronym())? body.getAcronym(): task.getAcronym());
            }
            // Set data for task
            taskRepository.save(task);

            // Set role for task and scripts
            List<TaskScriptConfig> taskScriptConfigsNew = new ArrayList<>();
            if(body.getScriptDtos()!= null && body.getScriptDtos().size()>0){
                List<Long> scriptIds = body.getScriptDtos().stream().map(RoleScriptDTO::getScriptId).collect(Collectors.toList());
                List<TaskScriptConfig> taskScriptConfigs = taskScriptConfigRepository.findAllByTaskIdAndScriptIdIn(body.getTaskId(), scriptIds);
                if(taskScriptConfigs!= null && taskScriptConfigs.size()>0){
                    // Update quyền truy cập
                    Map<Long, RoleScriptDTO> mapRole = body.getScriptDtos().stream().collect(Collectors.toMap(RoleScriptDTO::getScriptId, e-> e));
                    taskScriptConfigs.forEach(e->{
                        e.setCreated(mapRole.get(e.getScriptId()).getCreated());
                        e.setAssigned(mapRole.get(e.getScriptId()).getAssigned());
                    });
                    taskScriptConfigRepository.saveAll(taskScriptConfigs);
                    saveOtherRoleTask(taskScriptConfigs, body);

                } else {
                    // Tạo mới
                    for (RoleScriptDTO roleScript: body.getScriptDtos()){
                        TaskScriptConfig config = new TaskScriptConfig();
                        config.setTaskId(task.getId());
                        config.setScriptId(roleScript.getScriptId());
                        config.setAssigned(roleScript.getAssigned());
                        config.setCreated(roleScript.getCreated());
                        taskScriptConfigsNew.add(config);
                    }
                    taskScriptConfigRepository.saveAll(taskScriptConfigsNew);
                }
            }

            return Constant.STATUS_SUCCESS;
        } catch (Exception e){
            log.error("#TaskService - modifyTask - {}", e.getMessage());
            return Constant.SYS_ERR;
        }
    }

    private void saveOtherRoleTask(List<TaskScriptConfig> taskScriptConfigs, TaskScriptDTO body){
        List<TaskScriptConfig> taskScriptConfigsNew = new ArrayList<>();
        //Check xem có sót trong quá trình tìm kiếm
        if(taskScriptConfigs.size()< body.getScriptDtos().size()){
            List<Long> idsConfig = taskScriptConfigs.stream().map(TaskScriptConfig::getScriptId).collect(Collectors.toList());
            for (RoleScriptDTO scriptDTO: body.getScriptDtos()){
                // Nếu scriptId trong body k nằm trong list Id tìm thấy -> lưu mới
                if(!idsConfig.contains(scriptDTO.getScriptId())){
                    TaskScriptConfig config = new TaskScriptConfig();
                    config.setScriptId(scriptDTO.getScriptId());
                    config.setTaskId(body.getTaskId());
                    config.setAssigned(scriptDTO.getAssigned());
                    config.setCreated(scriptDTO.getCreated());
                    taskScriptConfigsNew.add(config);
                }
            }
            taskScriptConfigRepository.saveAll(taskScriptConfigsNew);
        }
    }

}
