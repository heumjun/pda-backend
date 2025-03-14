package com.denso.pdabackend.domain.partsInput.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.partsInput.service.PartsInputService;
import com.denso.pdabackend.response.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("partsInput/partsInput")
public class PartsInputController {

	private final AuthenticationFacade auth;
    private final PartsInputService partsInputService;
    
    @PostMapping
	@Operation(summary = "출고이력 등록", description = "출고이력 등록")
	public ResponseEntity<?> saveOfOutput(@RequestBody Map<String,Object> params) throws Exception {

		log.debug("{}", params);
		
		return ResponseEntityUtil.created("출고이력이 등록되었습니다.");
	}
    
}
