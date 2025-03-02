package com.denso.pdabackend.domain.warehousing.mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.codeManage.dto.TrainerDto.TrainerInfo;
import com.denso.pdabackend.domain.codeManage.dto.TrainerDto.TrainerRequest;
import com.denso.pdabackend.domain.warehousing.dto.WarehousingDto.WarehousingRequest;

@Mapper
public interface WarehousingMapper {

	List<Map<String, Object>> getWarehousingList(WarehousingRequest params);

}
