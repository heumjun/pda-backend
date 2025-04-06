package com.denso.pdabackend.domain.cigma.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.material.dto.MaterialsReleaseDto.Info;

@Mapper
public interface MaterialsReleaseDmesMapper {

	void insertDmes18(Info item) throws Exception;

}
