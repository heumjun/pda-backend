package com.denso.pdabackend.domain.pda.service;

import java.lang.constant.Constable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.pda.mapper.ConsignedMaterialsRelMapper;
import com.denso.pdabackend.domain.pda.mapper.ConsignedMaterialsRelSearchMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsignedMaterialsRelService {

    private final ConsignedMaterialsRelMapper cmrMapper;
    /**
     * 사급출고 상세 출고 아이템 목록
     * @param params
     * @return
     */
    public List<Map<String, Object>> getCmrList(Map<String,Object> params) throws Exception {
        return cmrMapper.getCmrList(params);
    }
    /**
     * 사급출고 상세 마스터 내용
     * @return
     * @throws Exception
     */
    public Map<String, Object> getCmrInfo(Map<String,Object> params) throws Exception {
        return cmrMapper.getCmrInfo(params);
    }
    /**
     * QR code의 정보를 읽고 존재하는 정보인지 아닌지와 정보가 존재할 시 해당 정보를 리턴한다.
     * @param params
     * @return
     * @throws Exception
     */
    public Map<String, Object> getQrCodeInfo(Map<String,Object> params) throws Exception {
    	Map<String,Object> result = new HashMap<String, Object>();
    	Map<String,Object> qrInfo = cmrMapper.getQrCodeInfo(params);
    	if(qrInfo == null) {
    		result.put("result_status", "F");
    	} else {
    		result.put("result_status", "S");
    		result.put("result_data", qrInfo);
    	}
        return result;
    }
}
