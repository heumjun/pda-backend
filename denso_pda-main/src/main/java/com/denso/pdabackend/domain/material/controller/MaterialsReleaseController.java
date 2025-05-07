package com.denso.pdabackend.domain.material.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.material.dto.MaterialsReleaseDto;
import com.denso.pdabackend.domain.material.service.MaterialsReleaseService;
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
@RequestMapping("materialsRelease/materialsRelease")
public class MaterialsReleaseController {

	private final AuthenticationFacade auth;
	
	private final MaterialsReleaseService materialsReleaseService; 
	
	@PostMapping("getMaterialsRelease")
	@Operation(summary = "재고이동 정보", description = "재고이동 정보")
	public ResponseEntity<?> getMaterialsRelease(@RequestBody Map<String,Object> params) throws Exception {

		UserDto userInfo = auth.getUserInfo();
		
		String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
		
		if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
        }

		params.put("company", company);
		params.put("factory", factory);
		
		Map<String,Object> materialsMove =  materialsReleaseService.getMaterialsRelease(params);

		if(materialsMove == null){
			materialsMove =  materialsReleaseService.getLotBox(params);
		}

		return ResponseEntityUtil.ok(materialsMove);
	}
	
	/**
     * 재고이동 등록
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping
	@Operation(summary = "재고이동 등록", description = "재고이동 등록")
	public ResponseEntity<?> saveMaterialsRelease(@RequestBody Map<String,Object> params) throws Exception {

		log.debug("{}", params);
		
		List<MaterialsReleaseDto.Info> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<MaterialsReleaseDto.Info>>() {});
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
        	
        	for( MaterialsReleaseDto.Info info : insertList ) {

        		info.setSt02Company(company);
    			info.setSt02Factory(factory);
    			info.setSt02Empno(empno);
    			
    			info.setLine( StringUtils.nullString(params.get("line")) );
    			info.setStok( StringUtils.nullString(params.get("stok")) );
    			info.setDist( StringUtils.nullString(params.get("dist")) );

    			MaterialsReleaseDto.Request request = new MaterialsReleaseDto.Request();
    			request.setSt02Company(auth.getUserInfo().getCompany());
    			request.setSt02Factory(auth.getUserInfo().getFactory());
    			
    			// 이미 등록된 이상처리 데이터인지 확인.
    			// 기존의 입고 테이블에 회사, 공장, no, 창고, 구역을 이용한 중복 확인
//    			Map<String, Object> resultMap = materialsMoveService.getDuplicationMaterialsMove(request);
//    			if (resultMap != null) {
//    				return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "이미 이상 등록 된 제품이 있습니다.");
//    			}

				// 재고에 등록된 데이터인지 확인
				params.put("company", company);
				params.put("factory", factory);
				params.put("st02Qrcode", info.getSt02Qrcode());
				Map<String,Object> resultMap =  materialsReleaseService.getMaterialsRelease(params);

				List<MaterialsReleaseDto.Info> result = new ArrayList<>();
				result.add(info);

				if(resultMap != null){
					// 출고 히스토리 등록 및 재고수량 감소
					materialsReleaseService.insertOfOutputHistory(result);

					// 입고 히스토리 등록 및 재고수량 증가
					materialsReleaseService.insertOfInputHistory(result);
				}
				else{
					materialsReleaseService.insertOfOnlyOutnputHistory(result);
				}

    		}

//        	// 출고 히스토리 등록 및 재고수량 감소
//			materialsReleaseService.insertOfOutputHistory(insertList);
//
//        	// 입고 히스토리 등록 및 재고수량 증가
//			materialsReleaseService.insertOfInputHistory(insertList);
        	
        }
        
		return ResponseEntityUtil.created("불출이 등록되었습니다.");
	}

}
