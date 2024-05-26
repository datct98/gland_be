package com.example.marketing.controller;

import com.example.marketing.model.dto.ScriptConnectDTO;
import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.Script;
import com.example.marketing.model.entities.script_setting.Task;
import com.example.marketing.model.entities.script_setting.TaskScriptConfig;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.ScriptRepository;
import com.example.marketing.repository.TaskRepository;
import com.example.marketing.repository.TaskScriptConfigRepository;
import com.example.marketing.service.ScriptService;
import com.example.marketing.util.Constant;
import com.example.marketing.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/task-script-config")
@Slf4j
public class TaskScriptConfigController {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private TaskScriptConfigRepository taskScriptConfigRepository;
    @Autowired
    private ScriptRepository scriptRepository;
    @Autowired
    private TaskRepository taskRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @Operation(description = "Get scripts and return page")
    @GetMapping
    public ResponseEntity<?> getScriptsConfiged(@RequestHeader(name="Authorization") String token,
                                        @RequestParam Long departmentId,@RequestParam long taskId){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        Task task = taskRepository.findById(taskId).orElse(null);
        if(task == null){
            return ResponseEntity.internalServerError().body("Cant find task with id: "+taskId);
        }

        List<ScriptConnectDTO> dtos = scriptRepository.findScriptsByDepartmentId(departmentId,task.getScriptId());
        List<Long>sciptIds = dtos.stream().map(ScriptConnectDTO::getId).collect(Collectors.toList());
        List<TaskScriptConfig> configs = taskScriptConfigRepository.findAllByTaskIdAndScriptIdIn(taskId, sciptIds);
        Map<Long,TaskScriptConfig> mapConfig = configs.stream().collect(Collectors.toMap(TaskScriptConfig::getScriptId, e -> e));
        for (ScriptConnectDTO dto: dtos){
            if (mapConfig.containsKey(dto.getId())){
                dto.setAssigned(mapConfig.get(dto.getId()).getAssigned());
                dto.setCommitted(mapConfig.get(dto.getId()).getCommitted());
            }
        }
        return ResponseEntity.ok(dtos);
    }
}
