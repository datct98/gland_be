package com.example.marketing.repository;

import com.example.marketing.model.entities.script_setting.TaskInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskInfoRepository extends JpaRepository<TaskInfo, Long> {
    List<TaskInfo> findAllByTaskId(Long taskId);
    List<TaskInfo> findAllByIdIn(List<Long> ids);
    List<TaskInfo> findAllByTaskIdIn(List<Long> taskIds);
}
