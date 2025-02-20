package com.denso.pdabackend.domain.pda.mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.codeManage.dto.TrainerDto.TrainerInfo;
import com.denso.pdabackend.domain.codeManage.dto.TrainerDto.TrainerRequest;

@Mapper
public interface ConsignedMaterialsRelSearchMapper {

    List<Map<String, Object>> getCmrsList();

}
