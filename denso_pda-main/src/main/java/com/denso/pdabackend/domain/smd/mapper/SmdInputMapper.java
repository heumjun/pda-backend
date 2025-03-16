package com.denso.pdabackend.domain.smd.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.smd.dto.SmdInputRequestDto;
import com.denso.pdabackend.domain.warehousing.dto.InputHistorySearchDto.Info;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;

@Mapper
public interface SmdInputMapper {

	boolean updateOfOutputRequestComp(SmdInputRequestDto.Info item);

	boolean updateOfStok(StockDto.Info stockInfo);
	
	boolean updateOfPdStock(StockDto.Info stockInfo);

	Map<String, Object> getSeq(Map<String, Object> hashMap);

	boolean insertOfInputHistory(Info inputInfo);

}
