package com.denso.pdabackend.domain.criteria.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.criteria.dto.DistrictDto;
import com.denso.pdabackend.domain.criteria.mapper.DistrictMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {
    
    private final DistrictMapper districtMapper;

    @Override
    public List<Map<String, Object>> getDistrictList(DistrictDto.Request request) throws Exception {
        return districtMapper.getDistrictList(request);
    }
    @Override
    public List<Map<String, Object>> getDistrictQrList(DistrictDto.Request request) throws Exception {
        return districtMapper.getDistrictQrList(request);
    }

    @Override
    public Map<String, Object> getDistrictInfo(DistrictDto.Request params) throws Exception{
        return districtMapper.getDistrictInfo(params);
    }

    @Override
    public boolean saveOfDistrict(List<DistrictDto.Info> districtAddedInfo, List<DistrictDto.Info> districtEditedInfo) throws Exception {

        if (districtAddedInfo != null) {
            districtAddedInfo.forEach(item -> {
                try {
                    districtMapper.saveOfDistrict(item);
                } catch (Exception e) {
                    e.printStackTrace();;
                }
            });
        }

        if (districtEditedInfo != null) {
            districtEditedInfo.forEach(item -> {
                try {
                    districtMapper.updateOfDistrict(item);
                } catch (Exception e) {
                    e.printStackTrace();;
                }
            });
        }

        return true;
    }

    @Override
    public boolean deleteOfDistrict(List<DistrictDto.Info> districtDeletedInfo) throws Exception {

        if (districtDeletedInfo != null) {
            districtDeletedInfo.forEach(item -> {
                try {
                    districtMapper.deleteOfDistrict(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        return true;
    }
}
