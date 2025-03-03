package com.denso.pdabackend.domain.material.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.domain.material.service.MaterialInfoService;
import com.denso.pdabackend.response.ResponseEntityUtil;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("materialInfo/materialInfo")
public class MaterialInfoController {


	private final MaterialInfoService materialInfoService; 
	
	@PostMapping
	@Operation(summary = "자재정보", description = "자재정보")
	public ResponseEntity<?> getMaterial(@RequestBody Map<String,Object> params) throws Exception {

		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> material =  materialInfoService.getMaterial(params);
		data.put("material", material);

		return ResponseEntityUtil.ok(material);
	}

}
