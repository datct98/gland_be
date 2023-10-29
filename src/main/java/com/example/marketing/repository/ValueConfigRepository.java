package com.example.marketing.repository;

import com.example.marketing.model.entities.ValueConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValueConfigRepository extends JpaRepository<ValueConfiguration, Long> {
}
