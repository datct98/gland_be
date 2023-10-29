package com.example.marketing.controller;
import com.example.marketing.model.entities.ValueConfiguration;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.ValueConfigRepository;
import com.example.marketing.service.TaskService;
import com.example.marketing.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ValueConfigRepository valueConfigRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/all")
    public ResponseEntity<?>getTasks(HttpServletRequest request,
                                    @RequestParam(required = false) int pageNum,
                                    @RequestParam long typeTask) throws Exception {
        String token = request.getHeader("Authorization");
        if(jwtUtil.validateToken(token.replace("Bearer ",""))) {
            String username = jwtUtil.getUsernameFromJwt(token.replace("Bearer ", ""));

            return ResponseEntity.ok(taskService.getTaskList(username, request, typeTask, pageNum));
        }
        throw new Exception("Un-authentication");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/create")
    public ResponseEntity<?>createTask(HttpServletRequest request,
                                       @RequestBody List<ValueConfiguration> body) throws Exception {
        String token = request.getHeader("Authorization");
        if(jwtUtil.validateToken(token.replace("Bearer ",""))) {
            String username = jwtUtil.getUsernameFromJwt(token.replace("Bearer ", ""));
            body.forEach(e-> {
                e.setCreatedAt(new Date());
                e.setCreatedBy(username);
            });
            valueConfigRepository.saveAll(body);
            return ResponseEntity.ok(new DataResponse<>());
        }
        throw new Exception("Un-authentication");
    }

}
