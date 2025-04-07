package com.denso.pdabackend.domain.cigma.mapper;

import com.denso.pdabackend.domain.output.dto.OutputSearchDto;
import com.denso.pdabackend.domain.warehousing.dto.InputHistorySearchDto;
import org.apache.ibatis.annotations.Mapper;
import com.denso.pdabackend.domain.material.dto.MaterialsReleaseDto;

@Mapper
public interface CigmaInterfaceMapper {

	void insertDmes16(InputHistorySearchDto.Info item) throws Exception;
	void insertDmes18(MaterialsReleaseDto.Info item) throws Exception;
	void insertDmes18Output(OutputSearchDto.Info item) throws Exception;


}
