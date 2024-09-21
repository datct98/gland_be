package com.example.marketing.repository;

import com.example.marketing.model.entities.HistoryWork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoryWorkRepository extends MongoRepository<HistoryWork, String> {

    Page<HistoryWork> findAllByIdWork(String idWork, Pageable pageable);
}
