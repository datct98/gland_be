package com.example.marketing.service;

import com.example.marketing.model.entities.script_setting.TaskInfo;
import com.example.marketing.repository.TaskInfoRepository;
import com.example.marketing.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskInfoService {
    @Autowired
    private TaskInfoRepository taskInfoRepository;

    public String modifyTaskInfo(TaskInfo body){
        TaskInfo taskInfo;
        if(body.getId()== null){
            taskInfo = new TaskInfo();
        } else {
            taskInfo = taskInfoRepository.findById(body.getId()).orElse(null);
            if(taskInfo == null){
                return Constant.TASK_NOT_EXISTED;
            }
        }
        taskInfo.setTaskId(body.getTaskId());
        taskInfo.setField(body.getField());
        taskInfo.setDataType(body.getDataType());
        taskInfo.setAllowSearch(body.getAllowSearch());
        taskInfo.setRequireInput(body.getRequireInput());
        taskInfo.setDisplayOnFilter(body.getDisplayOnFilter());
        taskInfo.setDisplayOnList(body.getDisplayOnList());
        taskInfoRepository.save(taskInfo);
        return Constant.STATUS_SUCCESS;
    }
}
