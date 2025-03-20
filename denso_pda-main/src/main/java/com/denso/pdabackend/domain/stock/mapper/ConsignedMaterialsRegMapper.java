package com.denso.pdabackend.domain.stock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto;

@Mapper
public interface ConsignedMaterialsRegMapper {

	List<Map<String, Object>> consignedMaterialsRegDetailList(ConsignedMaterialsRegDto.Request request);
	
}
