package com.denso.pdabackend.domain.pcb.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.pcb.dto.FaultDto.Info;
import com.denso.pdabackend.domain.pcb.dto.FaultDto.Request;
import com.denso.pdabackend.domain.pcb.mapper.FaultMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaultService {

	private final FaultMapper faultMapper;
	
	public Map<String, Object> getSeq(Request request) {
		return faultMapper.getSeq(request);
	}

	public Map<String, Object> getFaultInfo(Request request) {
		return faultMapper.getFaultInfo(request);
	}
	
	public boolean insertOfFault(List<Info> insertList) {
		
		insertList.forEach(item -> {
			try {
				faultMapper.insertOfFault(item);	
			} catch(Exception e) {
				e.printStackTrace();
			}
		});

		return true;
	}

}
