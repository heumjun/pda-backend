package com.denso.pdabackend.domain.pda.controller;

import java.io.BufferedInputStream;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.material.dto.MaterialDto;
import com.denso.pdabackend.domain.pda.service.ConsignedMaterialsRelSearchService;
import com.denso.pdabackend.domain.pda.service.ConsignedMaterialsRelService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("pda/consigned-materials-rel")
public class ConsignedMaterialsRelController {

    private final AuthenticationFacade auth;
    private final ConsignedMaterialsRelService cmrService;

    @PostMapping
    @Operation(summary = "사급출고상세 조회", description = "사급출고상세 조회")
    public ResponseEntity<?> getCmrList(@RequestBody Map<String,Object> params) throws Exception {
    	log.debug("{}", params);
        Map<String,Object> data = new HashMap<String,Object>();
    
        Map<String,Object> cmrInfo =  cmrService.getCmrInfo(params);
        Map<String,Object> cmrInfoImg =  cmrService.getCmrSignImage(params);
        
        List<Map<String,Object>> cmrList =  cmrService.getCmrList(params);
        if(cmrInfoImg != null) {
        	byte arr[] = blobToBytes((Blob) cmrInfoImg.get("MF15_SIGN"));

            //data url 리턴 실시
            if(arr.length > 0 && arr != null){ //데이터가 들어 있는 경우
                //바이트를 base64인코딩 실시
                String base64Encode = byteToBase64(arr);
                base64Encode = "data:image/png;base64," + base64Encode;
                
                cmrInfo.put("mf15Sign", base64Encode);
            }
        }
        data.put("list",cmrList);
        data.put("info",cmrInfo);

        return ResponseEntityUtil.ok(data);
    }
    
    @PostMapping("save")
    @Operation(summary = "사급출고 저장", description = "사급출고 저장")
    public ResponseEntity<?> saveOfCmr(@RequestBody Map<String,Object> params) throws Exception {
        log.debug("{}",params);
        log.debug("{}",auth.getUserInfo());
        
        params.put("userInfo", auth.getUserInfo());
		cmrService.saveOfCmr(params);
        
        return ResponseEntityUtil.created(null);
    }
    
    @PostMapping("delete")
    @Operation(summary = "사급출고 삭제", description = "사급출고 삭제")
    public ResponseEntity<?> deleteOfCmr(@RequestBody Map<String,Object> params) throws Exception {
        log.debug("{}",params);
        
		cmrService.deleteOfCmr(params);
        
        return ResponseEntityUtil.created(null);
    }
    
    @PostMapping("qrcode")
    @Operation(summary = "qrCode 값 불러오기", description = "QR CODE에서 읽어온 정보를 유효성 확인 후 정보를 리턴한다.")
    public ResponseEntity<?> getQrCodeInfo(@RequestBody Map<String,Object> params) throws Exception{
        Map<String,Object> data = new HashMap<String,Object>();

        Map<String,Object> qrInfo =  cmrService.getQrCodeInfo(params);
        data.put("result", qrInfo);
        return ResponseEntityUtil.ok(data);
    }
    
    // [blob 데이터를 바이트로 변환해주는 메소드]
    private static byte[] blobToBytes(Blob blob) {
        BufferedInputStream is = null;
        byte[] bytes = null;
        try {
            is = new BufferedInputStream(blob.getBinaryStream());
            bytes = new byte[(int) blob.length()];
            int len = bytes.length;
            int offset = 0;
            int read = 0;

            while (offset < len
                    && (read = is.read(bytes, offset, len - offset)) >= 0) {
                offset += read;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    // [byte를 base64로 인코딩 해주는 메소드]
    private static String byteToBase64(byte[] arr) {
        String result = "";
        try {
            result = Base64Utils.encodeToString(arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
