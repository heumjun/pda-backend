package com.denso.pdabackend.domain.stock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto.Request;

@Mapper
public interface ConsignedMaterialsOutputMapper {

	List<Map<String, Object>> getComboCusList(Request request);
	
}
