package com.denso.pdabackend.domain.material.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.material.service.MaterialInfoService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.response.StatusCode;
import com.denso.pdabackend.token.dto.UserDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("materialInfo/materialInfo")
public class MaterialInfoController {

	private final AuthenticationFacade auth;
	
	private final MaterialInfoService materialInfoService; 
	
	@PostMapping
	@Operation(summary = "자재정보", description = "자재정보")
	public ResponseEntity<?> getMaterial(@RequestBody Map<String,Object> params) throws Exception {

		UserDto userInfo = auth.getUserInfo();
		
		String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
		
		if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
        }
		
		Map<String,Object> data = new HashMap<String,Object>();
		
		params.put("company", company);
		params.put("factory", factory);
		
		Map<String,Object> material =  materialInfoService.getMaterial(params);
		data.put("material", material);

		return ResponseEntityUtil.ok(material);
	}

}
