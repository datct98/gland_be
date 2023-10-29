package com.example.marketing.controller;

import com.example.marketing.model.entities.Department;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.DepartmentRepository;
import com.example.marketing.service.DepartmentService;
import com.example.marketing.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api-department")
@Slf4j
public class DepartmentController {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DepartmentService departmentService;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/create")
    public ResponseEntity<?>createDepartment(@RequestHeader(name="Authorization") String token,
                                             @RequestBody Department department) throws Exception {
        token= token.replace("Bearer ","");
        if(jwtUtil.validateToken(token)){
            department.setCreatedBy(jwtUtil.getUsernameFromJwt(token));
            departmentRepository.save(department);
            return ResponseEntity.ok(new DataResponse<>(departmentRepository.findAllByStoreId(department.getStoreId())));
        }
        throw new Exception("Un-authentication");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/all")
    public ResponseEntity<?>getDepartments(@RequestHeader(name="Authorization") String token,
                                           @RequestParam long storeId,
                                           @RequestParam int pageNum) throws Exception {
        token= token.replace("Bearer ","");
        if(jwtUtil.validateToken(token)){
            Page<Department> page = departmentRepository.findAllByStoreId(storeId, PageRequest.of(pageNum,10));
            List<Department> departments = page.getContent();
            departments.forEach(e->e.setScripts(departmentService.getDepartmentScripts(storeId).get(e.getId())));
            return ResponseEntity.ok(new DataResponse<>(departments, page.getTotalPages()));
        }
        throw new Exception("Un-authentication");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("")
    public ResponseEntity<?>getDepartmentsWithoutPage(@RequestHeader(name="Authorization") String token,
                                           @RequestParam long storeId,
                                           @RequestParam long typeTaskId) throws Exception {
        token= token.replace("Bearer ","");
        if(jwtUtil.validateToken(token)){
            List<Department> departments = departmentRepository.findAllByStoreId(storeId);
            departments.forEach(e->e.setScripts(departmentService.getDepartmentScriptsAndRole(storeId, typeTaskId).get(e.getId())));
            return ResponseEntity.ok(new DataResponse<>(departments));
        }
        throw new Exception("Un-authentication");
    }
}
