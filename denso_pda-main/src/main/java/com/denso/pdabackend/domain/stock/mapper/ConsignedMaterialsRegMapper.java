package com.denso.pdabackend.domain.stock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto.Info;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto.Lot;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto.Request;

@Mapper
public interface ConsignedMaterialsRegMapper {

	List<Map<String, Object>> consignedMaterialsRegDetailList(Request request);
	
	boolean saveConsignedMaterialsReqDetail(Info item) throws Exception;

	List<Map<String, Object>> searchConsignedMaterialsReqInfo(ConsignedMaterialsRegDto.Request request) throws Exception;

	List<Map<String, Object>> getProdStockList(Lot lotParams);

	boolean saveOfProductionOutput(Info item);
	
}
