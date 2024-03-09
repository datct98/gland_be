package com.example.marketing.repository;

import com.example.marketing.model.dto.DepartmentScriptDTO;
import com.example.marketing.model.entities.Department;
import com.example.marketing.model.entities.Script;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScriptRepository extends JpaRepository<Script, Long> {
    Page<Script> findAllByDepartmentIdAndStatusOrderByCreatedAtDesc(long departmentId, boolean status, Pageable pageable);
}
