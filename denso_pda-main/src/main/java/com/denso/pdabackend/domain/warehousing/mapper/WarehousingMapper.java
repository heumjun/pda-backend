package com.denso.pdabackend.domain.warehousing.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.warehousing.dto.InputHistorySearchDto;
import com.denso.pdabackend.domain.warehousing.dto.InputHistorySearchDto.Request;
import com.denso.pdabackend.domain.warehousing.dto.InspectionConfDto;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;
import com.denso.pdabackend.domain.warehousing.dto.StockDto.Info;
import com.denso.pdabackend.domain.warehousing.dto.WarehousingDto.Warehousing;
import com.denso.pdabackend.domain.warehousing.dto.WarehousingDto.WarehousingRequest;

@Mapper
public interface WarehousingMapper {
	
	String completeFlag(WarehousingRequest params) throws Exception;

	List<Map<String, Object>> getCompleteWarehousingList(WarehousingRequest params) throws Exception;

	List<Map<String, Object>> getWarehousingList(WarehousingRequest params) throws Exception;

	void saveOfWarehousing(Warehousing info) throws Exception;

	List<Map<String, Object>> getOutputChk(Request params) throws Exception;

	List<Map<String, Object>> getInputHistorySearchInfo(Request params) throws Exception;

	void updateOfPdStock(Info stockInfo) throws Exception;

	void updateOfInputHistory(InputHistorySearchDto.Info item) throws Exception;

	Map<String, Object> getSeq(HashMap hashMap) throws Exception;

	int getNewLotSeq(InputHistorySearchDto.Info item) throws Exception;

	void insertOfLotStock(Info stockInfo) throws Exception;

	void insertOfInputHistory(InputHistorySearchDto.Info item) throws Exception;

	Map<String, Object> getAvailable(HashMap hashMap) throws Exception;

	void insertInspectionConf(InspectionConfDto.Info inspectionConf) throws Exception;

	List<Map<String, Object>> getInputInfo(StockDto.Request params) throws Exception;

}
