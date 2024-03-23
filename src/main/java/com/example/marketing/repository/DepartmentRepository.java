package com.example.marketing.repository;

import com.example.marketing.model.dto.DepartmentScriptDTO;
import com.example.marketing.model.entities.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Page<Department> findAllByStatus(boolean status, Pageable pageable);

    List<Department> findAllByStatus(boolean status);

    @Query("select new com.example.marketing.model.dto.DepartmentScriptDTO" +
            "(d.id, d.name, s.id, s.name, d.note, s.note, d.createdAt, s.createdAt) " +
            "from Department d left join Script s on d.id = s.departmentId " +
            "order by d.createdAt desc ")
    Page<DepartmentScriptDTO> findAllByCreatedBy(String username, boolean admin, Pageable pageable);

    @Query("select new com.example.marketing.model.dto.DepartmentScriptDTO" +
            "(d.id, d.name, s.id, s.name, d.note, s.note, d.createdAt, s.createdAt) " +
            "from Department d left join Script s on d.id = s.departmentId " +
            "order by d.createdAt desc ")
    List<DepartmentScriptDTO> findAllByCreatedBy(String username, boolean admin);
}
