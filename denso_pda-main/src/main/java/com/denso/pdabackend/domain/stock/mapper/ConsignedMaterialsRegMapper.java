package com.denso.pdabackend.domain.stock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto.Info;

@Mapper
public interface ConsignedMaterialsRegMapper {

	List<Map<String, Object>> consignedMaterialsRegDetailList(ConsignedMaterialsRegDto.Request request);

	boolean saveConsignedMaterialsReqDetail(Info item) throws Exception;
	
}
