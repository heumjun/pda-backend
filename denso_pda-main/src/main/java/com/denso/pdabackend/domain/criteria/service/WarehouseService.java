package com.denso.pdabackend.domain.criteria.service;

import java.util.List;
import java.util.Map;

import com.denso.pdabackend.domain.criteria.dto.WarehouseDto;

public interface WarehouseService {

    List<Map<String, Object>> getWarehouseList(WarehouseDto.Request params) throws Exception;

    Map<String, Object> getWarehouseInfo(WarehouseDto.Request params) throws Exception;

    boolean saveOfWarehouse(List<WarehouseDto.Info> warehouseAddedInfo, List<WarehouseDto.Info> warehouseEditedInfo) throws Exception;

    boolean deleteOfWarehouse(List<WarehouseDto.Info> warehouseDeletedInfo) throws Exception;

    boolean getWarehouseCheck(WarehouseDto.Request params) throws Exception;
}
