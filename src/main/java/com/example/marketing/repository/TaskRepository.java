package com.example.marketing.repository;

import com.example.marketing.model.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("select t from Task t where t.createdBy =:user and t.typeTask =:typeTask")
    Page<Task>findMyTaskByTypeTask(long typeTask, String user, Pageable pageable);

    @Query("select t from Task t where t.assignee =:user and t.typeTask =:typeTask")
    Page<Task>findAssignTaskByTypeTask(long typeTask, String user, Pageable pageable);
}
