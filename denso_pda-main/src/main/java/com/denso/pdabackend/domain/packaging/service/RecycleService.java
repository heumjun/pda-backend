package com.denso.pdabackend.domain.packaging.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.packaging.dto.RecycleDto.Info;
import com.denso.pdabackend.domain.packaging.mapper.RecycleMapper;
import com.denso.pdabackend.response.exception.BusinessException;
import com.denso.pdabackend.utils.StringUtils;

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
			
			Map<String, Object> duplMap = recycleMapper.duplicationRecycle(item);
			
			if ( duplMap != null ) {
				item.setSt12Cnt( Integer.parseInt(StringUtils.nullString(duplMap.get("st12Cnt"))) );
				// 품목 CNT + 1해서 인서트
				recycleMapper.cntPlusRecycle(item);
			} else {
				// 신규
				recycleMapper.insertRecycle(item);
			}
		});

		return true;
	}
	
}
