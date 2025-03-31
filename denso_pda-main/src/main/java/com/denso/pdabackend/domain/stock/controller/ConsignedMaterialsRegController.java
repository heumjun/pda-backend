package com.denso.pdabackend.domain.stock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.packaging.dto.AnomalyDto;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsReqDto;
import com.denso.pdabackend.domain.stock.service.ConsignedMaterialsRegService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.response.StatusCode;
import com.denso.pdabackend.response.exception.BusinessException;
import com.denso.pdabackend.token.dto.UserDto;
import com.denso.pdabackend.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("consignedMaterialsReg/consignedMaterialsReg")
public class ConsignedMaterialsRegController {
	
	private final AuthenticationFacade auth;
    private final ConsignedMaterialsRegService consignedMaterialsRegService;
    
    @GetMapping
    @Operation(summary = "사급출고요청 상세 조회", description = "사급출고요청 상세 조회")
	public ResponseEntity<?> consignedMaterialsRegDetailList(ConsignedMaterialsRegDto.Request request) throws Exception {
    		
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

		List<Map<String,Object>> consignedMaterialsRegDetailList = consignedMaterialsRegService.consignedMaterialsRegDetailList(request);

		data.put("consignedMaterialsRegDetailList", consignedMaterialsRegDetailList);

		return ResponseEntityUtil.ok(data);

	}
    
    @PostMapping
    @Operation(summary = "사급출고요청 등록", description = "사급출고요청 등록")
	public ResponseEntity<?> saveConsignedMaterialsReq(@RequestBody Map<String,Object> params) throws Exception{
    	
    	log.debug("{}", params);
		
		List<ConsignedMaterialsRegDto.Info> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<ConsignedMaterialsRegDto.Info>>() {});
		if( ObjectUtils.isEmpty(insertList) ) throw new BusinessException("저장할 내역이 없습니다.");
		
		UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        
        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 수정할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 수정할 수 없습니다.");
        }

        // 공통정보 세팅 - 등록
 		if(insertList != null) {
 			for (ConsignedMaterialsRegDto.Info info : insertList) {
 				info.setMf16Company(company);
 				info.setMf16Factory(factory);
 				info.setMf16Empno(userInfo.getEmpNo()); // 등록자
 				info.setMf16UpdEmpno(userInfo.getEmpNo()); // 수정자
 			}
 		}
		
		if(!consignedMaterialsRegService.saveConsignedMaterialsReq(insertList)) {
			return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "사급요청 등록에 실패하였습니다.");
		}

		return ResponseEntityUtil.created("사급요청이 등록되었습니다.");

	}
    
}
