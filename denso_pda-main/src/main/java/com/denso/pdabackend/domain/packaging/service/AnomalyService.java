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

	public Map<String, Object> getAnomalyInfo(Request request) {
		return anomalyMapper.getAnomalyInfo(request);
	}
	
	public boolean insertOfAnomaly(List<Info> insertList) {
		
		insertList.forEach(item -> {
			try {
				anomalyMapper.insertOfAnomaly(item);	
			} catch(Exception e) {
				e.printStackTrace();
			}
		});

		return true;
	}

	public Map<String, Object> getSeq(Request request) {
		return anomalyMapper.getSeq(request);
	}

}
