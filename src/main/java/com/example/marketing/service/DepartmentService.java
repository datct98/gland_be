package com.example.marketing.service;

import com.example.marketing.model.dto.DepartmentScriptDTO;
import com.example.marketing.model.entities.Department;
import com.example.marketing.model.entities.RoleTask;
import com.example.marketing.model.entities.Script;
import com.example.marketing.repository.DepartmentRepository;
import com.example.marketing.repository.RoleTaskRepository;
import com.example.marketing.repository.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class DepartmentService {
    @Autowired
    private ScriptRepository scriptRepository;
    @Autowired
    private RoleTaskRepository roleTaskRepository;

    public Map<Long, List<Script>> getDepartmentScripts(long storeId){
        List<Script> scripts = scriptRepository.findAllByStoreId(storeId);
        Map<Long, List<Script>> scriptMap = scripts.stream()
                .collect(Collectors.groupingBy(Script::getDepartmentId));

        return scriptMap;
    }

    public Map<Long, List<Script>> getDepartmentScriptsAndRole(long storeId, long typeTaskId){
        List<Script> scripts = scriptRepository.findAllByStoreId(storeId);
        List<Long> scriptIds = scripts.stream().map(e-> e.getId()).collect(Collectors.toList());
        scripts.forEach(e->{
            List<RoleTask> roleTasks = getRoleTaskByScriptId(typeTaskId, scriptIds).get(e.getId());
            if(roleTasks!= null && roleTasks.size()>0 && roleTasks.get(0)!=null)
                e.setRoleTask(roleTasks.get(0));
            else e.setRoleTask(null);
        } );

        return scripts.stream()
                .collect(Collectors.groupingBy(Script::getDepartmentId));
    }

    private Map<Long, List<RoleTask>> getRoleTaskByScriptId(long typeTaskId, List<Long>scriptIds){
        List<RoleTask> roleTasks = roleTaskRepository.findAllByScriptIdInAndTypeTaskId(scriptIds, typeTaskId);
        Map<Long, List<RoleTask>> roleTaskMap = roleTasks.stream().collect(Collectors.groupingBy(RoleTask::getScriptId));
        return roleTaskMap;
    }
}
