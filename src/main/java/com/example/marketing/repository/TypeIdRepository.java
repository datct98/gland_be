package com.example.marketing.repository;

import com.example.marketing.model.entities.stock.TypeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeIdRepository extends JpaRepository<TypeId, Long> {
    @Query("select t from TypeId t where (:departmentId is null or t.departmentId =:departmentId)")
    Page<TypeId> findAllByDepartmentId(Long departmentId, Pageable pageable);

    List<TypeId> findAllByPreCode(String preCode);
}
