package com.denso.pdabackend.domain.criteria.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.criteria.dto.CommonCodeDto.Request;

@Mapper
public interface CommonCodeMapper {

	List<Map<String, Object>> getCommonCodeDetailList(Request params) throws Exception;

}
