package com.denso.pdabackend.domain.criteria.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.criteria.dto.DistrictDto;
import com.denso.pdabackend.domain.criteria.dto.WarehouseDto;
import com.denso.pdabackend.domain.criteria.mapper.DistrictMapper;
import com.denso.pdabackend.domain.criteria.mapper.WarehouseMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseMapper warehouseMapper;
    private final DistrictMapper districtMapper;

    @Override
    public List<Map<String, Object>> getWarehouseList(WarehouseDto.Request request) throws Exception {
        return warehouseMapper.getWarehouseList(request);
    }

    @Override
    public boolean getWarehouseCheck(WarehouseDto.Request request) throws Exception {
        if(warehouseMapper.getWarehouseCheck(request).size()>0) return true;
        return false;
    }

    @Override
    public Map<String, Object> getWarehouseInfo(WarehouseDto.Request request) throws Exception{
        return warehouseMapper.getWarehouseInfo(request);
    }

    @Override
    public boolean saveOfWarehouse(List<WarehouseDto.Info> warehouseAddedInfo, List<WarehouseDto.Info> warehouseEditedInfo) throws Exception {

        if (warehouseAddedInfo != null) {
            warehouseAddedInfo.forEach(item -> {
                try {
                    warehouseMapper.saveOfWarehouse(item);
                } catch (Exception e) {
                    e.printStackTrace();;
                }
            });
        }

        if (warehouseEditedInfo != null) {
            warehouseEditedInfo.forEach(item -> {
                try {
                    warehouseMapper.updateOfWarehouse(item);
                } catch (Exception e) {
                    e.printStackTrace();;
                }
            });
        }

        return true;
    }

    @Override
    public boolean deleteOfWarehouse(List<WarehouseDto.Info> warehouseDeletedInfo) throws Exception {

        if (warehouseDeletedInfo != null) {
            warehouseDeletedInfo.forEach(item -> {
                try {
                    warehouseMapper.deleteOfWarehouse(item);

                    // 구역 삭제
                    DistrictDto.Info info = new DistrictDto.Info();
                    info.setCompany(item.getCompany());
                    info.setFactory(item.getFactory());
                    info.setCm16Stok(item.getCm15Code());

                    districtMapper.deleteOfStockDistrict(info);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        return true;
    }
}
