package com.example.marketing.service;

import com.example.marketing.model.entities.DataConnection;
import com.example.marketing.repository.DataConnectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DataConnectService {
    @Autowired
    private DataConnectRepository dataConnectRepository;

    public void deleteData(List<String>idWorks, List<Long>idScripts){
        try {
            List<DataConnection> dataConnections = dataConnectRepository.findAllByIdFromInOrIdToIn(idWorks, idScripts);
            log.info("#DataConnectService - deleteData - dataConnections size: {}", dataConnections.size());
            if(dataConnections.size()>0){
                dataConnectRepository.deleteAll(dataConnections);
            }
        } catch (Exception e){
            log.error("#DataConnectService - deleteData fail: "+e.getMessage());
        }
    }

    public void unConnected(String idFrom, long idTo){
        List<DataConnection> connections = dataConnectRepository.findAllByIdFromAndIdTo(idFrom, idTo);
        if(connections.size() == 0){
            log.error("Không tìm thấy công việc: "+idFrom+" - và kịch bản: "+idTo);
        } else {
            connections.forEach(e ->{
                e.setConnected(false);
            });
        }
        dataConnectRepository.saveAll(connections);
    }

    public void createNewConnect(String idFrom, long idToNew){
        List<DataConnection> connections = dataConnectRepository.findAllByIdFrom(idFrom);
        boolean checkIdToNewExisted = false;
        for (DataConnection connection: connections){
            if(connection.getIdTo() == idToNew){
                checkIdToNewExisted = true;
                connection.setConnected(true);
            } else{
                connection.setConnected(false);
            }
        }
        if (!checkIdToNewExisted){
            DataConnection connection = new DataConnection();
            connection.setConnected(true);
            connection.setIdFrom(idFrom);
            connection.setIdTo(idToNew);
            connections.add(connection);
        }
        dataConnectRepository.saveAll(connections);
    }
}
