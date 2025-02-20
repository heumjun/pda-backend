package com.denso.pdabackend.domain.pda.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.pda.mapper.ConsignedMaterialsRelSearchMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsignedMaterialsRelSearchService {

    private final ConsignedMaterialsRelSearchMapper cmrsMapper;
    /**
     * 트레이너 리스트
     * @param params
     * @return
     */
    public List<Map<String, Object>> getCmrsList() throws Exception {
        return cmrsMapper.getCmrsList();
    }
}
