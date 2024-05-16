package com.example.marketing.service;

import com.example.marketing.model.entities.ConfigSystem;
import com.example.marketing.model.entities.stock.DataStock;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.ConfigSystemRepository;
import com.example.marketing.repository.DataStockRepository;
import com.example.marketing.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataStockService {
    @Autowired
    private DataStockRepository dataStockRepository;
    @Autowired
    private ConfigSystemRepository configSystemRepository;

    public String modify(DataStock body){
        DataStock stock = dataStockRepository.findById(body.getId()).orElse(null);
        if(stock ==null){
            if(StringUtils.isNotEmpty(body.getIdCustom())){
                // Check id đã tồn tại hay chưa trong trường hợp tạo mới
                List<DataStock> dataStocks = dataStockRepository.findByIdCustom(body.getIdCustom());
                if(dataStocks.size()>0){
                    return  "Id đã tồn tại, vui lòng thử lại";
                }
            }
            stock = new DataStock();
        } else {
            // Check trong trường hợp người dùng muốn chỉnh sửa Id
            if(StringUtils.isNotEmpty(body.getIdCustom()) && !body.getIdCustom().equals(stock.getIdCustom())){
                List<DataStock> dataStocks = dataStockRepository.findByIdCustom(body.getIdCustom());
                if(dataStocks.size()>0){
                    return "Id đã tồn tại, vui lòng thử lại";
                }
            }
        }

        if(StringUtils.isEmpty(body.getIdCustom())){
            ConfigSystem configSystem = configSystemRepository.findById(1L).orElse(null);
            if(configSystem == null){
                return "Không tìm thấy cấu hình hệ thống";
            }
            stock.setIdAuto(body.getPreCode()+""+configSystem.getIdStockAuto());
            configSystem.setIdStockAuto(configSystem.getIdStockAuto()+1);
            configSystemRepository.save(configSystem);
        }
        stock.setTypeId(body.getTypeId());
        stock.setDepartmentId(body.getDepartmentId());
        stock.setDepartmentName(body.getDepartmentName());
        stock.setScriptId(body.getScriptId());
        stock.setScriptName(body.getScriptName());
        stock.setIdCustom(body.getIdCustom());
        //stock.setIdAuto(body.getIdAuto());
        stock.setPreCode(body.getPreCode());
        stock.setData(body.getData());
        dataStockRepository.save(stock);
        return Constant.STATUS_SUCCESS;
    }
}
