package com.example.marketing.repository;

import com.example.marketing.model.entities.script_setting.TaskInfo;
import com.example.marketing.model.entities.script_setting.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    List<TaskStatus> findAllByTaskId(long taskId);
    List<TaskStatus> findAllByTaskIdIn(List<Long> taskIds);
}
