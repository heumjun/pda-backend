package com.denso.pdabackend.domain.pcb.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.pcb.dto.FaultDto.Info;
import com.denso.pdabackend.domain.pcb.dto.FaultDto.Request;

@Mapper
public interface FaultMapper {

	/*
	 * 불량처리 입고품목 조회
	 */
	Map<String, Object> getFault(Info info);
	
	/*
	 * seq 취득
	 */
	Map<String, Object> getSeq(Request request);
	
	/*
	 * 이미 등록된 불량처리 데이터인지 확인.
	 */
	Map<String, Object> getDuplicationFaultInfo(Request request);
	
	/*
	 * PCB 공정 불량처리 등록
	 */
	boolean insertOfFault(Info info);

}
