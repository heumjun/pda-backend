package com.denso.pdabackend.domain.criteria.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.criteria.dto.WarehouseDto;

@Mapper
public interface WarehouseMapper {

    List<Map<String, Object>> getWarehouseList(WarehouseDto.Request request) throws Exception;

    List<Map<String, Object>> getWarehouseCheck(WarehouseDto.Request request) throws Exception;

    Map<String, Object> getWarehouseInfo(WarehouseDto.Request request) throws Exception;

    Boolean saveOfWarehouse(WarehouseDto.Info params) throws Exception;

    Boolean updateOfWarehouse(WarehouseDto.Info params) throws Exception;

    Boolean deleteOfWarehouse(WarehouseDto.Info params) throws Exception;
}
