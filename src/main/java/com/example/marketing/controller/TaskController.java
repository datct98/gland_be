package com.example.marketing.controller;

import com.example.marketing.model.dto.TaskScriptDTO;
import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.script_setting.Task;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.TaskRepository;
import com.example.marketing.service.TaskService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private JWTUtil jwtUtil;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @Operation(description = "Get tasks page by scriptID ")
    @GetMapping
    public ResponseEntity<?> getTasks(@RequestHeader(name="Authorization") String token,
                                      @RequestParam long scriptId,
                                      @RequestParam(required = false) Integer pageNum){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        if(pageNum == null){
            List<Task> tasks = taskRepository.findAllByScriptId(scriptId);
            return ResponseEntity.ok(new DataResponse<>(tasks));
        }
        Page<Task> tasks = taskRepository.findAllByScriptId(scriptId, PageRequest.of(pageNum, Constant.PAGE_SIZE));
        return ResponseEntity.ok(new DataResponse<>(tasks.getContent(), tasks.getTotalPages()));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @Operation(description = "Get other tasks for ket noi du lieu scriptID ")
    @GetMapping("/other")
    public ResponseEntity<?> getOtherTasks(@RequestHeader(name="Authorization") String token,
                                      @RequestParam long scriptId){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        List<Task> tasks = taskRepository.findAllByScriptIdIsNot(scriptId);
        return ResponseEntity.ok(new DataResponse<>(tasks));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(description = "Used to config or create task")
    @PostMapping
    public ResponseEntity<?> configTask(@RequestHeader(name="Authorization") String token,
                                        @RequestBody TaskScriptDTO body){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        String responseCode = taskService.modifyTask(body);
        if(Constant.STATUS_SUCCESS.equals(responseCode)){
            return ResponseEntity.ok(new DataResponse<>(HttpStatus.OK.value(), "Thao tác thành công!"));
        }
        return ResponseEntity.badRequest().body(new DataResponse<>(Integer.parseInt(responseCode), Constant.MESSAGE_ERR.get(responseCode)));
    }


}
