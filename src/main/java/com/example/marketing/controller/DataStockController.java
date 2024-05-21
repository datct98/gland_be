package com.example.marketing.controller;

import com.example.marketing.model.dto.TypeIdDTO;
import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.ConfigSystem;
import com.example.marketing.model.entities.stock.DataStock;
import com.example.marketing.model.entities.stock.TypeId;
import com.example.marketing.model.entities.stock.TypeIdInfo;
import com.example.marketing.model.response.DataResponse;
import com.example.marketing.repository.DataStockRepository;
import com.example.marketing.repository.TypeIdRepository;
import com.example.marketing.service.DataStockService;
import com.example.marketing.service.TypeIdInfoService;
import com.example.marketing.service.TypeIdService;
import com.example.marketing.util.Constant;
import com.example.marketing.util.JWTUtil;
import com.example.marketing.util.RoleName;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/data-stock")
@Slf4j
public class DataStockController {
    @Autowired
    private DataStockRepository dataStockRepository;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private TypeIdService typeIdService;
    @Autowired
    private TypeIdRepository typeIdRepository;
    @Autowired
    private TypeIdInfoService typeIdInfoService;
    @Autowired
    private DataStockService dataStockService;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping
    public ResponseEntity<?> getDataStock (@RequestHeader(name="Authorization") String token,
                                       @RequestParam(required = false) Long typeId,
                                       @RequestParam(required = false)  Integer pageNum,
                                       @RequestParam(required = false)  Integer pageSize){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }

        pageNum = pageNum == null ? 0 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        Page<DataStock> page;
        if(typeId == null)
            page = dataStockRepository.findAll(PageRequest.of(pageNum, pageSize));
        else page = dataStockRepository.findAllByTypeId(typeId,PageRequest.of(pageNum, pageSize));
        return ResponseEntity.ok(new DataResponse<>(page.getContent(), page.getTotalPages()));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping
    public ResponseEntity<?> saveDataStock (@RequestHeader(name="Authorization") String token,
                                           @RequestBody DataStock body){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }

        String response = dataStockService.modify(body, userDTO.getUsername());
        if(Constant.STATUS_SUCCESS.equals(response))
            return ResponseEntity.ok("Thao tác thành công");
        return ResponseEntity.badRequest().body(response);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailDataStock (@RequestHeader(name="Authorization") String token,
                                              @PathVariable String id){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }

        DataStock stock = dataStockRepository.findByIdCustomIsOrIdAutoIs(id, id);
        if(stock == null){
            return ResponseEntity.badRequest().body("Không tìm thấy Data stock id:"+id) ;
        }

        return ResponseEntity.ok().body(stock);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDataStock (@RequestHeader(name="Authorization") String token,
                                            @PathVariable String id){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }

        String response = dataStockService.deleteDtStock(id, userDTO);
        if(Constant.STATUS_SUCCESS.equals(response))
            return ResponseEntity.ok("Thao tác thành công");
        return ResponseEntity.badRequest().body(response);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/type-id")
    public ResponseEntity<?> modifyTypeId (@RequestHeader(name="Authorization") String token,
                                           @RequestBody TypeId body){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        String result = typeIdService.modify(body);
        if(Constant.STATUS_SUCCESS.equals(result)){
            return ResponseEntity.ok("Thao tác thành công");
        }
        return ResponseEntity.badRequest().body(result);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/type-id")
    public ResponseEntity<?> getTypeIds (@RequestHeader(name="Authorization") String token,
                                         @RequestParam(required = false) Long departmentId,
                                         @RequestParam(required = false)  Integer pageNum,
                                         @RequestParam(required = false)  Integer pageSize){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        pageNum = pageNum == null ? 0 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;

        Page<TypeId> page = typeIdRepository.findAllByDepartmentId(departmentId, PageRequest.of(pageNum, pageSize));
        return ResponseEntity.ok().body(page.getContent());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.DELETE)
    @DeleteMapping("/type-id/{id}")
    public ResponseEntity<?> deleteTypeId (@RequestHeader(name="Authorization") String token,
                                         @PathVariable long id){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        if(!userDTO.isAdmin() && !"leader".equalsIgnoreCase(userDTO.getRole()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DataResponse<>(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền thao tác"));
        String response = typeIdService.delete(id);
        if(!response.equals(Constant.STATUS_SUCCESS))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DataResponse<>(response));
        return ResponseEntity.ok(new DataResponse<>(Constant.MESSAGE_ERR.get(response)));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/type-id/info")
    public ResponseEntity<?> modifyTypeIdInfo (@RequestHeader(name="Authorization") String token,
                                           @RequestBody TypeIdDTO body){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        String result = typeIdInfoService.modify(body);
        if(Constant.STATUS_SUCCESS.equals(result)){
            return ResponseEntity.ok("Thao tác thành công");
        }
        return ResponseEntity.badRequest().body(result);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.DELETE)
    @DeleteMapping("/type-id/info")
    public ResponseEntity<?> deleteTypeIdInfo (@RequestHeader(name="Authorization") String token,
                                         @RequestParam long id){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null || (!userDTO.isAdmin() && !RoleName.LEADER.name().equalsIgnoreCase(userDTO.getRole()))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
        String result = typeIdInfoService.deleteTypeIdInfo(id);
        if(Constant.STATUS_SUCCESS.equals(result)){
            return ResponseEntity.ok("Thao tác thành công");
        }
        return ResponseEntity.badRequest().body(result);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/type-id/info")
    public ResponseEntity<?> getDetailTypeIdInfo (@RequestHeader(name="Authorization") String token,
                                               @RequestParam long typeId){
        UserDTO userDTO = jwtUtil.validateTokenAndGetUsername(token);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED.value(), "Xác thực thất bại, vui lòng đăng nhập lại!"));
        }
       TypeIdDTO dto = typeIdInfoService.getDetail(typeId);
        if(dto == null){
            return ResponseEntity.badRequest().body(dto);
        }
        return ResponseEntity.ok(dto);
    }
}
