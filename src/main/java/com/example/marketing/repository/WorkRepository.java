package com.example.marketing.repository;

import com.example.marketing.model.entities.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends MongoRepository<Work, String> {
    Page<Work> findAllByTaskIdAndCreatedByAndStatusInOrderByCreatedAtDesc(long taskId, String createdBy, List<String> status, Pageable pageable);
    Page<Work> findAllByTaskIdAndIdInAndStatusNotIn(long taskId, List<String> ids, List<String> status, Pageable pageable);
    List<Work> findAllByIdWork(String idWork);
    List<Work> findAllByTaskId(long taskId);
    List<Work> findAllByTaskIdIn(List<Long> taskIds);
    Work findByIdWork(String idWork);
    Work findByIdCustomIsOrIdAutoIs(String idCustom, String idAuto);
}
