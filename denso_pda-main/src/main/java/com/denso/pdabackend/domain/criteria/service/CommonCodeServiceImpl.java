package com.denso.pdabackend.domain.criteria.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.denso.pdabackend.domain.criteria.dto.CommonCodeDto.DetailInfo;
import com.denso.pdabackend.domain.criteria.dto.CommonCodeDto.MasterInfo;
import com.denso.pdabackend.domain.criteria.dto.CommonCodeDto.Request;
import com.denso.pdabackend.domain.criteria.mapper.CommonCodeMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommonCodeServiceImpl implements CommonCodeService {

	private final CommonCodeMapper commonCodeMapper;
	
	@Override
	public List<Map<String, Object>> getCommonCodeDetailList(Request params) throws Exception {
		return commonCodeMapper.getCommonCodeDetailList(params);
	}

}
