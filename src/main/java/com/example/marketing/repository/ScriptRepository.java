package com.example.marketing.repository;

import com.example.marketing.model.entities.Script;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ScriptRepository extends JpaRepository<Script, Long> {
    @Query("select new Script(s.id, s.name, d.name) from Script s JOIN Department d " +
            "on s.departmentId = d.id " +
            "where (:scriptId is null or s.id =:scriptId) ")
    List<Script>findAllByNameAndDepartment(Long scriptId);

    Page<Script> findAllByDepartmentId(long departmentId, Pageable pageable);

    List<Script> findAllByDepartmentIdIn(List<Long> departmentIds);

    @Query("select new Script(s.id, s.name, s.departmentId ,d.name) from Script s JOIN Department d " +
            "on s.departmentId = d.id " +
            "where (:storeId is null or d.storeId =:storeId) ")
    List<Script> findAllByStoreId(long storeId);

    @Query("select new Script(s.id, s.name, s.departmentId ,d.name, r) from Script s JOIN Department d " +
            "on s.departmentId = d.id " +
            "left join RoleTask r on r.scriptId = s.id " +
            "where (:storeId is null or d.storeId =:storeId) and (r.typeTaskId is null or r.typeTaskId =:typeTaskId) ")
    List<Script> findAllByStoreIdAndTypeTask(long storeId, long typeTaskId);
}
