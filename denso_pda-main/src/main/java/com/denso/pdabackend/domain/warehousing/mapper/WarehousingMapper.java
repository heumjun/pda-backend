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

	List<Map<String, Object>> getWarehousingList(WarehousingRequest params);

	void saveOfWarehousing(Warehousing info);

	List<Map<String, Object>> getOutputChk(Request params);

	List<Map<String, Object>> getInputHistorySearchInfo(Request params);

	void updateOfPdStock(Info stockInfo);

	void updateOfInputHistory(InputHistorySearchDto.Info item);

	Map<String, Object> getSeq(HashMap hashMap);

	int getNewLotSeq(InputHistorySearchDto.Info item);

	void insertOfLotStock(Info stockInfo);

	void insertOfInputHistory(InputHistorySearchDto.Info item);

	Map<String, Object> getAvailable(HashMap hashMap);

	void insertInspectionConf(InspectionConfDto.Info inspectionConf);

	List<Map<String, Object>> getInputInfo(StockDto.Request params);

}
