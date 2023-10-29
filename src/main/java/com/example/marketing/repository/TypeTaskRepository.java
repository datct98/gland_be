package com.example.marketing.repository;

import com.example.marketing.model.entities.TypeTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeTaskRepository extends JpaRepository<TypeTask, Long> {
    Page<TypeTask> findAllByScriptId(long scriptId, Pageable pageable);
    List<TypeTask> findAllByScriptId(long scriptId);

    @Query("select t from TypeTask t JOIN RoleTask r on t.id = r.typeTaskId " +
            "where t.scriptId =:scriptId and r.myJob = true ")
    List<TypeTask> findAllMyJobByScriptId(long scriptId);

    @Query("select t from TypeTask t JOIN RoleTask r on t.id = r.typeTaskId " +
            "where t.scriptId =:scriptId and r.assignedJob = true ")
    List<TypeTask> findAllAssignJobByScriptId(long scriptId);
}
