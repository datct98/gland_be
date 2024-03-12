package com.example.marketing.service;

import com.example.marketing.model.entities.Script;
import com.example.marketing.repository.ScriptRepository;
import com.example.marketing.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ScriptService {
    @Autowired
    private ScriptRepository scriptRepository;

    public String modifyScript(Script body, String createdBy){
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
            return Constant.STATUS_SUCCESS;
        } catch (Exception e){
            log.error("#ScriptService - modifyScript fail: {}", e.getMessage());
            return null;
        }

    }

    public Page<Script> getScripts(long departmentId, Pageable pageable){
        Page<Script> scripts = scriptRepository.findAllByDepartmentIdAndStatusOrderByCreatedAtDesc(departmentId, true, pageable);
        return scripts;
    }

    public String deleteById(long id){
        Script script = scriptRepository.findById(id).orElse(null);
        if(script == null)
            return Constant.SCRIPT_NOT_EXISTED;
        else {
            try {
                scriptRepository.delete(script);
                log.info("deleteById - delete script with id: {} successfully", id);
                return Constant.STATUS_SUCCESS;
            } catch (Exception e){
                log.error("deleteById - delete script with id: {} fail: {}", id, e);
                return Constant.SYS_ERR;
            }
        }
    }
}
