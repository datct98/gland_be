package com.example.marketing.repository;

import com.example.marketing.model.entities.DataConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataConnectRepository extends JpaRepository<DataConnection, Long> {

    List<DataConnection> findAllByIdFrom(String idFrom);
    List<DataConnection> findAllByIdFromAndIdTo(String idFrom, Long idTo);
    List<DataConnection> findAllByIdToAndConnected(long idTo, boolean connected);
    List<DataConnection> findAllByIdFromInOrIdToIn(List<String> idWorks, List<Long> idScripts);
}
