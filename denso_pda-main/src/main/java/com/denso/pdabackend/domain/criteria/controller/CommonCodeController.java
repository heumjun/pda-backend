package com.denso.pdabackend.domain.criteria.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.criteria.dto.CommonCodeDto;
import com.denso.pdabackend.domain.criteria.dto.CommonCodeDto.Request;
import com.denso.pdabackend.domain.criteria.service.CommonCodeService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.token.dto.UserDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("criteria/commonCode")
public class CommonCodeController {
	
	private final CommonCodeService commonCodeService;
	private final AuthenticationFacade auth;  //토큰인증된 사용자정보관리
	
	@Operation(summary = "공통코드 필드값 리스트(사용중인코드만)",description = "프로그램 조회조건에 사용될경우")
	@GetMapping("{code}/detail")
	public ResponseEntity<?> getCommonCodeDetailList(CommonCodeDto.Request params) throws Exception{

		params.setLock("N");	//사용중인것만
		return commonCodeDetailList(params);
		
	}
	
	/**
	 * 코드 속성리스트
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private ResponseEntity<?> commonCodeDetailList(Request params) throws Exception {
		
		//토큰인증 사용자 정보
		UserDto userInfo = auth.getUserInfo();
		params.setCompany(userInfo.getCompany());
		params.setFactory(userInfo.getFactory());
		
		Map<String,Object> data = new HashMap<String,Object>();
		List<Map<String,Object>> commonCodeDetailList = commonCodeService.getCommonCodeDetailList(params);
		
		data.put("commonCodeDetailList", commonCodeDetailList);
		
		return ResponseEntityUtil.ok(data);
		
	}

}
