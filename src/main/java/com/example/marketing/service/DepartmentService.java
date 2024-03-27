package com.example.marketing.service;

import com.example.marketing.model.dto.DepartmentDTO;
import com.example.marketing.model.dto.DepartmentScriptDTO;
import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.Department;
import com.example.marketing.model.entities.Script;
import com.example.marketing.repository.DepartmentRepository;
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
}
