package com.example.marketing.repository;

import com.example.marketing.model.dto.DepartmentScriptDTO;
import com.example.marketing.model.dto.ScriptConnectDTO;
import com.example.marketing.model.entities.Department;
import com.example.marketing.model.entities.Script;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScriptRepository extends JpaRepository<Script, Long> {
    @Query("select s from Script s where (:departmentId is null or s.departmentId =:departmentId) " +
            "AND s.status =:status " +
            "order by s.createdAt desc")
    Page<Script> findAllByDepartmentIdAndStatusOrderByCreatedAtDesc(Long departmentId, boolean status, Pageable pageable);

    //List<Script> findAllByDepartmentIdAndStatusOrderByCreatedAtDesc(long departmentId, boolean status);

    List<Script> findAllByIdIsNotAndDepartmentId(long scriptId, long departmentId);

    @Query("select s from Script s join TaskScriptConfig t on s.id = t.scriptId " +
            "where s.departmentId =:departmentId and t.taskId =:taskId and t.assigned is true " +
            "and s.id is not :scriptId")
    List<Script> findByIdIsNotAndDepartmentId(long scriptId, long departmentId, long taskId);

    @Query("select new com.example.marketing.model.dto.ScriptConnectDTO(s.id, s.name, d.connected) from Script s left join DataConnection d on s.id = d.idTo " +
            "where s.id is not :scriptId")
    List<ScriptConnectDTO> findOtherScripts(long scriptId);

    @Query("select new com.example.marketing.model.dto.ScriptConnectDTO(s.id, s.name) from Script s  " +
            "where s.departmentId =:departmentId and s.id is not :scriptId")
    List<ScriptConnectDTO> findScriptsByDepartmentId(long departmentId, long scriptId);

    List<Script> findAllByDepartmentId(long departmentId);
}
