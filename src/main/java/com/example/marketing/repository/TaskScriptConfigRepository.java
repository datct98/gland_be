package com.example.marketing.repository;

import com.example.marketing.model.entities.script_setting.TaskScriptConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskScriptConfigRepository extends JpaRepository<TaskScriptConfig, Long> {
    List<TaskScriptConfig> findAllByTaskIdAndScriptIdIn(Long taskId, List<Long> scriptIds);
}
