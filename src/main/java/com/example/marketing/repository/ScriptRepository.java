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
    Page<Script> findAllByDepartmentIdAndStatusOrderByCreatedAtDesc(long departmentId, boolean status, Pageable pageable);

    List<Script> findAllByIdIsNotAndDepartmentId(long scriptId, long departmentId);

    @Query("select new com.example.marketing.model.dto.ScriptConnectDTO(s.id, s.name, d.connected) from Script s left join DataConnection d on s.id = d.idTo " +
            "where s.id is not :scriptId")
    List<ScriptConnectDTO> findOtherScripts(long scriptId);
}
