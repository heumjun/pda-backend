package com.denso.pdabackend.domain.pda.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.pda.service.ConsignedMaterialsRelSearchService;
import com.denso.pdabackend.domain.pda.service.ConsignedMaterialsRelService;
import com.denso.pdabackend.response.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("pda/consigned-materials-rel")
public class ConsignedMaterialsRelController {

    private final AuthenticationFacade auth;
    private final ConsignedMaterialsRelService cmrService;

    @PostMapping
    @Operation(summary = "사급출고상세 조회", description = "사급출고상세 조회")
    public ResponseEntity<?> getCmrList(@RequestBody Map<String,Object> params) throws Exception {
    	log.debug("{}", params);
        Map<String,Object> data = new HashMap<String,Object>();
    
        Map<String,Object> cmrInfo =  cmrService.getCmrInfo(params);
        List<Map<String,Object>> cmrList =  cmrService.getCmrList(params);

        data.put("list",cmrList);
        data.put("info",cmrInfo);

        return ResponseEntityUtil.ok(data);
    }
    
    @PostMapping("qrcode")
    @Operation(summary = "qrCode 값 불러오기", description = "QR CODE에서 읽어온 정보를 유효성 확인 후 정보를 리턴한다.")
    public ResponseEntity<?> getQrCodeInfo(@RequestBody Map<String,Object> params) throws Exception{
        Map<String,Object> data = new HashMap<String,Object>();

        Map<String,Object> qrInfo =  cmrService.getQrCodeInfo(params);
        data.put("result", qrInfo);
        return ResponseEntityUtil.ok(data);
    }
}
