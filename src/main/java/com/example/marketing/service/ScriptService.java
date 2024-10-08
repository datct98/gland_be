package com.example.marketing.service;

import com.example.marketing.model.dto.ScriptConnectDTO;
import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.DataConnection;
import com.example.marketing.model.entities.Department;
import com.example.marketing.model.entities.Script;
import com.example.marketing.model.entities.User;
import com.example.marketing.model.entities.Work;
import com.example.marketing.model.entities.script_setting.Task;
import com.example.marketing.model.entities.script_setting.TaskInfo;
import com.example.marketing.model.entities.script_setting.TaskStatus;
import com.example.marketing.repository.DataConnectRepository;
import com.example.marketing.repository.DepartmentRepository;
import com.example.marketing.repository.ScriptRepository;
import com.example.marketing.repository.TaskInfoRepository;
import com.example.marketing.repository.TaskRepository;
import com.example.marketing.repository.TaskStatusRepository;
import com.example.marketing.repository.UserRepository;
import com.example.marketing.repository.WorkRepository;
import com.example.marketing.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScriptService {
    @Autowired
    private ScriptRepository scriptRepository;
    @Autowired
    private DataConnectRepository dataConnectRepository;
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskInfoRepository taskInfoRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private DataConnectService dataConnectService;
    @Autowired
    private TaskScriptConfigService taskScriptConfigService;

    public String modifyScript(Script body, String createdBy){
        try {
            Script script;
            if(body.getId() == null)
                script = new Script();
            else {
                script = scriptRepository.findById(body.getId()).orElse(null);
                if(script == null){
                    log.error("#ScriptService - modifyScript fail, can't find script with id: "+body.getId());
                    return null;
                }
            }
            script.setName(body.getName());
            script.setCreatedBy(createdBy);
            script.setStatus(true);
            script.setDepartmentId(body.getDepartmentId());
            scriptRepository.save(script);
            return Constant.STATUS_SUCCESS;
        } catch (Exception e){
            log.error("#ScriptService - modifyScript fail: {}", e.getMessage());
            return null;
        }

    }

    public Page<ScriptConnectDTO> getScripts(Long departmentId, Pageable pageable){
        Page<ScriptConnectDTO> scripts = scriptRepository.findAllByDepartmentIdAndStatusOrderByCreatedAtDesc(departmentId, true, pageable);
        return scripts;
    }

    public List<ScriptConnectDTO> getOtherScripts(long scriptId, long departmentId, String idWork){
        Work work = workRepository.findById(idWork).orElse(null);
        if(work == null){
            log.error("Không tìm thấy công vc có id: "+idWork);
            return null;
        }
        List<Script> scripts = scriptRepository.findByIdIsNotAndDepartmentId(scriptId, departmentId, work.getTaskId());
        List<DataConnection> connections = dataConnectRepository.findAllByIdFrom(idWork);
        List<ScriptConnectDTO> dtos = scripts.stream()
                .map(script -> new ScriptConnectDTO(script.getId(), script.getName(),
                        connections.stream()
                                .anyMatch(connection -> script.getId() == connection.getIdTo() && connection.getConnected())))
                .collect(Collectors.toList());

        return dtos;
    }

    /*public List<ScriptConnectDTO> getAssignedScripts(long departmentId, String idWork){
        Work work = workRepository.findById(idWork).orElse(null);
        if(work == null){
            log.error("Không tìm thấy công vc có id: "+idWork);
            return null;
        }
        List<Script> scripts = scriptRepository.findByIdIsNotAndDepartmentId(scriptId, departmentId, work.getTaskId());
        List<DataConnection> connections = dataConnectRepository.findAllByIdFrom(idWork);
        List<ScriptConnectDTO> dtos = scripts.stream()
                .map(script -> new ScriptConnectDTO(script.getId(), script.getName(),
                        connections.stream()
                                .anyMatch(connection -> script.getId() == connection.getIdTo() && connection.getConnected())))
                .collect(Collectors.toList());

        return dtos;
    }*/

    public String deleteById(long id, long departmentId, Boolean isAdmin){
        Script script = scriptRepository.findById(id).orElse(null);
        if(script == null)
            return Constant.SCRIPT_NOT_EXISTED;
        else {
            try {
                List<Task> tasks = taskRepository.findAllByScriptId(id);
                if(tasks.size()>0){
                    List<Long> idTasks = tasks.stream().map(Task::getId).collect(Collectors.toList());
                    List<Work> works = workRepository.findAllByTaskIdIn(idTasks);
                    List<String>idWorks = works.stream().map(Work::getId).collect(Collectors.toList());
                    log.info("#deleteById works size: "+works.size());
                    dataConnectService.deleteData(idWorks, new ArrayList<>(List.of(id)));
                    if(works.size()>0){
                        workRepository.deleteAll(works);
                    }
                    List<TaskInfo> taskInfos = taskInfoRepository.findAllByTaskIdIn(idTasks);
                    log.info("#deleteById taskInfos size: "+taskInfos.size());
                    if(taskInfos.size()>0){
                        taskInfoRepository.deleteAll(taskInfos);
                    }
                    List<TaskStatus> taskStatuses = taskStatusRepository.findAllByTaskIdIn(idTasks);
                    log.info("#deleteById taskStatuses size: "+taskStatuses.size());
                    if(taskStatuses.size()>0){
                        taskStatusRepository.deleteAll(taskStatuses);
                    }
                    taskScriptConfigService.deleteConfig(idTasks, List.of(id));
                    taskRepository.deleteAll(tasks);
                }
                scriptRepository.delete(script);
                log.info("deleteById - delete script with id: {} successfully", id);
                return Constant.STATUS_SUCCESS;
            } catch (Exception e){
                log.error("deleteById - delete script with id: {} fail: {}", id, e);
                return Constant.SYS_ERR;
            }
        }
    }

    public String connectData(List<DataConnection> bodies){
        List<DataConnection> connections = new ArrayList<>();
        List<Work> works = new ArrayList<>();

        for (DataConnection body: bodies){
            Work work = workRepository.findById(body.getIdFrom()).orElse(null);
            if(work == null){
                return "Không tìm thấy work id:"+body.getIdFrom();
            }
            work.setStatus("Assigned");

            // Khi giao việc thì cho phép giao đến cả kịch bản đã tạo ra công việc đó (work.getScriptId())
            List<DataConnection> connectionsFound = dataConnectRepository.findAllByIdFromAndIdToIn(body.getIdFrom(), List.of(body.getIdTo(), work.getScriptId()));
            boolean checkWorkScriptId = false;
            if(connectionsFound.size()>0){
                for (DataConnection dc: connectionsFound){
                    if(dc.getIdTo() != work.getScriptId()){
                        dc.setConnected(body.getConnected());
                        connections.add(dc);
                    } else {
                        checkWorkScriptId = true;
                    }
                }
            } else {
                DataConnection connection = new DataConnection();
                connection.setIdFrom(body.getIdFrom());
                connection.setIdTo(body.getIdTo());
                connection.setConnected(body.getConnected());
                connections.add(connection);
            }

            if(!checkWorkScriptId){
                // Thực hiện kết nối cv đến kịch bản đã tạo ra cv
                DataConnection connection = new DataConnection();
                connection.setIdFrom(body.getIdFrom());
                connection.setIdTo(work.getScriptId());
                connection.setConnected(false);
                connections.add(connection);
            }

            List<UserDTO> users = userRepository.findAllByRoleAndDepartmentId("leader",work.getDepartmentId());
            if(users.size() >0){
                work.setAssignee(users.get(0).getUsername());
                //work.setStatus("Assigned");
            }
            works.add(work);

        }
        dataConnectRepository.saveAll(connections);
        if(works.size()>0)
            workRepository.saveAll(works);
        return Constant.STATUS_SUCCESS;
    }
}
