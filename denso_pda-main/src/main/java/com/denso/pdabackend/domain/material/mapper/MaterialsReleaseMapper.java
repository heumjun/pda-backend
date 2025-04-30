package com.denso.pdabackend.domain.material.mapper;

import java.util.Map;

import com.denso.pdabackend.domain.warehousing.dto.StockDto;
import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.material.dto.MaterialsReleaseDto.Info;
import com.denso.pdabackend.domain.material.dto.MaterialsReleaseDto.Request;

@Mapper
public interface MaterialsReleaseMapper {

	Map<String, Object> getMaterialsRelease(Map<String,Object> params) throws Exception;

	Map<String, Object> getLotBox(Map<String,Object> params) throws Exception;

	boolean updateOfStok(StockDto.Info stockInfo);

	Map<String, Object> getSeq(Request request) throws Exception;

	Map<String, Object> getDuplicationMaterialsRelease(Request request) throws Exception;

	int getSt02Seq(Info item) throws Exception;
	
	boolean insertOfOutputHistory(Info item) throws Exception;

	boolean insertOfInputHistory(Info item) throws Exception;


}
