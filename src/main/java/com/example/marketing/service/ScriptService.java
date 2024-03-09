package com.example.marketing.service;

import com.example.marketing.model.entities.Script;
import com.example.marketing.repository.ScriptRepository;
import com.example.marketing.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScriptService {
    @Autowired
    private ScriptRepository scriptRepository;

    public Page<Script> modifyScript(Script body, String createdBy){
        try {
            Script script;
            if(body.getId() == null)
                script = new Script();
            else {
                script = scriptRepository.findById(body.getId()).orElse(null);
                if(script == null){
                    log.error("#ScriptService - modifyScript fail, can't find script with id: "+body.getId());
                    return null;
                }
            }
            script.setName(body.getName());
            script.setCreatedBy(createdBy);
            script.setStatus(true);
            script.setDepartmentId(body.getDepartmentId());
            scriptRepository.save(script);
            // Return new List<Script>
            Page<Script> scripts = scriptRepository.findAllByDepartmentIdAndStatusOrderByCreatedAtDesc
                    (body.getDepartmentId(), true, PageRequest.of(0, Constant.PAGE_SIZE));
            return scripts;
        } catch (Exception e){
            log.error("#ScriptService - modifyScript fail: {}", e.getMessage());
            return null;
        }

    }
}
