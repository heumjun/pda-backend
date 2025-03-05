package com.denso.pdabackend.domain.output.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.output.dto.OutputRequestDto;
import com.denso.pdabackend.domain.output.dto.OutputSearchDto;
import com.denso.pdabackend.domain.output.dto.OutputSearchDto.Info;
import com.denso.pdabackend.domain.output.dto.OutputSearchDto.Request;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;

@Mapper
public interface OutputMapper {

	List<Map<String, Object>> getOutputRequestSearch(OutputSearchDto.Request params);

	List<Map<String, Object>> getOutputRequestSel(OutputRequestDto.Request params);

	List<Map<String, Object>> getOutputHistorySearchInfo(Request params);

	List<Map<String, Object>> inspectChk(Request params);

	List<Map<String, Object>> inspectSpecChk(Request params);

	boolean insertOfOutputHistory(Info item);

	void updateOfPdStock(StockDto.Info stockInfo);

	List<Map<String, Object>> getStokDist(Request params);
	
}
