package com.denso.pdabackend.domain.material.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.material.dto.MaterialDto.Material;
import com.denso.pdabackend.domain.material.dto.MaterialDto.MaterialRequest;

@Mapper
public interface MaterialMapper {

    List<Map<String, Object>> getMaterialList(MaterialRequest params);

    List<Map<String, Object>> getAutocompleteCode(String code);

    Map<String, Object> getMaterial(MaterialRequest params);

    void saveOfMaterial(Material item);

    void deleteOfMaterial(Material item);

}
