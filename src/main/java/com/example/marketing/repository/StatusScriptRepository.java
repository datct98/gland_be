package com.example.marketing.repository;

import com.example.marketing.model.entities.StatusTypeTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusScriptRepository extends JpaRepository<StatusTypeTask, Long> {
}
