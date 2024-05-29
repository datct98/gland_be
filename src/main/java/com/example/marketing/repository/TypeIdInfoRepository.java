package com.example.marketing.repository;

import com.example.marketing.model.entities.stock.TypeIdInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeIdInfoRepository extends JpaRepository<TypeIdInfo, Long> {
    List<TypeIdInfo> findAllByTypeId(long typeId);

    List<TypeIdInfo> findAllByTypeIdIn(List<Long> typeIds);
}
