package com.denso.pdabackend.domain.partsInput.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.partsInput.dto.PartsInputRequestDto;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;

@Mapper
public interface PartsInputMapper {
	Map<String, Object> getPartsInputRequestInfo(PartsInputRequestDto.Request params);

	boolean updateOfPdStock(StockDto.Info stockInfo);
	boolean updateOfSmdStock(StockDto.Info stockInfo);
}
