package com.denso.pdabackend.domain.warehousing.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.pda.mapper.ConsignedMaterialsRelSearchMapper;
import com.denso.pdabackend.domain.warehousing.dto.WarehousingDto.WarehousingRequest;
import com.denso.pdabackend.domain.warehousing.mapper.WarehousingMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehousingService {

    private final WarehousingMapper warehousingMapper;

	public List<Map<String, Object>> getWarehousingList(WarehousingRequest params) {
		return warehousingMapper.getWarehousingList(params);
	}
    
}
