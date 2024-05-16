package com.example.marketing.repository;

import com.example.marketing.model.entities.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends MongoRepository<Work, String> {
    Page<Work> findAllByTaskIdAndCreatedByOrderByCreatedAtDesc(long taskId, String createdBy, Pageable pageable);
    Page<Work> findAllByTaskIdAndIdIn(long taskId, List<String> ids, Pageable pageable);
    List<Work> findAllByIdWork(String idWork);

    Work findByIdWork(String idWork);
}
