package com.denso.pdabackend.domain.stock.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsReqDto.Request;
import com.denso.pdabackend.domain.stock.mapper.ConsignedMaterialsReqMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsignedMaterialsReqService {

	private final ConsignedMaterialsReqMapper consignedMaterialsReqMapper;
	
	/*
	 * 사급출고요청서 목록 조회
	 */
	public List<Map<String, Object>> getConsignedMaterialsReqSearch(Request request) {
		return consignedMaterialsReqMapper.getConsignedMaterialsReqSearch(request);
	}
	
}
