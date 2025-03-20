package com.denso.pdabackend.domain.packaging.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.packaging.dto.RecycleDto;
import com.denso.pdabackend.domain.packaging.service.RecycleService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.response.StatusCode;
import com.denso.pdabackend.response.exception.BusinessException;
import com.denso.pdabackend.token.dto.UserDto;
import com.denso.pdabackend.utils.JsonUtils;
import com.denso.pdabackend.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("recycle/recycle")
public class RecycleController {
	
    private final AuthenticationFacade auth;
    private final RecycleService recycleService;

    /**
     * 스캔된 QRCODE에 대한 이상처리 품목 조회
     * @param info
     * @return
     * @throws Exception
     */
    @GetMapping
	@Operation(summary = "재투입 품목 조회", description = "재투입 품목 조회")
	public ResponseEntity<?> getRecycle(RecycleDto.Request request) throws BusinessException {

		Map<String,Object> data = new HashMap<String,Object>();
		
		UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        
        request.setSt12Company(company);
        request.setSt12Factory(factory);
		
		Map<String,Object> mf17Info = recycleService.getMf17Info(request);
		
		 if (mf17Info == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "문구 수정 - MF17에 데이터 존재하지 않음.");
        }

		Map<String,Object> mp02Info = recycleService.getMp02Info(request);
		
		if (mp02Info == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "문구 수정 - MP02에 데이터 존재하지 않음.");
        }
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("st12Company", company);
		resultMap.put("st12Factory", factory);
		resultMap.put("st12No", mf17Info.get("mf17No"));
		resultMap.put("st12Code", mf17Info.get("mf17Code"));
		resultMap.put("st12Name", mf17Info.get("mf17Name"));
		resultMap.put("st12Pcc", mf17Info.get("mf17Pcc"));
		resultMap.put("st12LotSeq", mp02Info.get("mp02LotSeq"));
		
		data.put("recycleInfo", resultMap);

		return ResponseEntityUtil.ok(data);
	}
    
    /**
     * 포장 공정 이상 처리
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping
	@Operation(summary = "재투입 등록", description = "재투입 등록")
	public ResponseEntity<?> saveRecycle(@RequestBody Map<String,Object> params) throws BusinessException {

		log.debug("{}", params);
		
		List<RecycleDto.Info> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<RecycleDto.Info>>() {});
		if( ObjectUtils.isEmpty(insertList) ) throw new BusinessException("저장할 내역이 없습니다.");
		
		UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        int empno = userInfo.getEmpNo();
        
        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 수정할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 수정할 수 없습니다.");
        }
		
        if( insertList != null ) {
        	
        	for( RecycleDto.Info info : insertList ) {

        		info.setSt12Company(company);
    			info.setSt12Factory(factory);
    			info.setSt12Empno(empno);
    			info.setSt12LineCode( StringUtils.nullString(params.get("st12LineCode")) );
    			info.setSt12EquipCode( StringUtils.nullString(params.get("st12EquipCode")) );
    			
    		}
        	
        	// 포장 공정 이상처리 등록
        	recycleService.saveRecycle(insertList);
        	
        }
        
		return ResponseEntityUtil.created("이상처리가 등록되었습니다.");
	}
    
}
