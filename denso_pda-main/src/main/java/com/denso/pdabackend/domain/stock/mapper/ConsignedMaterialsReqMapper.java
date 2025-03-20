package com.denso.pdabackend.domain.stock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsReqDto.Request;

@Mapper
public interface ConsignedMaterialsReqMapper {

	/*
	 * 사급출고요청서 목록 조회
	 */
	List<Map<String, Object>> getConsignedMaterialsReqSearch(Request request);
	
}
