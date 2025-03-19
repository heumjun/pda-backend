package com.denso.pdabackend.domain.packaging.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.packaging.dto.RecycleDto;
import com.denso.pdabackend.domain.packaging.dto.RecycleDto.Info;
import com.denso.pdabackend.response.exception.BusinessException;

@Mapper
public interface RecycleMapper {
	
	Map<String, Object> getMf17Info(RecycleDto.Request request) throws BusinessException;

	Map<String, Object> getMp02Info(RecycleDto.Request request) throws BusinessException;

	int duplicationRecycle(Info item) throws BusinessException;
	
	void updateRecycle(Info item) throws BusinessException;
	
	void insertRecycle(Info item) throws BusinessException;

}
