package com.example.marketing.repository;

import com.example.marketing.model.entities.script_setting.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAllByScriptId(long scriptId, Pageable pageable);
    List<Task> findAllByScriptId(long scriptId);
    List<Task> findAllByScriptIdIsNot(long scriptId);
    List<Task> findAllByPreCode(String preCode);
}
