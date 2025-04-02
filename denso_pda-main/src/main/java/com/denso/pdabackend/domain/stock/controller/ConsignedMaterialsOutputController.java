package com.denso.pdabackend.domain.stock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto;
import com.denso.pdabackend.domain.stock.service.ConsignedMaterialsOutputService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.response.StatusCode;
import com.denso.pdabackend.token.dto.UserDto;

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
	
}
