package com.denso.pdabackend.domain.stock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsReqDto;
import com.denso.pdabackend.domain.stock.service.ConsignedMaterialsReqService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.response.StatusCode;
import com.denso.pdabackend.token.dto.UserDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("consignedMaterialsReq/consignedMaterialsReq")
public class ConsignedMaterialsReqController {
	
	private final AuthenticationFacade auth;
    private final ConsignedMaterialsReqService consignedMaterialsReqService;
    
    /**
     * 사급출고요청서 조회
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping
    @Operation(summary = "사급출고요청서 목록 조회", description = "사급출고요청서 목록 조회")
    public ResponseEntity<?> getConsignedMaterialsReqSearch(ConsignedMaterialsReqDto.Request request) throws Exception {

    	Map<String,Object> data = new HashMap<String,Object>();

        UserDto userInfo = auth.getUserInfo();
        
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        
        request.setMf15Company(company);
        request.setMf15Factory(factory);

        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
        }

        // 사급출고요청서 목록 조회
        List<Map<String, Object>> consignedMaterialsReqList =  consignedMaterialsReqService.getConsignedMaterialsReqSearch(request);

        if(consignedMaterialsReqList.size() < 1) {
            return ResponseEntityUtil.error(StatusCode.NOT_FOUND,"사급출고요청서가 존재하지 않습니다.");
        }

        data.put("consignedMaterialsReqList", consignedMaterialsReqList);
        
        return ResponseEntityUtil.ok(data);
    }
    
}
