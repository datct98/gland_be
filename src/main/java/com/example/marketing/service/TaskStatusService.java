package com.example.marketing.service;

import com.example.marketing.model.entities.script_setting.TaskStatus;
import com.example.marketing.repository.TaskStatusRepository;
import com.example.marketing.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public String modifyTaskStatus(TaskStatus body){
        TaskStatus taskStatus;
        if(body.getId()== null){
            taskStatus = new TaskStatus();
            taskStatus.setName(body.getName());
            taskStatus.setTaskId(body.getTaskId());
            taskStatus.setColor(body.getColor());
        } else {
            taskStatus = taskStatusRepository.findById(body.getId()).orElse(null);
            if(taskStatus == null){
                return Constant.TASK_NOT_EXISTED;
            }
            taskStatus.setColor(StringUtils.isNotEmpty
                    (body.getColor())? body.getColor() : taskStatus.getColor());
            taskStatus.setName(StringUtils.isNotEmpty
                    (body.getName())? body.getName() : taskStatus.getName());
        }
        taskStatusRepository.save(taskStatus);
        return Constant.STATUS_SUCCESS;
    }
}
