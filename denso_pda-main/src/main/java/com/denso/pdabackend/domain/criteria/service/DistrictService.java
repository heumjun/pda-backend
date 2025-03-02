package com.denso.pdabackend.domain.criteria.service;

import java.util.List;
import java.util.Map;

import com.denso.pdabackend.domain.criteria.dto.DistrictDto;

public interface DistrictService {

    List<Map<String, Object>> getDistrictList(DistrictDto.Request params) throws Exception;

    List<Map<String, Object>> getDistrictQrList(DistrictDto.Request params) throws Exception;

    Map<String, Object> getDistrictInfo(DistrictDto.Request params) throws Exception;

    boolean saveOfDistrict(List<DistrictDto.Info> districtAddedInfo, List<DistrictDto.Info> districtEditedInfo) throws Exception;

    boolean deleteOfDistrict(List<DistrictDto.Info> districtDeletedInfo) throws Exception;
}
