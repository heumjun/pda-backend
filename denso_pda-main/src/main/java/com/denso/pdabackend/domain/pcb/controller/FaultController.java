package com.denso.pdabackend.domain.pcb.controller;

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
import com.denso.pdabackend.domain.pcb.dto.FaultDto;
import com.denso.pdabackend.domain.pcb.service.FaultService;
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
@RequestMapping("fault/fault")
public class FaultController {
	
    private final AuthenticationFacade auth;
    private final FaultService faultService;

    /**
     * 스캔된 QRCODE에 대한 불량 입고 된 품목 조회
     * @param info
     * @return
     * @throws Exception
     */
    @GetMapping
	@Operation(summary = "불량처리 입고품목 조회", description = "불량처리 입고품목 조회")
	public ResponseEntity<?> getFault(FaultDto.Info info) throws Exception {

		Map<String,Object> data = new HashMap<String,Object>();
		
		UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        
        info.setSt08Company(company);
		info.setSt08Factory(factory);
		
		Map<String,Object> faultInfo = faultService.getFault(info);
		data.put("faultInfo", faultInfo);

		return ResponseEntityUtil.ok(data);
	}
    
    /**
     * PCB 공정 불량 처리
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping
	@Operation(summary = "PCB 불량 등록", description = "PCB 불량 등록")
	public ResponseEntity<?> saveOfFault(@RequestBody Map<String,Object> params) throws Exception {

		log.debug("{}", params);
		
		List<FaultDto.Info> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<FaultDto.Info>>() {});
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
        	
        	for( FaultDto.Info info : insertList ) {

        		info.setSt08Company(company);
    			info.setSt08Factory(factory);
    			info.setSt08Empno(empno);

    			// seq 취득
    			FaultDto.Request request = new FaultDto.Request();
    			request.setSt08Company(company);
    			request.setSt08Factory(factory);
    			request.setSt08Dat(info.getSt08Dat());
    			request.setSt08Qrcode(info.getSt08Qrcode());
    			
    			Map<String, Object> seqMap = faultService.getSeq(request);
    			request.setSt08Seq(  Integer.parseInt(String.valueOf(seqMap.get("st08Seq")))  );

    			// 이미 등록된 불량처리 데이터인지 확인.
    			Map<String, Object> resultMap = faultService.getDuplicationFaultInfo(request);
    			if (resultMap != null) {
    				return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "이미 불량 등록 된 제품이 있습니다.");
    			}
    			
    			// Info에 SEQ 입력.
    			info.setSt08Seq(  Integer.parseInt(String.valueOf(seqMap.get("st08Seq")))  );

    		}
        	
        }
        
        // PCB 공정 불량처리 등록
        faultService.insertOfFault(insertList);

		return ResponseEntityUtil.created("불량처리가 등록되었습니다.");
	}
}
