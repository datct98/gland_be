package com.example.marketing.controller;

import com.example.marketing.model.entities.RoleTask;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.RoleTaskRepository;
import com.example.marketing.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/role-task")
public class RoleTaskController {
    @Autowired
    private RoleTaskRepository roleTaskRepository;
    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/update")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    public ResponseEntity<?> updateRole(@RequestBody RoleTask body,
                                        HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        if(jwtUtil.validateToken(token.replace("Bearer ",""))){
            RoleTask roleTask = roleTaskRepository.findByTypeTaskIdAndScriptId(body.getTypeTaskId(), body.getScriptId());
            if(roleTask!= null){
                if(body.getAssignedJob()!= null){
                    roleTask.setAssignedJob(body.getAssignedJob());
                }
                if(body.getMyJob()!= null){
                    roleTask.setAssignedJob(body.getMyJob());
                }
                roleTaskRepository.save(roleTask);
                return ResponseEntity.ok(new DataResponse<>(HttpStatus.OK.value(), ""));
            }
            roleTaskRepository.save(body);
            return ResponseEntity.ok(new DataResponse<>(HttpStatus.OK.value(), ""));
        }
        throw new Exception("Un-authentication");
    }
}
