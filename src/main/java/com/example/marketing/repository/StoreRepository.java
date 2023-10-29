package com.example.marketing.repository;

import com.example.marketing.model.entities.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByCreatedBy(String createdBy);
}
