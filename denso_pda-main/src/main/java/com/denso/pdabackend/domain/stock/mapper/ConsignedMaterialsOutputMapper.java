package com.denso.pdabackend.domain.stock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsOutputDto;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsOutputDto.DetailInfo;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsOutputDto.MasterInfo;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto.Lot;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto.Request;

@Mapper
public interface ConsignedMaterialsOutputMapper {

	List<Map<String, Object>> getComboCusList(Request request) throws Exception;

	Map<String, Object> getConsignedMaterialsOutput(Map<String, Object> params) throws Exception;

	Map<String, Object> getConsignedMaterialsOutputCheck(Map<String, Object> params) throws Exception;

	Map<String, Object> createMf15No(ConsignedMaterialsOutputDto.Request request) throws Exception;

	boolean insertConsignedMaster(MasterInfo masterInfo) throws Exception;

	List<Map<String, Object>> getProdStockList(Lot lotParams) throws Exception;

	boolean insertConsignedDetail(DetailInfo detailInfo) throws Exception;

	boolean saveOfProductionOutput(DetailInfo detailInfo) throws Exception;

}
