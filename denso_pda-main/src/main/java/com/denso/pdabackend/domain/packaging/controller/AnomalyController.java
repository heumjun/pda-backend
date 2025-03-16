package com.denso.pdabackend.domain.packaging.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.packaging.dto.AnomalyDto;
import com.denso.pdabackend.domain.packaging.service.AnomalyService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.response.StatusCode;
import com.denso.pdabackend.response.exception.BusinessException;
import com.denso.pdabackend.token.dto.UserDto;
import com.denso.pdabackend.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("anomaly/anomaly")
public class AnomalyController {
	
    private final AuthenticationFacade auth;
    private final AnomalyService anomalyService;

    @PostMapping
	@Operation(summary = "이상처리 등록", description = "이상처리 등록")
	public ResponseEntity<?> saveAnomaly(@RequestBody Map<String,Object> params) throws Exception {

		log.debug("{}", params);
		
		List<AnomalyDto.Info> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<AnomalyDto.Info>>() {});
		if( ObjectUtils.isEmpty(insertList) ) throw new BusinessException("저장할 내역이 없습니다.");
		
		UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        
        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 수정할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 수정할 수 없습니다.");
        }
		
        if( insertList != null ) {
        	
        	for( AnomalyDto.Info info : insertList ) {

        		info.setSt08Company(auth.getUserInfo().getCompany());
    			info.setSt08Factory(auth.getUserInfo().getFactory());
    			info.setSt08Empno(auth.getUserInfo().getEmpNo());

    			AnomalyDto.Request request = new AnomalyDto.Request();
    			request.setSt08Company(auth.getUserInfo().getCompany());
    			request.setSt08Factory(auth.getUserInfo().getFactory());
    			request.setSt08Dat(info.getSt08Dat());
    			request.setSt08Seq(info.getSt08Seq());
    			
    			Map<String, Object> seqMap = anomalyService.getSeq(request);
    			request.setSt08Seq(  Integer.parseInt(String.valueOf(seqMap.get("st08Seq")))  );

    			// 같은 납품확인서로 2개이상 입고하는 경우를 없애야함
    			Map<String, Object> resultMap = anomalyService.getAnomalyInfo(request);
    			if (resultMap != null) {
    				return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "이미 이상등록 된 제품이 있습니다.");
    			}
    			
    			info.setSt08Seq(  Integer.parseInt(String.valueOf(seqMap.get("st08Seq")))  );

    		}
        	
        }
        anomalyService.insertOfAnomaly(insertList);

		return ResponseEntityUtil.created("이상처리가 등록되었습니다.");
	}
}
