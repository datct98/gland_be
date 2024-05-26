package com.example.marketing.service;

import com.example.marketing.model.entities.script_setting.TaskScriptConfig;
import com.example.marketing.repository.TaskScriptConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TaskScriptConfigService {
    @Autowired
    private TaskScriptConfigRepository taskScriptConfigRepository;

    public void deleteConfig(List<Long> idTasks, List<Long> idScripts){
        try{
            List<TaskScriptConfig> configs =
                    taskScriptConfigRepository.findAllByTaskIdInOrScriptIdIn(idTasks, idScripts);
            log.info("#TaskScriptConfigService - deleteConfig - configs size: {}", configs.size());
            if(configs.size()> 0){
                taskScriptConfigRepository.deleteAll(configs);
            }
        } catch (Exception e){
            log.info("#TaskScriptConfigService - deleteConfig - error: {}", e.getCause().toString());
        }

    }
}
