package com.denso.pdabackend.domain.material.controller;

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
import com.denso.pdabackend.domain.material.dto.MaterialsMoveDto;
import com.denso.pdabackend.domain.material.service.MaterialsMoveService;
import com.denso.pdabackend.domain.output.dto.OutputSearchDto;
import com.denso.pdabackend.domain.packaging.dto.AnomalyDto;
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
@Slf4j
@RequiredArgsConstructor
@RequestMapping("materialsMove/materialsMove")
public class MaterialsMoveController {

	private final AuthenticationFacade auth;
	
	private final MaterialsMoveService materialsMoveService; 
	
	@GetMapping
	@Operation(summary = "재고이동 정보", description = "재고이동 정보")
	public ResponseEntity<?> getMaterialsMove(MaterialsMoveDto.Request request) throws Exception {

		UserDto userInfo = auth.getUserInfo();
		
		String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        int empno = userInfo.getEmpNo();
		
		if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
        }
		
		Map<String,Object> data = new HashMap<String,Object>();
		
		request.setSt02Company(company);
		request.setSt02Factory(factory);
		
		Map<String,Object> materialsMove =  materialsMoveService.getMaterialsMove(request);
		data.put("materialsMove", materialsMove);

		return ResponseEntityUtil.ok(data);
	}
	
	/**
     * 재고이동 등록
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping
	@Operation(summary = "재고이동 등록", description = "재고이동 등록")
	public ResponseEntity<?> saveMaterialsMove(@RequestBody Map<String,Object> params) throws Exception {

		log.debug("{}", params);
		
		List<MaterialsMoveDto.Info> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<MaterialsMoveDto.Info>>() {});
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
        	
        	for( MaterialsMoveDto.Info info : insertList ) {

        		info.setSt02Company(company);
    			info.setSt02Factory(factory);
    			info.setSt02Empno(empno);
    			info.setSt02Qrcode(StringUtils.nullString(params.get("st02Qrcode")));

    			MaterialsMoveDto.Request request = new MaterialsMoveDto.Request();
    			request.setSt02Company(auth.getUserInfo().getCompany());
    			request.setSt02Factory(auth.getUserInfo().getFactory());
    			//request.setSt02Dat(info.getSt02Dat());
    			request.setSt02Qrcode( StringUtils.nullString(params.get("st02Qrcode")) );
    			
    			// 이미 등록된 이상처리 데이터인지 확인.
    			// 기존의 입고 테이블에 회사, 공장, no, 창고, 구역을 이용한 중복 확인
//    			Map<String, Object> resultMap = materialsMoveService.getDuplicationMaterialsMove(request);
//    			if (resultMap != null) {
//    				return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "이미 이상 등록 된 제품이 있습니다.");
//    			}
    			
    		}
        	
        	// 출고 히스토리 등록 및 재고수량 감소
			materialsMoveService.insertOfOutputHistory(insertList);
        	
        	// 입고 히스토리 등록 및 재고수량 증가
			materialsMoveService.insertOfInputHistory(insertList);
        	
        }
        
		return ResponseEntityUtil.created("이상처리가 등록되었습니다.");
	}

}
