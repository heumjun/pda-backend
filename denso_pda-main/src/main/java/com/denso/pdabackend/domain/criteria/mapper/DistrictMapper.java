package com.denso.pdabackend.domain.criteria.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.criteria.dto.DistrictDto;

@Mapper
public interface DistrictMapper {

    List<Map<String, Object>> getDistrictList(DistrictDto.Request request) throws Exception;

    List<Map<String, Object>> getDistrictQrList(DistrictDto.Request request) throws Exception;

    Map<String, Object> getDistrictInfo(DistrictDto.Request request) throws Exception;

    Boolean saveOfDistrict(DistrictDto.Info params) throws Exception;

    Boolean updateOfDistrict(DistrictDto.Info params) throws Exception;

    Boolean deleteOfDistrict(DistrictDto.Info params) throws Exception;

    Boolean deleteOfStockDistrict(DistrictDto.Info params) throws Exception;
}
