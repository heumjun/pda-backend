package com.denso.pdabackend.domain.material.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.material.mapper.MaterialInfoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialInfoService {
	
	private final MaterialInfoMapper materialInfoMapper;

	public Map<String, Object> getMaterial(Map<String,Object> param) throws Exception {
		return materialInfoMapper.getMaterial(param);
	}

}
