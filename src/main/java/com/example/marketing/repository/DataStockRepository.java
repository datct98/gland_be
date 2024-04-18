package com.example.marketing.repository;

import com.example.marketing.model.entities.DataStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface DataStockRepository extends MongoRepository<DataStock, String> {
    @Query("{ $and: [ :?0 ?0, {} ] }")
    Page<DataStock> findAllByPreCode(@RequestParam(name = "pre_code") String preCode, Pageable pageable);
}
