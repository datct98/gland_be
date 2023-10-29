package com.example.marketing.repository;

import com.example.marketing.model.entities.RoleTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RoleTaskRepository extends JpaRepository<RoleTask, Long> {
    RoleTask findByTypeTaskIdAndScriptId(long typeTaskId, long scriptId);
    List<RoleTask> findAllByScriptIdInAndTypeTaskId(List<Long> scriptIds, long typeTaskId);
}
