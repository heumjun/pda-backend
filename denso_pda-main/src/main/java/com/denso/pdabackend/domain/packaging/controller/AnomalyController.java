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
import com.denso.pdabackend.domain.packaging.dto.AnomalyDto;
import com.denso.pdabackend.domain.packaging.service.AnomalyService;
import com.denso.pdabackend.domain.product.dto.LotFaultDto;
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
@RequestMapping("anomaly/anomaly")
public class AnomalyController {
	
    private final AuthenticationFacade auth;
    private final AnomalyService anomalyService;

    /**
     * 스캔된 QRCODE에 대한 이상처리 품목 조회
     * @param info
     * @return
     * @throws Exception
     */
    @GetMapping
	@Operation(summary = "이상처리 품목 조회", description = "이상처리 품목 조회")
	public ResponseEntity<?> getAnomaly(AnomalyDto.Info info) throws Exception {

		Map<String,Object> data = new HashMap<String,Object>();
		
		UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        
        info.setSt09Company(company);
		info.setSt09Factory(factory);
		
		Map<String,Object> anomalyInfo = anomalyService.getAnomaly(info);
		data.put("anomalyInfo", anomalyInfo);

		return ResponseEntityUtil.ok(data);
	}
    
    /**
     * 포장 공정 이상 처리
     * @param params
     * @return
     * @throws Exception
     */
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

        		info.setSt09Company(auth.getUserInfo().getCompany());
    			info.setSt09Factory(auth.getUserInfo().getFactory());
    			info.setSt09Empno(auth.getUserInfo().getEmpNo());
    			info.setSt09Dept( StringUtils.nullString(params.get("st09Dept")) );
    			info.setSt09Line( StringUtils.nullString(params.get("st09Line")) );
    			info.setSt09EquipCode( StringUtils.nullString(params.get("st09EquipCode")) );

    			AnomalyDto.Request request = new AnomalyDto.Request();
    			request.setSt09Company(auth.getUserInfo().getCompany());
    			request.setSt09Factory(auth.getUserInfo().getFactory());
    			request.setSt09Dat(info.getSt09Dat());
    			request.setSt09Qrcode(info.getSt09Qrcode());
    			
    			Map<String, Object> seqMap = anomalyService.getSeq(request);
    			request.setSt09Seq(  Integer.parseInt(String.valueOf(seqMap.get("st09Seq")))  );

    			// 이미 등록된 이상처리 데이터인지 확인.
    			Map<String, Object> resultMap = anomalyService.getDuplicationAnomalyInfo(request);
    			if (resultMap != null) {
    				return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "이미 이상 등록 된 제품이 있습니다.");
    			}
    			
    			// Info에 SEQ 입력.
    			info.setSt09Seq(  Integer.parseInt(String.valueOf(seqMap.get("st09Seq")))  );

    		}
        	
        }
        
        // 포장 공정 이상처리 등록
        anomalyService.insertOfAnomaly(insertList);

		return ResponseEntityUtil.created("이상처리가 등록되었습니다.");
	}
    
    /**
     * 부서코드 리스트
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/getComboDeptCodeList")
    @Operation(summary = "부서 목록", description = "부서 목록")
    public ResponseEntity<?> getComboDeptList(AnomalyDto.Request request) throws Exception{

        Map<String,Object> data = new HashMap<String,Object>();

        UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        
        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 수정할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 수정할 수 없습니다.");
        }
        
        request.setSt09Company(auth.getUserInfo().getCompany());
		request.setSt09Factory(auth.getUserInfo().getFactory());
        
        List<Map<String, Object>> comboDeptCodeList = anomalyService.comboDeptCodeList(request);

        data.put("comboDeptCodeList", comboDeptCodeList);

        return ResponseEntityUtil.ok(data);

    }
}
