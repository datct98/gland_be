package com.example.marketing.controller;

import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.script_setting.TaskInfo;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.TaskInfoRepository;
import com.example.marketing.service.TaskInfoService;
import com.example.marketing.util.Constant;
import com.example.marketing.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/task-info")
public class TaskInfoController {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private TaskInfoRepository taskInfoRepository;
    @Autowired
    private TaskInfoService taskInfoService;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @Operation(description = "Get list info of task")
    @GetMapping
    public ResponseEntity<?> getTaskInfo(@RequestHeader(name="Authorization") String token,
                                           @RequestParam long taskId){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        List<TaskInfo> taskInfos = taskInfoRepository.findAllByTaskId(taskId);
        return ResponseEntity.ok(new DataResponse<>(taskInfos));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(description = "Modify info of task. Id = null -> Insert else update")
    @PostMapping
    public ResponseEntity<?> modifyTaskInfo(@RequestHeader(name="Authorization") String token,
                                              @RequestBody TaskInfo body){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        String responseCode = taskInfoService.modifyTaskInfo(body);
        if(Constant.TASK_STATUS_NOT_EXISTED.equals(responseCode)){
            return ResponseEntity.badRequest().body(new DataResponse<>(Constant.MESSAGE_ERR.get(responseCode)));
        }
        return ResponseEntity.ok(new DataResponse<>("Thao tác thành công"));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.DELETE)
    @Operation(description = "Delete a field of task")
    @DeleteMapping
    public ResponseEntity<?> deleteTaskInfo(@RequestHeader(name="Authorization") String token,
                                            @RequestParam long id){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        String responseCode = taskInfoService.deleteInfo(id);
        if(Constant.TASK_STATUS_NOT_EXISTED.equals(responseCode)){
            return ResponseEntity.badRequest().body(new DataResponse<>(Constant.MESSAGE_ERR.get(responseCode)));
        }
        return ResponseEntity.ok(new DataResponse<>("Thao tác thành công"));
    }
}
