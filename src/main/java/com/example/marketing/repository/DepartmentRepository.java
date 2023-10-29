package com.example.marketing.repository;

import com.example.marketing.model.dto.DepartmentScriptDTO;
import com.example.marketing.model.entities.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByStoreId(long storeId);

    Page<Department> findAllByStoreId(long storeId, Pageable pageable);


}
