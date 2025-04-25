package com.denso.pdabackend.domain.product.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.product.dto.LotFaultDto.Info;
import com.denso.pdabackend.domain.product.dto.LotFaultDto.Request;


@Mapper
public interface LotFaultMapper {

	/*
	 * 라인코드 선택을 위한 콤보박스
	 */
	List<Map<String, Object>> getComboLineList(Request request);
	
	/*
	 * 설비코드 선택을 위한 콤보박스
	 */
	List<Map<String, Object>> getComboEquipCodeList(Request request);
	
	/*
	 * 불량처리 입고품목 조회
	 */
	Map<String, Object> getLotFault(Info info);
	
	/*
	 * seq 취득
	 */
	Map<String, Object> getSeq(Request request);
	
	/*
	 * 이미 등록된 불량처리 데이터인지 확인.
	 */
	Map<String, Object> getDuplicationLotFaultInfo(Request request);
	
	/*
	 * 제조(생산) 공정 불량처리 등록
	 */
	boolean insertOfLotFault(Info info);

	List<Map<String, Object>> getArrayList(Request request);

}
