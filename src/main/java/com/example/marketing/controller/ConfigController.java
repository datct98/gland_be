package com.example.marketing.controller;


import com.example.marketing.model.entities.Configuration;
import com.example.marketing.model.entities.User;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.ConfigRepository;
import com.example.marketing.repository.UserRepository;
import com.example.marketing.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config-api")
public class ConfigController {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfigRepository configRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/create")
    public ResponseEntity<?> createConfigForTypeTask(
                                              @RequestHeader(name="Authorization") String token,
                                              @RequestBody Configuration configuration) throws Exception {
        if(jwtUtil.validateToken(token.replace("Bearer ",""))) {
            try {
                long userId = jwtUtil.getUserByIdfromJWT(token.replace("Bearer ", ""));
                User user = userRepository.findUserById(userId);
                configuration.setCreatedBy(user.getUsername());
                configRepository.save(configuration);
                Page<Configuration> page = configRepository.findAllByTypeTaskIdAndType(configuration.getTypeTaskId(), configuration.getType(), PageRequest.of(0, 10));
                return ResponseEntity.ok(new DataResponse<>(page.getContent(), page.getTotalPages()));
            } catch (Exception e) {
                throw e;
            }
        }
        throw new Exception("Un-authentication");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/all")
    public ResponseEntity<?> getConfigForTypeTask(
            @RequestHeader(name="Authorization") String token,
            @RequestParam int type,
            @RequestParam(required = false) long typeTask,
            @RequestParam(required = false) int pageNum) throws Exception {
        if(jwtUtil.validateToken(token.replace("Bearer ",""))) {
            try {
                Page<Configuration> page = configRepository.findAllByTypeTaskIdAndType(typeTask, type, PageRequest.of(pageNum, 10));
                return ResponseEntity.ok(new DataResponse<>(page.getContent(), page.getTotalPages()));
            } catch (Exception e) {
                throw e;
            }
        }
        throw new Exception("Un-authentication");
    }
}
