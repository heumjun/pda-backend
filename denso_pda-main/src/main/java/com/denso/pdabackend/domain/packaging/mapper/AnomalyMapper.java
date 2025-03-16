package com.denso.pdabackend.domain.packaging.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.packaging.dto.AnomalyDto.Info;
import com.denso.pdabackend.domain.packaging.dto.AnomalyDto.Request;

@Mapper
public interface AnomalyMapper {

	Map<String, Object> getAnomalyInfo(Request request);
	
	boolean insertOfAnomaly(Info item);

	Map<String, Object> getSeq(Request request);

}
