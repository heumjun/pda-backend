package com.denso.pdabackend.domain.smd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.output.dto.OutputSearchDto;
import com.denso.pdabackend.domain.smd.dto.SmdInputRequestDto;
import com.denso.pdabackend.domain.smd.dto.SmdInputRequestDto.Request;
import com.denso.pdabackend.domain.warehousing.dto.InputHistorySearchDto.Info;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;

@Mapper
public interface SmdInputMapper {

	boolean updateOfOutputRequestComp(SmdInputRequestDto.Info item);

	boolean updateOfStok(StockDto.Info stockInfo);
	
	boolean updateOfPdStock(StockDto.Info stockInfo);

	Map<String, Object> getSeq(Map<String, Object> hashMap);

	boolean insertOfInputHistory(Info inputInfo);

	int getOutputGbn(Request request);

	Map<String, Object> getLotInfo(OutputSearchDto.Request params);

	List<Map<String, Object>> inspectChk(OutputSearchDto.Request params) throws Exception;
	List<Map<String, Object>> inspectSpecChk(OutputSearchDto.Request params) throws Exception;
	List<Map<String, Object>> firstInOutChk(OutputSearchDto.Request params) throws Exception;
	List<Map<String, Object>> firstInputData(OutputSearchDto.Request params) throws Exception;

}
