package com.denso.pdabackend.domain.mfr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.mfr.dto.MfrPartsInputRequestDto;
import com.denso.pdabackend.domain.smd.dto.PartsInputRequestDto.Info;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;

@Mapper
public interface MfrPartsInputMapper {

	Map<String, Object> getPartsInputRequestInfo(MfrPartsInputRequestDto.Request params);

	Map<String, Object> getMfrPartsInputInfo(Map<String,Object> params) throws Exception;

	boolean updateOfStok(StockDto.Info stockInfo);

	boolean updateOfPdStock(StockDto.Info stockInfo);

	void insertOfPartInputHistory(MfrPartsInputRequestDto.Info item);

	void insertOfSmdOutStock(MfrPartsInputRequestDto.Info item);

	List<Map<String, Object>> getCompMfList(MfrPartsInputRequestDto.Request params);

	void createMfOrder(Map<String, Object> insertParam);

	void createMfOrderDetail(MfrPartsInputRequestDto.Info item);

}
