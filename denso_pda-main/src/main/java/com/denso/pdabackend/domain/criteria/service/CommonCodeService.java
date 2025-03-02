package com.denso.pdabackend.domain.criteria.service;

import java.util.List;
import java.util.Map;

import com.denso.pdabackend.domain.criteria.dto.CommonCodeDto.Request;



public interface CommonCodeService {
	
	/**
	 * 공통코드 속성리스트
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getCommonCodeDetailList(Request params) throws Exception;


}
