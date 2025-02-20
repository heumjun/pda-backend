package com.denso.pdabackend.domain.pda.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.pda.service.ConsignedMaterialsRelSearchService;
import com.denso.pdabackend.response.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("pda/consigned-materials-rel-search")
public class ConsignedMaterialsRelSearchController {

    private final AuthenticationFacade auth;
    private final ConsignedMaterialsRelSearchService cmrsService;

    @GetMapping
    @Operation(summary = "사급출고조회 리스트", description = "사급출고조회 리스트")
    public ResponseEntity<?> getCmrsList() throws Exception {
        Map<String,Object> data = new HashMap<String,Object>();
    
        List<Map<String,Object>> cmrsList =  cmrsService.getCmrsList();

        data.put("list",cmrsList);

        return ResponseEntityUtil.ok(data);
    }
}
