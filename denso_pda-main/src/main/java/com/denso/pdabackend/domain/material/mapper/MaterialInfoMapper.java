package com.denso.pdabackend.domain.material.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public  interface MaterialInfoMapper {

	Map<String, Object> getMaterial(Map<String, Object> param);

}
