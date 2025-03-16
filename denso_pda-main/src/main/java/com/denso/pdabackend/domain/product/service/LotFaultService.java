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
	
	public Map<String, Object> getSeq(Request request) {
		return lotFaultMapper.getSeq(request);
	}

	public Map<String, Object> getLotFaultInfo(Request request) {
		return lotFaultMapper.getLotFaultInfo(request);
	}
	
	public boolean insertOfFault(List<Info> insertList) {
		
		insertList.forEach(item -> {
			try {
				lotFaultMapper.insertOfLotFault(item);	
			} catch(Exception e) {
				e.printStackTrace();
			}
		});

		return true;
	}

}
