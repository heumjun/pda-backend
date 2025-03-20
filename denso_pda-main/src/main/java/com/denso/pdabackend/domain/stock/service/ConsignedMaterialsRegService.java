package com.denso.pdabackend.domain.stock.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto;
import com.denso.pdabackend.domain.stock.mapper.ConsignedMaterialsRegMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsignedMaterialsRegService {

	private final ConsignedMaterialsRegMapper consignedMaterialsRegMapper;

	public List<Map<String, Object>> consignedMaterialsRegDetailList(ConsignedMaterialsRegDto.Request request) {
		return consignedMaterialsRegMapper.consignedMaterialsRegDetailList(request);
	}
	
	
}
