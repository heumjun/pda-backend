package com.denso.pdabackend.domain.pda.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.pda.mapper.RelSearchMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RelSearchService {

    private final RelSearchMapper rsMapper;
    /**
     * 트레이너 리스트
     * @param params
     * @return
     */
    public List<Map<String, Object>> getRsList() throws Exception {
        return rsMapper.getRsList();
    }
}
