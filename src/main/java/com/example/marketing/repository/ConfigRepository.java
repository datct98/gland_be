package com.example.marketing.repository;

import com.example.marketing.model.entities.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConfigRepository extends JpaRepository<Configuration, Long> {
    Page<Configuration> findAllByTypeTaskIdAndType(long typeId, int type, Pageable pageable);
    /*@Query("select c from Configuration c where c.departmentKey like :department " +
            "and (:type is null or c.type =:type)")
    Page<Configuration> findAllByDepartmentKeyAndType(String department, Integer type, Pageable pageable);
*/}
