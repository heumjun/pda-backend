package com.denso.pdabackend.domain.codeManage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.codeManage.dto.TrainerDto;
import com.denso.pdabackend.domain.codeManage.service.TrainerService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("code-manage/trainers")
public class TrainerController {

    private final AuthenticationFacade auth;
    private final TrainerService trainerService;

//    @GetMapping
//    @Operation(summary = "트레이너 리스트", description = "트레이너 리스트")
//    public ResponseEntity<?> getTrainerList(TrainerDto.TrainerRequest params) throws Exception {
//        log.debug("{}",params);
//        Map<String,Object> data = new HashMap<String,Object>();
//
//        String jan = auth.getUserInfo().getJan();
//        params.setJan(jan);
//    
//        List<Map<String,Object>> trainerList =  trainerService.getTrainerList(params);
//
//        data.put("trainerList",trainerList);
//
//        return ResponseEntityUtil.ok(data);
//    }
//
//    @GetMapping("{id}")
//    @Operation(summary = "트레이너 정보", description = "트레이너 정보")
//    public ResponseEntity<?> getTrainer(@PathVariable String id) throws Exception {
//        
//        Map<String,Object> data = new HashMap<String,Object>();
//
//        String jan = auth.getUserInfo().getJan();
//        TrainerDto.TrainerRequest params = TrainerDto.TrainerRequest.builder()
//                                            .id(id)
//                                            .jan(jan)
//                                            .build();
//
//        Map<String,Object> trainer =  trainerService.getTrainer(params);
//
//        data.put("trainer",trainer);
//
//        return ResponseEntityUtil.ok(data);
//    }
//    
//
//    @PostMapping
//    @Operation(summary = "트레이너 저장", description = "트레이너 저장")
//    public ResponseEntity<?> saveOfTrainer(@RequestBody Map<String,Object> params) throws Exception {
//
//        log.debug("{}",params);
//        
//        List<TrainerDto.TrainerInfo> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<TrainerDto.TrainerInfo>>() {});
//        List<TrainerDto.TrainerInfo> updateList = JsonUtils.deserialize(params.get("updateList"), new TypeReference<List<TrainerDto.TrainerInfo>>() {});
//
//        String jan = auth.getUserInfo().getJan();
//        String id = auth.getUserInfo().getUserId();
//
//        //지점, 등록자 지정
//        insertList.forEach(item->{
//            item.setTraJan(jan);
//            item.setTraRegid(id);
//        });
//        updateList.forEach(item->item.setTraRegid(id));
//    
//        trainerService.saveOfTrainer(insertList,updateList);
//
//        return ResponseEntityUtil.created(null);
//    }
//
//    @DeleteMapping
//    @Operation(summary = "트레이너 삭제", description = "트레이너 삭제")
//    public ResponseEntity<?> deleteOfTrainer(@RequestBody Map<String,Object> params) throws Exception {
//
//        log.debug("{}",params);
//        
//        List<TrainerDto.TrainerInfo> deleteList = JsonUtils.deserialize(params.get("deleteList"), new TypeReference<List<TrainerDto.TrainerInfo>>() {});
//        
//        String jan = auth.getUserInfo().getJan();
//        
//        //지점, 등록자 지정
//        deleteList.forEach(item->item.setTraJan(jan));
//            
//        trainerService.deleteOfTrainer(deleteList);
//
//        return ResponseEntityUtil.created(null);
//    }
}
