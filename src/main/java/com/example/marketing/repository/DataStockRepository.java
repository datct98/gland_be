package com.example.marketing.repository;

import com.example.marketing.model.entities.stock.DataStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataStockRepository extends MongoRepository<DataStock, String> {
    Page<DataStock> findAllByTypeId(Long typeId, Pageable pageable);

    Page<DataStock> findAll(Pageable pageable);
    List<DataStock> findByIdCustom(String idCustom);
}
