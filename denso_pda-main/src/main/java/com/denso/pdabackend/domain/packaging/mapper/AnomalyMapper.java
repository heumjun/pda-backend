package com.denso.pdabackend.domain.packaging.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.packaging.dto.AnomalyDto.Info;
import com.denso.pdabackend.domain.packaging.dto.AnomalyDto.Request;

@Mapper
public interface AnomalyMapper {
	
	/*
	 * 이상처리 입고품목 조회
	 */
	Map<String, Object> getAnomaly(Info info);
	
	/*
	 * seq 취득
	 */
	Map<String, Object> getSeq(Request request);

	/*
	 * 이미 등록된 불량처리 데이터인지 확인.
	 */
	Map<String, Object> getDuplicationAnomalyInfo(Request request);
	
	/*
	 * 포장 공정 이상처리 등록
	 */
	boolean insertOfAnomaly(Info item);

	/*
	 * 부서 목록
	 */
	List<Map<String, Object>> comboDeptCodeList(Request request);

}
