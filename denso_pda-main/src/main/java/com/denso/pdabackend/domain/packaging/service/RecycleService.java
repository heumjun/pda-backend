package com.denso.pdabackend.domain.packaging.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.packaging.dto.RecycleDto.Info;
import com.denso.pdabackend.domain.packaging.mapper.RecycleMapper;
import com.denso.pdabackend.response.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecycleService {

	private final RecycleMapper recycleMapper;
	
	public Map<String, Object> getMf17Info(com.denso.pdabackend.domain.packaging.dto.RecycleDto.Request request) throws BusinessException {
		return recycleMapper.getMf17Info(request);
	}
	
	public Map<String, Object> getMp02Info(com.denso.pdabackend.domain.packaging.dto.RecycleDto.Request request) throws BusinessException {
		return recycleMapper.getMp02Info(request);
	}

	public boolean saveRecycle(List<Info> insertList) throws BusinessException {
		// 재투입 등록
		insertList.forEach(item -> {
			
			int duplCnt = recycleMapper.duplicationRecycle(item);
			
			if ( duplCnt > 0 ) {
				// 품목 CNT + 1업데이트
				recycleMapper.updateRecycle(item);
			} else {
				// 신규
				recycleMapper.insertRecycle(item);
			}
		});

		return true;
	}
	
}
