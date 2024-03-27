package com.example.marketing.repository;

import com.example.marketing.model.entities.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends MongoRepository<Work, String> {
    Page<Work> findAllByTaskIdAndCreatedByOrderByCreatedAt(long taskId, String createdBy, Pageable pageable);
}
