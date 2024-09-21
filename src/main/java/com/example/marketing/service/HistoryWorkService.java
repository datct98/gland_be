package com.example.marketing.service;

import com.example.marketing.model.dto.StockDTO;
import com.example.marketing.model.entities.HistoryWork;
import com.example.marketing.model.entities.Work;
import com.example.marketing.repository.HistoryWorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HistoryWorkService {
    @Autowired
    private HistoryWorkRepository historyWorkRepository;

    public Page<HistoryWork> getHistories(String workId, int pageSize, int pageNum){
        return historyWorkRepository.findAllByIdWork(workId, PageRequest.of(pageNum, pageSize));
    }

    public void saveHistory(Work body, String createdBy, String status, List<StockDTO> stockOlds){
        switch (status){
            case "create":
                HistoryWork historyWork = new HistoryWork();
                historyWork.setId(UUID.randomUUID().toString());
                historyWork.setIdWork(body.getId());
                historyWork.setCreatedBy(createdBy);
                historyWork.setStocks(body.getIdStocks());
                historyWorkRepository.save(historyWork);
                break;
            case "update":
                if(body.getIdStocks()!= null && body.getIdStocks().size()>0){
                    if(stockOlds.size()>0){
                        Map<String, StockDTO> stockOldMap = stockOlds.stream()
                                .collect(Collectors.toMap(StockDTO::getIdDataStock, f -> f));
                        List<StockDTO> stockDTOSNew = new ArrayList<>();
                        for (StockDTO stockBody: body.getIdStocks()){
                            // Check nếu như chưa từng lưu trc đó thì lưu mới vào lịch sử
                            if(!stockOldMap.containsKey(stockBody.getIdDataStock())){
                                stockBody.setCreatedBy(createdBy);
                                stockDTOSNew.add(stockBody);
                            }
                        }
                        HistoryWork history = new HistoryWork();
                        history.setId(UUID.randomUUID().toString());
                        history.setIdWork(body.getId());
                        history.setCreatedBy(createdBy);
                        history.setCreatedAt(new Date());
                        history.setStocks(stockDTOSNew);
                        historyWorkRepository.save(history);

                    } else {
                        // Nếu công vc trc đó k lưu stock
                        HistoryWork history = new HistoryWork();
                        history.setId(UUID.randomUUID().toString());
                        history.setIdWork(body.getId());
                        history.setCreatedBy(createdBy);
                        history.setCreatedAt(new Date());
                        history.setStocks(body.getIdStocks());
                        historyWorkRepository.save(history);
                    }

                }
                break;
        }
    }
}
