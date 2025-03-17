package com.denso.pdabackend.domain.packaging.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.packaging.dto.AnomalyDto.Info;
import com.denso.pdabackend.domain.packaging.dto.AnomalyDto.Request;
import com.denso.pdabackend.domain.packaging.mapper.AnomalyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnomalyService {

	private final AnomalyMapper anomalyMapper;
	
	/*
	 * 이상처리 입고품목 조회
	 */
	public Map<String, Object> getAnomaly(Info info) {
		return anomalyMapper.getAnomaly(info);
	}

	/*
	 * seq 취득
	 */
	public Map<String, Object> getSeq(Request request) {
		return anomalyMapper.getSeq(request);
	}
	
	/*
	 * 이미 등록된 이상처리 데이터인지 확인.
	 */
	public Map<String, Object> getDuplicationAnomalyInfo(Request request) {
		return anomalyMapper.getDuplicationAnomalyInfo(request);
	}
	
	/*
	 * 포장 공정 이상처리 등록
	 */
	public boolean insertOfAnomaly(List<Info> insertList) {
		
		// 포장 불량처리 등록
		insertList.forEach(item -> {
			try {
				anomalyMapper.insertOfAnomaly(item);	
			} catch(Exception e) {
				e.printStackTrace();
			}
		});

		return true;
	}

}
