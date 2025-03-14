package com.denso.pdabackend.domain.partsInput.service;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.partsInput.mapper.PartsInputMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartsInputService {
    
    private final PartsInputMapper partsInputMapper;

}
