package com.denso.pdabackend.domain.smd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.smd.dto.PartsInputRequestDto;
import com.denso.pdabackend.domain.smd.dto.PartsInputRequestDto.Info;
import com.denso.pdabackend.domain.smd.dto.PartsInputRequestDto.Request;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;

@Mapper
public interface PartsInputMapper {

	Map<String, Object> getPartsInputRequestInfo(PartsInputRequestDto.Request params);

	boolean updateOfPdStock(StockDto.Info stockInfo);

	void insertOfPartInputHistory(Info item);

	void insertOfSmdOutStock(Info item);

	List<Map<String, Object>> getCompMfList(PartsInputRequestDto.Request params);

	void createMfOrder(Map<String, Object> insertParam);

	void createMfOrderDetail(PartsInputRequestDto.Info item);

	String getMf01Pcc(Map<String, Object> insertParam);

}
