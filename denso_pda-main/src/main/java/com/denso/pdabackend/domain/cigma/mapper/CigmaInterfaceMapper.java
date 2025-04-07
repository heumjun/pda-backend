package com.denso.pdabackend.domain.cigma.mapper;

import com.denso.pdabackend.domain.output.dto.OutputSearchDto;
import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.material.dto.MaterialsReleaseDto;

@Mapper
public interface CigmaInterfaceMapper {

	void insertDmes18(MaterialsReleaseDto.Info item) throws Exception;
	void insertDmes18Output(OutputSearchDto.Info item) throws Exception;

}
