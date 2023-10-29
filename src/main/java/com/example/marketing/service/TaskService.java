package com.example.marketing.service;

import com.example.marketing.model.entities.Configuration;
import com.example.marketing.model.entities.Task;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.ConfigRepository;
import com.example.marketing.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ConfigRepository configRepository;

    public DataResponse getTaskList(String username, HttpServletRequest request, long typeTask, int pageNum){
        // Get value for columns
        Page<Configuration> pageColumn = configRepository.findAllByTypeTaskIdAndType(typeTask, 2, PageRequest.of(0, 50));

        // Get values for rows
        String action = request.getHeader("action");
        Page<Task> pageRow;
        if(action.equals("create")){
            pageRow = taskRepository.findMyTaskByTypeTask(typeTask, username, PageRequest.of(pageNum, 10));
        } else
            pageRow = taskRepository.findAssignTaskByTypeTask(typeTask, username, PageRequest.of(pageNum, 10));
        return new DataResponse<>(pageRow.getContent(), pageRow.getTotalPages(), pageColumn.getContent());
    }
}
