package com.denso.pdabackend.domain.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.system.dto.AuthorityDto;
import com.denso.pdabackend.domain.system.service.AuthorityService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("system/authorities")
public class AuthorityController {

    private final AuthenticationFacade auth;
    private final AuthorityService authorityService;

//    @GetMapping
//    @Operation(summary = "메뉴별 권한리스트", description = "url별 권한리스트")
//    public ResponseEntity<?> getAuthority(AuthorityDto.AuthorityRequest params) throws Exception{
//
//        Map<String,Object> data = new HashMap<String,Object>();
//
//        String jan = auth.getUserInfo().getJan();
//        params.setJan(jan);
//        
//        List<Map<String,Object>> authority =  authorityService.getAuthority(params);
//        data.put("authority", authority);
//        
//        return ResponseEntityUtil.ok(data);
//
//    }
//    @GetMapping("{id}")
//    @Operation(summary = "사용자 메뉴 권한리스트", description = "url에 사용자 권한 반환")
//    public ResponseEntity<?> getAuthorityInfo(@PathVariable String id,AuthorityDto.AuthorityRequest params) throws Exception{
//
//        Map<String,Object> data = new HashMap<String,Object>();
//
//        String jan = auth.getUserInfo().getJan();
//        params.setJan(jan);
//        params.setId(id);
//        
//        Map<String,Object> authority =  authorityService.getAuthorityInfo(params);
//        data.put("authority", authority);
//        
//        return ResponseEntityUtil.ok(data);
//
//    }
//
//    @PostMapping
//    @Operation(summary = "권한저장", description = "권한저장 및 수정")
//    public ResponseEntity<?> saveOfAuthority(@RequestBody Map<String,Object> params) throws Exception{
//        log.debug("{}", params);
//
//        String jan = auth.getUserInfo().getJan();
//
//        List<AuthorityDto.Authority> saveList = JsonUtils.deserialize(params.get("saveList"), new TypeReference<List<AuthorityDto.Authority>>() {});
//        saveList.forEach(item->item.setAthJan(jan));
//
//        authorityService.saveOfAuthority(saveList);
//        
//        return ResponseEntityUtil.created(null);
//    }
//
//    @DeleteMapping
//    @Operation(summary = "권한내역 삭제", description = "권한내역 삭제")
//    public ResponseEntity<?> deleteOfAuthority(@RequestBody Map<String,Object> params) throws Exception{
//
//        log.debug("{}", params);
//
//        String jan = auth.getUserInfo().getJan();
//        List<AuthorityDto.Authority> deleteList = JsonUtils.deserialize(params.get("deleteList"), new TypeReference<List<AuthorityDto.Authority>>() {});
//        deleteList.forEach(item->item.setAthJan(jan));
//
//        authorityService.deleteOfAuthority(deleteList);
//        
//        return ResponseEntityUtil.created(null);
//    }
//
//    @PostMapping("copies")
//    @Operation(summary = "권한복사", description = "복사 대상의 권한을 적용대상에게 권한을 복사한다.")
//    public ResponseEntity<?> copyOfAuthority(@RequestBody AuthorityDto.AuthorityCopyRequest params) throws Exception {
//        log.debug("{}", params);
//        
//        String jan = auth.getUserInfo().getJan();
//        params.setJan(jan);
//        
//        authorityService.copyOfAuthority(params);
//        
//        return ResponseEntityUtil.created(null);
//    }
    
}
