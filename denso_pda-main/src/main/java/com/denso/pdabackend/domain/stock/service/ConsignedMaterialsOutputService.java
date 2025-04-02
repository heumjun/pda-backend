package com.denso.pdabackend.domain.stock.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto.Request;
import com.denso.pdabackend.domain.stock.mapper.ConsignedMaterialsOutputMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsignedMaterialsOutputService {

	private final ConsignedMaterialsOutputMapper consignedMaterialsOutputMapper;

	public List<Map<String, Object>> getComboCusList(Request request) {
		return consignedMaterialsOutputMapper.getComboCusList(request);
	}
	
}
