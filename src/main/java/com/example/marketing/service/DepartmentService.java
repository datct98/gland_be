package com.example.marketing.service;

import com.example.marketing.model.dto.DepartmentDTO;
import com.example.marketing.model.dto.DepartmentScriptDTO;
import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.Department;
import com.example.marketing.model.entities.Script;
import com.example.marketing.model.entities.Work;
import com.example.marketing.model.entities.script_setting.Task;
import com.example.marketing.model.entities.script_setting.TaskInfo;
import com.example.marketing.model.entities.script_setting.TaskStatus;
import com.example.marketing.repository.DepartmentRepository;
import com.example.marketing.repository.ScriptRepository;
import com.example.marketing.repository.TaskInfoRepository;
import com.example.marketing.repository.TaskRepository;
import com.example.marketing.repository.TaskStatusRepository;
import com.example.marketing.repository.WorkRepository;
import com.example.marketing.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private ScriptRepository scriptRepository;
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
    @Autowired
    private TypeIdService typeIdService;

    public void modifyDepartmentService(Department body, String username){
        // Tồn tại thì update
        if(body.getId()!=null){
            Department department = departmentRepository.findById(body.getId()).orElse(null);
            if(department == null){
                log.error("#modifyDepartmentService cant find department with id {} ", body.getId());
                return;
            }
            department.setNote(body.getNote());
            department.setName(body.getName());
            //body.setStatus(true);
            departmentRepository.save(department);
        } else {
            // Thêm mới
            Department department = new Department();
            department.setStatus(true);
            department.setCreatedBy(username);
            department.setName(body.getName());
            department.setNote(body.getNote());
            departmentRepository.save(department);
        }

    }

    public Page<DepartmentScriptDTO> getPageDepartments(UserDTO userDTO, int pageNum){
        return departmentRepository.findAllByCreatedBy(userDTO.getUsername(), userDTO.isAdmin(), PageRequest.of(pageNum, Constant.PAGE_SIZE));
    }

    public List<DepartmentScriptDTO> getDepartments(UserDTO userDTO){
        return departmentRepository.findAllByCreatedBy(userDTO.getUsername(), userDTO.isAdmin());
    }

    public List<DepartmentDTO> convertDepartmentScriptDTOsToDepartmentDTOs(List<DepartmentScriptDTO> departmentScriptDTOs) {
        Map<Long, List<DepartmentScriptDTO>> groupedDepartmentScriptDTOs = departmentScriptDTOs.stream()
                .collect(Collectors.groupingBy(DepartmentScriptDTO::getDepartmentId));

        List<DepartmentDTO> departmentDTOs = new ArrayList<>();

        for (Map.Entry<Long, List<DepartmentScriptDTO>> entry : groupedDepartmentScriptDTOs.entrySet()) {
            Long departmentId = entry.getKey();
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setDepartmentId(departmentId);

            List<DepartmentScriptDTO> departmentScripts = entry.getValue();
            DepartmentScriptDTO firstDepartmentScript = departmentScripts.get(0);
            departmentDTO.setDepartmentName(firstDepartmentScript.getDepartmentName());
            departmentDTO.setNoteDepartment(firstDepartmentScript.getNoteDepartment());
            departmentDTO.setCreatedDateDepartment(firstDepartmentScript.getCreatedDateDepartment());

            List<Script> scripts = departmentScripts.stream()
                    .filter(departmentScriptDTO -> departmentScriptDTO.getScriptId() != null)
                    .map(departmentScriptDTO -> {
                        Script script = new Script();
                        script.setId(departmentScriptDTO.getScriptId());
                        script.setName(departmentScriptDTO.getScriptName());
                        script.setDepartmentId(departmentId);
                        script.setNote(departmentScriptDTO.getNoteScript());
                        script.setCreatedAt(departmentScriptDTO.getCreatedDateScript());
                        return script;
                    })
                    .distinct()
                    .collect(Collectors.toList());
            departmentDTO.setScripts(scripts);

            departmentDTOs.add(departmentDTO);
        }

        return departmentDTOs;
    }

    public String deleteDepartment(int id){
        Department department = departmentRepository.findById((long) id).orElse(null);
        if(department == null){
            return Constant.DEPARTMENT_NOT_EXISTED;
        }
        try {
            List<Script> scripts = scriptRepository.findAllByDepartmentId(id);
            if(scripts.size()>0){
                List<Long> idScripts = scripts.stream().map(Script::getId).collect(Collectors.toList());
                List<Task> tasks = taskRepository.findAllByScriptIdIn(idScripts);
                if(tasks.size()> 0 ){
                    List<Long> idTasks = tasks.stream().map(Task::getId).collect(Collectors.toList());
                    List<TaskInfo> infos = taskInfoRepository.findAllByTaskIdIn(idTasks);
                    if(infos.size()>0){
                        taskInfoRepository.deleteAll(infos);
                    }
                    List<TaskStatus> taskStatuses = taskStatusRepository.findAllByTaskIdIn(idTasks);
                    if(taskStatuses.size()>0){
                        taskStatusRepository.deleteAll(taskStatuses);
                    }
                    List<Work> works = workRepository.findAllByTaskIdIn(idTasks);
                    if(works.size()>0){
                        List<String>idWorks = works.stream().map(Work::getId).collect(Collectors.toList());
                        log.info("#deleteById works size: "+works.size());
                        dataConnectService.deleteData(idWorks, idScripts);
                        workRepository.deleteAll(works);
                    }
                    taskScriptConfigService.deleteConfig(idTasks, idScripts);
                    taskRepository.deleteAll(tasks);
                }
                scriptRepository.deleteAll(scripts);
            }
            // delete kho dữ liệu
            typeIdService.deleteTypeId(id);
            departmentRepository.delete(department);
            return Constant.STATUS_SUCCESS;
        } catch (Exception e){
            log.error("#deleteDepartment {}", e.getMessage());
            return Constant.SYS_ERR;
        }
    }
}
