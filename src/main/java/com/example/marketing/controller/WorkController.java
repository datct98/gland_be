package com.example.marketing.controller;

import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.Work;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.WorkRepository;
import com.example.marketing.service.WorkService;
import com.example.marketing.util.Constant;
import com.example.marketing.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/work")
@Slf4j
public class WorkController {
    @Autowired
    private WorkService workService;
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private JWTUtil jwtUtil;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(description = "Used to insert or update. If id != null, it means update. Else insert")
    @PostMapping
    public ResponseEntity<?> modifyWork (@RequestHeader(name="Authorization") String token,
                                         @RequestBody Work body){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        String responseCode = workService.modifyWork(body, userDTO.getUsername());
        if(!Constant.STATUS_SUCCESS.equals(responseCode)){
            return ResponseEntity.badRequest().body(Constant.MESSAGE_ERR.get(responseCode));
        }
        return ResponseEntity.ok("Tạo thành công");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping
    public ResponseEntity<?> getWorks (@RequestHeader(name="Authorization") String token,
                                       @RequestParam long taskId,
                                       @RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }

        pageNum = pageNum == null ? 0 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        Page<Work> page = workRepository.findAllByTaskIdAndCreatedByOrderByCreatedAtDesc
                (taskId,userDTO.getUsername(), PageRequest.of(pageNum, pageSize));
        return ResponseEntity.ok(new DataResponse<>(page.getContent(), page.getTotalPages()));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("{id}")
    public ResponseEntity<?> getDetailWork (@RequestHeader(name="Authorization") String token,
                                            @PathVariable String id){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }

        Work work = workRepository.findById(id).orElse(null);
        if(work == null){
            ResponseEntity.badRequest().body(new DataResponse<>(400,"Không tìm thấy id: "+id));
        }
        return ResponseEntity.ok(work);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/connected")
    public ResponseEntity<?> getConnectedWorks (@RequestHeader(name="Authorization") String token,
                                       @RequestParam long taskId,
                                       @RequestParam long scriptId,
                                       @RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }

        pageNum = pageNum == null ? 0 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        Page<Work> page = workService.getWorksConnected(PageRequest.of(pageNum, pageSize), taskId, scriptId);
        return ResponseEntity.ok(new DataResponse<>(page.getContent(), page.getTotalPages()));
    }
}
