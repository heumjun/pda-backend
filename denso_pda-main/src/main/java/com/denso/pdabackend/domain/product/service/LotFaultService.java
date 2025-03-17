package com.denso.pdabackend.domain.product.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.product.dto.LotFaultDto.Info;
import com.denso.pdabackend.domain.product.dto.LotFaultDto.Request;
import com.denso.pdabackend.domain.product.mapper.LotFaultMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LotFaultService {

	private final LotFaultMapper lotFaultMapper;
	
	/*
	 * 라인코드 선택을 위한 콤보박스
	 */
	public List<Map<String, Object>> getComboLineList(Request request) {
		return lotFaultMapper.getComboLineList(request);
	}
	
	/*
	 * 설비코드 선택을 위한 콤보박스
	 */
	public List<Map<String, Object>> getComboEquipCodeList(Request request) {
		return lotFaultMapper.getComboEquipCodeList(request);
	}
	
	/*
	 * 불량처리 입고품목 조회
	 */
	public Map<String, Object> getLotFault(Info info) {
		return lotFaultMapper.getLotFault(info);
	}
	
	/*
	 * seq 취득
	 */
	public Map<String, Object> getSeq(Request request) {
		return lotFaultMapper.getSeq(request);
	}

	/*
	 * 이미 등록된 불량처리 데이터인지 확인.
	 */
	public Map<String, Object> getDuplicationLotFaultInfo(Request request) {
		return lotFaultMapper.getDuplicationLotFaultInfo(request);
	}
	
	/*
	 * 제조(생산) 공정 불량처리 등록
	 */
	public boolean insertOfLotFault(List<Info> insertList) {
		
		insertList.forEach(item -> {
			try {
				
				// 제조(생산) 불량처리 등록
				lotFaultMapper.insertOfLotFault(item);	
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		});

		return true;
	}

}
