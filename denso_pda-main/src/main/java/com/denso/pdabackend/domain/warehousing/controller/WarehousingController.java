package com.denso.pdabackend.domain.warehousing.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.material.dto.MaterialDto;
import com.denso.pdabackend.domain.warehousing.dto.WarehousingDto;
import com.denso.pdabackend.domain.warehousing.service.WarehousingService;
import com.denso.pdabackend.response.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("warehousing/warehousing")
public class WarehousingController {
	
	private final AuthenticationFacade auth;
    private final WarehousingService warehousingService;
	
	@GetMapping
    @Operation(summary = "품목입고 조회", description = "품목입고 조회")
    public ResponseEntity<?> getWarehousingList(WarehousingDto.WarehousingRequest params) throws Exception {
		
        Map<String,Object> data = new HashMap<String,Object>();
        List<Map<String,Object>> warehousingList =  warehousingService.getWarehousingList(params);
        data.put("warehousingList", warehousingList);
        
        return ResponseEntityUtil.ok(data);
    }
	
}
