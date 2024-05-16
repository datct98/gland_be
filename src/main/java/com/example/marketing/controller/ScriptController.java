package com.example.marketing.controller;

import com.example.marketing.model.dto.DepartmentDTO;
import com.example.marketing.model.dto.DepartmentScriptDTO;
import com.example.marketing.model.dto.ScriptConnectDTO;
import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.DataConnection;
import com.example.marketing.model.entities.Script;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.service.DepartmentService;
import com.example.marketing.service.ScriptService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scripts")
@Slf4j
public class ScriptController {
    @Autowired
    private ScriptService scriptService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private DepartmentService departmentService;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(description = "Used to modifyScript. If id == null it means create. Else update ")
    @PostMapping
    public ResponseEntity<?> modifyScript(@RequestHeader(name="Authorization") String token,
                                          @RequestBody Script body){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        String responseCode = scriptService.modifyScript(body, userDTO.getUsername());
        if(responseCode == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DataResponse<>(HttpStatus.BAD_REQUEST.value(), "Thao tác thất bại, vui lòng thử lại!"));
        }
        Page<DepartmentScriptDTO> departments = departmentService.getPageDepartments(userDTO, 0);
        List<DepartmentDTO> departmentDTOS = departmentService.convertDepartmentScriptDTOsToDepartmentDTOs(departments.getContent());
        // Check is admin -> authorization
        if(!userDTO.isAdmin())
            departmentDTOS = departmentDTOS.stream().filter(e-> Objects.equals(e.getDepartmentId(), userDTO.getDepartmentId())).collect(Collectors.toList());
        return ResponseEntity.ok(new DataResponse<>(departmentDTOS, departments.getTotalPages()));

    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @Operation(description = "Get scripts and return page")
    @GetMapping
    public ResponseEntity<?> getScripts(@RequestHeader(name="Authorization") String token,
                                        @RequestParam(required = false) Integer pageNum,
                                        @RequestParam(required = false) Integer pageSize,
                                        @RequestParam Long departmentId){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        Page<Script> scriptPage = scriptService.getScripts(departmentId, PageRequest.of(pageNum, pageSize));
        return ResponseEntity.ok(new DataResponse<>(scriptPage.getContent(), scriptPage.getTotalPages()));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @Operation(description = "Get other scripts")
    @GetMapping("/other")
    public ResponseEntity<?> getScriptsOther(@RequestHeader(name="Authorization") String token,
                                             @RequestParam long scriptId,
                                             @RequestParam long departmentId,
                                             @RequestParam String idWork){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        List<ScriptConnectDTO> scripts = scriptService.getOtherScripts(scriptId, departmentId, idWork);

        return ResponseEntity.ok(new DataResponse<>(scripts));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(description = "Connect data")
    @PostMapping("/other")
    public ResponseEntity<?> connectScriptsOther(@RequestHeader(name="Authorization") String token,
                                                 @RequestBody List<DataConnection> bodies){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        String responseCode = scriptService.connectData(bodies);
        if(Constant.STATUS_SUCCESS.equals(responseCode)){
            return ResponseEntity.ok(new DataResponse<>("Thao tác thành công"));
        }

        return ResponseEntity.badRequest().body(new DataResponse<>(Constant.MESSAGE_ERR.get(responseCode)));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.DELETE)
    @Operation(description = "Delete scripts and return status")
    @DeleteMapping
    public ResponseEntity<?> deleteScript(@RequestHeader(name="Authorization") String token,
                                        @RequestParam long scriptId){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        if(!userDTO.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DataResponse<>(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền thao tác"));
        String responseCode = scriptService.deleteById(scriptId);
        if(!responseCode.equals(Constant.STATUS_SUCCESS))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DataResponse<>(Constant.MESSAGE_ERR.get(responseCode)));
        return ResponseEntity.ok(new DataResponse<>(Constant.MESSAGE_ERR.get(responseCode)));
    }
}
