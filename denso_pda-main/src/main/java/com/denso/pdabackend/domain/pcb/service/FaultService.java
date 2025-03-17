package com.denso.pdabackend.domain.pcb.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.pcb.dto.FaultDto.Info;
import com.denso.pdabackend.domain.pcb.dto.FaultDto.Request;
import com.denso.pdabackend.domain.pcb.mapper.FaultMapper;
import com.denso.pdabackend.utils.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaultService {

	private final FaultMapper faultMapper;
	
	/*
	 * 불량처리 입고품목 조회
	 */
	public Map<String, Object> getFault(Info info) {
		return faultMapper.getFault(info);
	}
	
	/*
	 * seq 취득
	 */
	public Map<String, Object> getSeq(Request request) {
		return faultMapper.getSeq(request);
	}

	/*
	 * 이미 등록된 불량처리 데이터인지 확인.
	 */
	public Map<String, Object> getDuplicationFaultInfo(Request request) {
		return faultMapper.getDuplicationFaultInfo(request);
	}
	
	/*
	 * PCB 공정 불량처리 등록
	 */
	public boolean insertOfFault(List<Info> insertList) {
		
		insertList.forEach(item -> {
			try {
				// PCB 불량처리 등록
				faultMapper.insertOfFault(item);	
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		});

		return true;
	}

}
