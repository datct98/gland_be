package com.example.marketing.service;

import com.example.marketing.model.dto.RoleScriptDTO;
import com.example.marketing.model.dto.TaskScriptDTO;
import com.example.marketing.model.entities.Work;
import com.example.marketing.model.entities.script_setting.Task;
import com.example.marketing.model.entities.script_setting.TaskInfo;
import com.example.marketing.model.entities.script_setting.TaskScriptConfig;
import com.example.marketing.model.entities.script_setting.TaskStatus;
import com.example.marketing.model.entities.stock.TypeIdInfo;
import com.example.marketing.repository.TaskInfoRepository;
import com.example.marketing.repository.TaskRepository;
import com.example.marketing.repository.TaskScriptConfigRepository;
import com.example.marketing.repository.TaskStatusRepository;
import com.example.marketing.repository.WorkRepository;
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
    @Autowired
    private TaskInfoRepository taskInfoRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private TaskScriptConfigService taskScriptConfigService;

    public String modifyTask(TaskScriptDTO body){
        try{
            Task task;
            if(body.getTaskId() == null){
                task = new Task();
                task.setName(body.getNameTask());
                //task.setAcronym(body.getAcronym());
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
                task.setTypeId(body.getIdAuto()? "idAuto": "idCustom");
                if("idCustom".equalsIgnoreCase(task.getTypeId())){
                    body.setPreCode(null);
                    task.setPreCode(null);
                }
                if(StringUtils.isNotEmpty(body.getPreCode()) && body.getIdAuto()){
                    if(!body.getPreCode().equalsIgnoreCase(task.getPreCode())){
                        List<Task> tasks = taskRepository.findAllByPreCode(body.getPreCode());
                        if(tasks.size()>0){
                            return "Từ viết tắt đã tồn tại";
                        }
                    }
                    task.setPreCode(body.getPreCode());
                }

                //Set data for task Id
                List<TaskInfo> infosExisted = taskInfoRepository.findAllByTaskId(task.getId());
                List<TaskInfo> infoNews = new ArrayList<>();
                if(infosExisted.size() ==0 ){
                    // Default have Id column
                    TaskInfo taskInfo = new TaskInfo();
                    taskInfo.setTaskId(task.getId());
                    taskInfo.setField("Id");
                    taskInfo.setDataType("text");
                    taskInfo.setStatus(true);

                    if("idAuto".equalsIgnoreCase(task.getTypeId())){
                        taskInfo.setIdAuto(true);
                        taskInfo.setIdCustom(false);
                        taskInfo.setPreCode(body.getPreCode());
                    } else {
                        taskInfo.setIdAuto(false);
                        taskInfo.setIdCustom(true);
                        taskInfo.setPreCode(null);
                    }
                    infoNews.add(taskInfo);
                } else {
                    for (TaskInfo info: infosExisted){
                        // update riêng cho cột Id
                        if("Id".equalsIgnoreCase(info.getField())){
                            if("idAuto".equalsIgnoreCase(task.getTypeId())){
                                info.setIdAuto(true);
                                info.setIdCustom(false);
                                info.setPreCode(body.getPreCode());
                            } else {
                                info.setIdAuto(false);
                                info.setIdCustom(true);
                                info.setPreCode(null);
                            }
                            taskInfoRepository.save(info);
                            break;
                        }
                    }
                }
                List<TaskInfo> infos = body.getInfos();
                for(TaskInfo info: infos){
                    TaskInfo infoNew;
                    if(info.getId() == null || info.getId() == 0){
                        infoNew = new TaskInfo();
                    } else {
                        infoNew = taskInfoRepository.findById(info.getId()).orElse(null);
                        if(infoNew == null){
                            return "Không tìm thấy id info: "+info.getId();
                        }
                    }
                    infoNew.setDataType(info.getDataType());
                    infoNew.setField(info.getField());
                    infoNew.setTaskId(info.getTaskId());
                    infoNew.setOptionValue(info.getOptionValue());
                    infoNews.add(infoNew);
                }
                taskInfoRepository.saveAll(infoNews);

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
                        //e.setCreated(mapRole.get(e.getScriptId()).getCreated());
                        e.setAssigned(mapRole.get(e.getScriptId()).getAssigned());
                        e.setCommitted(mapRole.get(e.getScriptId()).getCommitted());
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
                        config.setStatus(true);
                        //config.setCreated(roleScript.getCreated());
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
                    //config.setCreated(scriptDTO.getCreated());
                    config.setCommitted(scriptDTO.getCommitted());
                    taskScriptConfigsNew.add(config);
                }
            }
            taskScriptConfigRepository.saveAll(taskScriptConfigsNew);
        }
    }

    public TaskScriptDTO getDetail(long taskId){
        Task task = taskRepository.findById(taskId).orElse(null);
        if(task!= null){
            List<TaskInfo> infos = taskInfoRepository.findAllByTaskId(taskId);
            TaskScriptDTO dto = new TaskScriptDTO();
            dto.setTaskId(taskId);
            dto.setNameTask(task.getName());
            dto.setHourDefault(task.getHourDefault());
            dto.setInfos(infos);
            dto.setScriptId(task.getScriptId());
            dto.setPreCode(task.getPreCode());
            dto.setIdAuto("idAuto".equalsIgnoreCase(task.getTypeId()));
            dto.setIdCustom("idCustom".equalsIgnoreCase(task.getTypeId()));
            return dto;
        }
        return null;
    }

    public String deleteTask(long id){
        Task task = taskRepository.findById(id).orElse(null);
        if(task == null){
            return Constant.TASK_NOT_EXISTED;
        }
        List<Work> works = workRepository.findAllByTaskId(id);
        if(works.size()>0)
            workRepository.deleteAll(works);
        List<TaskInfo> taskInfos = taskInfoRepository.findAllByTaskId(id);
        log.info("#deleteById taskInfos size: "+taskInfos.size());
        if(taskInfos.size()>0){
            taskInfoRepository.deleteAll(taskInfos);
        }
        List<TaskStatus> taskStatuses = taskStatusRepository.findAllByTaskId(id);
        log.info("#deleteById taskStatuses size: "+taskStatuses.size());
        if(taskStatuses.size()>0){
            taskStatusRepository.deleteAll(taskStatuses);
        }
        taskScriptConfigService.deleteConfig(List.of(task.getId()), new ArrayList<>());
        taskRepository.delete(task);
        return Constant.STATUS_SUCCESS;
    }
}
