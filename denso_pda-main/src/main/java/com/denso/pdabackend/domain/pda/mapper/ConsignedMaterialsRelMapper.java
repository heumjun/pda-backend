package com.denso.pdabackend.domain.pda.mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.codeManage.dto.TrainerDto.TrainerInfo;
import com.denso.pdabackend.domain.codeManage.dto.TrainerDto.TrainerRequest;

@Mapper
public interface ConsignedMaterialsRelMapper {

    List<Map<String, Object>> getCmrList(Map<String,Object> params);
    Map<String, Object> getCmrInfo(Map<String,Object> params);
    Map<String, Object> getCmrSignImage(Map<String,Object> params);
    Map<String, Object> getQrCodeInfo(Map<String,Object> params);
    
    void updateSign(Map<String,Object> param);
    void updateSignNull(Map<String,Object> param);
	void insertSt03(Map<String, Object> param);
	void deleteSt03(Map<String, Object> param);
	void updateSt01(Map<String, Object> param);
	List<Map<String, Object>> getDeleteSt03List(Map<String, Object> param);
	
    
}
