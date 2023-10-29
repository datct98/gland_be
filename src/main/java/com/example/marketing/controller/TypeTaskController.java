package com.example.marketing.controller;

import com.example.marketing.model.entities.Script;
import com.example.marketing.model.entities.TypeTask;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.TypeTaskRepository;
import com.example.marketing.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/api-type-task")
public class TypeTaskController {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private TypeTaskRepository typeTaskRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/create")
    public ResponseEntity<?> createTypeTask(@RequestHeader(name="Authorization") String token,
                                          @RequestBody TypeTask body) throws Exception {
        token= token.replace("Bearer ","");
        if(jwtUtil.validateToken(token)){
            typeTaskRepository.save(body);
            Page<TypeTask> typeTasks = typeTaskRepository.findAllByScriptId(body.getScriptId(), PageRequest.of(0, 10));

            return ResponseEntity.ok(new DataResponse<>(typeTasks.getContent(), typeTasks.getTotalPages()));
        }
        throw new Exception("Un-authentication");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/all")
    public ResponseEntity<?>getTypeTask(@RequestHeader(name="Authorization") String token,
                                       @RequestParam long scriptId,
                                       @RequestParam int pageNum) throws Exception {
        token= token.replace("Bearer ","");
        if(jwtUtil.validateToken(token)){
            if(pageNum!= -1){
                Page<TypeTask> typeTasks = typeTaskRepository.findAllByScriptId(scriptId, PageRequest.of(pageNum, 10));
                return ResponseEntity.ok(new DataResponse<>(typeTasks.getContent(), typeTasks.getTotalPages()));
            }
            return ResponseEntity.ok(new DataResponse<>(typeTaskRepository.findAllByScriptId(scriptId)));
        }
        throw new Exception("Un-authentication");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping()
    public ResponseEntity<?>getTypeTaskNoPage(@RequestHeader(name="Authorization") String token,
                                        @RequestParam long scriptId,
                                        @RequestHeader(required = false)String type) throws Exception {
        token= token.replace("Bearer ","");
        if(jwtUtil.validateToken(token)){
            List<TypeTask> typeTasks;
            if(type.equalsIgnoreCase("myJob")){
                typeTasks = typeTaskRepository.findAllMyJobByScriptId(scriptId);
            } else {
                typeTasks = typeTaskRepository.findAllAssignJobByScriptId(scriptId);
            }
            return ResponseEntity.ok(new DataResponse<>(typeTasks));

        }
        throw new Exception("Un-authentication");
    }
}
