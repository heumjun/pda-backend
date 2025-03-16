package com.denso.pdabackend.domain.smd.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.smd.dto.PartsInputRequestDto;
import com.denso.pdabackend.domain.smd.dto.PartsInputRequestDto.Info;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;

@Mapper
public interface PartsInputMapper {

	Map<String, Object> getPartsInputRequestInfo(PartsInputRequestDto.Request params);

	boolean updateOfPdStock(StockDto.Info stockInfo);

	void insertOfPartInputHistory(Info item);

	boolean createMfOrder(Info item);

	void insertOfSmdOutStock(Info item);

}
