package com.denso.pdabackend.domain.pda.service;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.pda.mapper.ConsignedMaterialsRelMapper;
import com.denso.pdabackend.token.dto.UserDto;
import com.denso.pdabackend.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

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
    public Map<String, Object> getCmrSignImage(Map<String,Object> params) throws Exception {
        return cmrMapper.getCmrSignImage(params);
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
    
    public void saveOfCmr(Map<String,Object> params) throws Exception {
    	
    	UserDto userInfo = (UserDto) params.get("userInfo");
    	Map<String,Object> headerInfo = (Map<String, Object>) params.get("headerInfo");
        List<Map<String,Object>> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<Map<String,Object>>>() {});
        
        headerInfo.put("company", userInfo.getCompany());
        headerInfo.put("factory", userInfo.getFactory());
        
        byte imageArray [] = null;
        final String BASE_64_PREFIX = "data:image/png;base64,";
        try {
            String base64Url = String.valueOf(headerInfo.get("cmrReqSign"));
            if (base64Url.startsWith(BASE_64_PREFIX)){
                imageArray =  Base64.getDecoder().decode(base64Url.substring(BASE_64_PREFIX.length()));
                System.out.println("\n");
                System.out.println("=======================================");
                System.out.println("[DBApiController] : [saveImage]");
                System.out.println("[imageArray] : " + new String(imageArray));
                System.out.println("=======================================");
                System.out.println("\n");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
        headerInfo.put("base64Sign", imageArray);
        
        cmrMapper.updateSign(headerInfo);
        
        for(Map<String,Object> item : insertList) {
        	item.put("company", userInfo.getCompany());
        	item.put("factory", userInfo.getFactory());
        	item.put("st03Empno", userInfo.getUserId());
        	cmrMapper.insertSt03(item);	
        }
        
    }
    
    public void deleteOfCmr(Map<String,Object> params) throws Exception {
    	
    	Map<String,Object> headerInfo = (Map<String, Object>) params.get("headerInfo");
        
    	cmrMapper.updateSignNull(headerInfo);
    	cmrMapper.deleteSt03(headerInfo);
    }
}
