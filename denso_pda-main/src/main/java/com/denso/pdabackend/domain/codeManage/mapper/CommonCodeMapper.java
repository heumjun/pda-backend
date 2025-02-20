package com.denso.pdabackend.domain.codeManage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.codeManage.dto.CommonCodeDto.CommonCodeRequest;
import com.denso.pdabackend.domain.codeManage.dto.CommonCodeDto.CommonDesc;
import com.denso.pdabackend.domain.codeManage.dto.CommonCodeDto.CommonHead;

@Mapper
public interface CommonCodeMapper {

    List<Map<String, Object>> getGroupCodeList(CommonCodeRequest params);

    void saveOfGroupCode(CommonHead commonHead);

    List<Map<String, Object>> getCommonCodeList(String groupCode);

    void saveOfCommonCode(CommonDesc commonDesc);

    void deleteOfCommonCode(CommonDesc commonDesc);
    /**
     * 전체 순번 재정렬
     * 
     */
    void updateOfCommonCodeOrder(CommonCodeRequest request);
    

    void deleteOfGroupCode(CommonHead commonHead);

    List<Map<String, Object>> getTableList(String code);

    
    
}
