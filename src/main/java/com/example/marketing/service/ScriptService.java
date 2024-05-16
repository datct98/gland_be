package com.example.marketing.service;

import com.example.marketing.model.dto.ScriptConnectDTO;
import com.example.marketing.model.entities.DataConnection;
import com.example.marketing.model.entities.Script;
import com.example.marketing.model.entities.Work;
import com.example.marketing.repository.DataConnectRepository;
import com.example.marketing.repository.ScriptRepository;
import com.example.marketing.repository.WorkRepository;
import com.example.marketing.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScriptService {
    @Autowired
    private ScriptRepository scriptRepository;
    @Autowired
    private DataConnectRepository dataConnectRepository;
    @Autowired
    private WorkRepository workRepository;

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

    public List<ScriptConnectDTO> getOtherScripts(long scriptId, long departmentId, String idWork){
        Work work = workRepository.findById(idWork).orElse(null);
        if(work == null){
            log.error("Không tìm thấy công vc có id: "+idWork);
            return null;
        }
        List<Script> scripts = scriptRepository.findByIdIsNotAndDepartmentId(scriptId, departmentId, work.getTaskId());
        List<DataConnection> connections = dataConnectRepository.findAllByIdFrom(idWork);
        List<ScriptConnectDTO> dtos = scripts.stream()
                .map(script -> new ScriptConnectDTO(script.getId(), script.getName(),
                        connections.stream()
                                .anyMatch(connection -> script.getId() == connection.getIdTo() && connection.getConnected())))
                .collect(Collectors.toList());

        return dtos;
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

    public String connectData(List<DataConnection> bodies){
        DataConnection connection;
        List<DataConnection> connections = new ArrayList<>();

        for (DataConnection body: bodies){
            List<DataConnection> connectionsFound = dataConnectRepository.findAllByIdFromAndIdTo(body.getIdFrom(), body.getIdTo());
            if(connectionsFound.size()>0){
                connection = connectionsFound.get(0);
                connection.setConnected(body.getConnected());
            } else {
                connection = new DataConnection();
                connection.setIdFrom(body.getIdFrom());
                connection.setIdTo(body.getIdTo());
                connection.setConnected(body.getConnected());
            }
            connections.add(connection);
        }
        dataConnectRepository.saveAll(connections);
        return Constant.STATUS_SUCCESS;
    }
}
