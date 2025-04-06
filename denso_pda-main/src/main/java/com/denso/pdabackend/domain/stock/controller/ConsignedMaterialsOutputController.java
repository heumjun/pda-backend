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
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsOutputDto;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto;
import com.denso.pdabackend.domain.stock.service.ConsignedMaterialsOutputService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.response.StatusCode;
import com.denso.pdabackend.response.exception.BusinessException;
import com.denso.pdabackend.token.dto.UserDto;
import com.denso.pdabackend.utils.JsonUtils;
import com.denso.pdabackend.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("consignedMaterialsOutput/consignedMaterialsOutput")
public class ConsignedMaterialsOutputController {
	
	private final AuthenticationFacade auth;
	private final ConsignedMaterialsOutputService consignedMaterialsOutputService;
    
	@GetMapping("/getComboCusList")
    @Operation(summary = "제조사 콤보 박스", description = "제조사 콤보 박스")
	public ResponseEntity<?> getComboCusList(ConsignedMaterialsRegDto.Request request) throws Exception {
    		
		Map<String,Object> data = new HashMap<String,Object>();

		UserDto userInfo = auth.getUserInfo();
		String company = userInfo.getCompany();
		String factory = userInfo.getFactory();

		request.setCompany(company);
		request.setFactory(factory);

		if (company == null) {
			return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
		}

		if (factory == null) {
			return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
		}

		List<Map<String,Object>> cmbCusList = consignedMaterialsOutputService.getComboCusList(request);

		data.put("cmbCusList", cmbCusList);

		return ResponseEntityUtil.ok(data);
	}
	
	@PostMapping("getConsignedMaterialsOutput")
	@Operation(summary = "품목 정보", description = "품목 정보")
	public ResponseEntity<?> getConsignedMaterialsOutput(@RequestBody Map<String,Object> params) throws Exception {

		UserDto userInfo = auth.getUserInfo();
		
		String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
		
		if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
        }
		
		params.put("company", company);
		params.put("factory", factory);
		
		Map<String,Object> consignedMaterialsOutput =  consignedMaterialsOutputService.getConsignedMaterialsOutput(params);
		
		return ResponseEntityUtil.ok(consignedMaterialsOutput);
	}
	
	@PostMapping
    @Operation(summary = "사급 등록", description = "사급 등록")
	public ResponseEntity<?> saveConsignedMaterialsOutput(@RequestBody Map<String,Object> params) throws Exception {
    	
    	log.debug("{}", params);
		
		List<ConsignedMaterialsOutputDto.DetailInfo> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<ConsignedMaterialsOutputDto.DetailInfo>>() {});
		if( ObjectUtils.isEmpty(insertList) ) throw new BusinessException("저장할 내역이 없습니다.");
		
		UserDto userInfo = auth.getUserInfo();
		String company = userInfo.getCompany();
		String factory = userInfo.getFactory();
		int empNo = userInfo.getEmpNo();
        
        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 수정할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 수정할 수 없습니다.");
        }
        
        ConsignedMaterialsOutputDto.Request request = new ConsignedMaterialsOutputDto.Request();
        request.setCompany(company);
        request.setFactory(factory);
        
        Map<String, Object> masterMap = consignedMaterialsOutputService.createMf15No(request);
        
        // 사급출고 마스터 정보 세팅
        ConsignedMaterialsOutputDto.MasterInfo masterInfo = new ConsignedMaterialsOutputDto.MasterInfo();
        masterInfo.setMf15Company(company);
        masterInfo.setMf15Factory(factory);
        masterInfo.setMf15EmpNo(empNo);
        masterInfo.setMf15Cus( StringUtils.nullString(params.get("cusCode")) );
        masterInfo.setMf15No( StringUtils.nullString(masterMap.get("mf15No")) );
        masterInfo.setMf15Requestno( StringUtils.nullString(masterMap.get("mf15Requestno")) );
        masterInfo.setMf15Seq( Integer.parseInt(StringUtils.nullString(masterMap.get("mf15Seq"))) );

        // 사급출고 상세 정보 세팅
 		if(insertList != null) {
 			for (ConsignedMaterialsOutputDto.DetailInfo detailInfo : insertList) {
 				detailInfo.setMf16Company(company);
 				detailInfo.setMf16Factory(factory);
 				detailInfo.setMf16EmpNo(empNo); // 등록자
 				detailInfo.setMf16Hno( StringUtils.nullString(masterMap.get("mf15No")) ); // 등록자
 				detailInfo.setMf16Code( detailInfo.getSt02Code() ); // 등록자
 				detailInfo.setSt03Cus( StringUtils.nullString(params.get("cusCode")) ); // 등록자
 			}
 		}

 		if(!consignedMaterialsOutputService.saveConsignedMaterialsOutput(masterInfo, insertList)) {
 			return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "사급 등록에 실패하였습니다.");
 		}

 		return ResponseEntityUtil.created("사급이 등록되었습니다.");

	}
	
}
