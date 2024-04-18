package com.example.marketing.service;

import com.example.marketing.model.entities.script_setting.TaskInfo;
import com.example.marketing.repository.TaskInfoRepository;
import com.example.marketing.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        taskInfo.setStatus(true);
        // Check nếu là ID nhập tay -> unique for true
        /*if(body.getIdCustom()){
            List<TaskInfo> taskInfos = taskInfoRepository.findAllByTaskId(body.getTaskId());
            for (TaskInfo info: taskInfos){
                if(info.getIdCustom()){
                    return Constant.ID_CUSTOM_EXISTED;
                }
            }
        }*/
        taskInfo.setIdCustom(body.getIdCustom());
        taskInfo.setIdAuto(body.getIdAuto());
        taskInfo.setPreCode(body.getPreCode());
        taskInfo.setIncome(body.getIncome());
        taskInfo.setSpending(body.getSpending());
        taskInfoRepository.save(taskInfo);
        return Constant.STATUS_SUCCESS;
    }
}
