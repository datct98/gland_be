package com.example.marketing.controller;

import com.example.marketing.model.dto.DepartmentScriptDTO;
import com.example.marketing.model.entities.Department;
import com.example.marketing.model.entities.Script;
import com.example.marketing.model.entities.StatusTypeTask;
import com.example.marketing.model.entities.User;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.DepartmentRepository;
import com.example.marketing.repository.ScriptRepository;
import com.example.marketing.repository.StatusScriptRepository;
import com.example.marketing.service.DepartmentService;
import com.example.marketing.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api-script")
public class ScriptController {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private ScriptRepository scriptRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/create")
    public ResponseEntity<?>createScript(@RequestHeader(name="Authorization") String token,
                                         @RequestParam(required = false) long storeId,
                                         @RequestBody Script script) throws Exception {
        token= token.replace("Bearer ","");
        if(jwtUtil.validateToken(token)){
            script.setCreatedBy(jwtUtil.getUsernameFromJwt(token));
            scriptRepository.save(script);
            Page<Script> scriptPage = scriptRepository.findAllByDepartmentId(script.getDepartmentId(), PageRequest.of(0, 10));

            return ResponseEntity.ok(new DataResponse<>(scriptPage.getContent(), scriptPage.getTotalPages()));
        }
        throw new Exception("Un-authentication");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/all")
    public ResponseEntity<?>getScripts(@RequestHeader(name="Authorization") String token,
                                       @RequestParam long departmentId,
                                       @RequestParam int pageNum) throws Exception {
        token= token.replace("Bearer ","");
        if(jwtUtil.validateToken(token)){
            Page<Script> scriptPage = scriptRepository.findAllByDepartmentId(departmentId, PageRequest.of(pageNum, 10));
            return ResponseEntity.ok(new DataResponse<>(scriptPage.getContent(), scriptPage.getTotalPages()));
        }
        throw new Exception("Un-authentication");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.PUT)
    @PutMapping("/{id}")
    public ResponseEntity<?>editScript(@RequestHeader(name="Authorization") String token,
                                         @PathVariable long id,
                                         @RequestBody Script body) throws Exception {
        token= token.replace("Bearer ","");
        if(jwtUtil.validateToken(token)){
            Script script = scriptRepository.findById(id).orElse(null);
            if(script == null)
                return ResponseEntity.ok(new DataResponse<>(HttpStatus.BAD_REQUEST.value(),"Không tìm thấy scriptId: "+id));
            script.setName(body.getName());
            scriptRepository.save(script);
            Page<Script> scriptPage = scriptRepository.findAllByDepartmentId(script.getDepartmentId(), PageRequest.of(0, 10));

            return ResponseEntity.ok(new DataResponse<>(scriptPage.getContent(), scriptPage.getTotalPages()));
        }
        throw new Exception("Un-authentication");
    }
}
