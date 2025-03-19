package com.denso.pdabackend.domain.product.controller;

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
import com.denso.pdabackend.domain.product.dto.LotFaultDto;
import com.denso.pdabackend.domain.product.service.LotFaultService;
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
@RequestMapping("lotFault/lotFault")
public class LotFaultController {
	
    private final AuthenticationFacade auth;
    private final LotFaultService lotFaultService;

    /**
     * 라인코드 선택을 위한 콤보박스 
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/getComboLineList")
    @Operation(summary = "라인 목록", description = "라인 목록")
    public ResponseEntity<?> getComboLineList(LotFaultDto.Request request) throws Exception{

        Map<String,Object> data = new HashMap<String,Object>();

        List<Map<String, Object>> comboLineList = lotFaultService.getComboLineList(request);

        data.put("comboLineList", comboLineList);

        return ResponseEntityUtil.ok(data);

    }
    
    /**
     * 설비코드 선택을 위한 콤보박스 
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/getComboEquipCodeList")
    @Operation(summary = "설비코드 목록", description = "설비 목록")
    public ResponseEntity<?> getComboEquipCodeList(LotFaultDto.Request request) throws Exception{
    	
    	Map<String,Object> data = new HashMap<String,Object>();
    	
    	UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        
        request.setSt08Company(company);
        request.setSt08Factory(factory);
		
    	List<Map<String, Object>> comboEquipCodeList = lotFaultService.getComboEquipCodeList(request);
    	
    	data.put("comboEquipCodeList", comboEquipCodeList);
    	
    	return ResponseEntityUtil.ok(data);
    	
    }
    
    /**
     * 스캔된 QRCODE에 대한 불량 입고 된 품목 조회
     * @param info
     * @return
     * @throws Exception
     */
    @GetMapping
	@Operation(summary = "불량처리 입고품목 조회", description = "불량처리 입고품목 조회")
	public ResponseEntity<?> getLotFault(LotFaultDto.Info info) throws Exception {

		Map<String,Object> data = new HashMap<String,Object>();
		
		UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        
        info.setSt08Company(company);
		info.setSt08Factory(factory);
		
		Map<String,Object> faultInfo = lotFaultService.getLotFault(info);
		data.put("faultInfo", faultInfo);

		return ResponseEntityUtil.ok(data);
	}
    
    /**
     * 제조(생산) 공정 불량 처리
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping
	@Operation(summary = "제조(생산) 불량 등록", description = "제조(생산) 불량 등록")
	public ResponseEntity<?> saveOfLotFault(@RequestBody Map<String,Object> params) throws Exception {

		log.debug("{}", params);
		
		List<LotFaultDto.Info> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<LotFaultDto.Info>>() {});
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
        	
        	for( LotFaultDto.Info info : insertList ) {

        		info.setSt08Company(company);
    			info.setSt08Factory(factory);
    			info.setSt08Empno(empno);
    			info.setSt08Line( StringUtils.nullString(params.get("st08Line")) );
    			info.setSt08EquipCode( StringUtils.nullString(params.get("st08EquipCode")) );

    			// seq 취득
    			LotFaultDto.Request request = new LotFaultDto.Request();
    			request.setSt08Company(company);
    			request.setSt08Factory(factory);
    			request.setSt08Dat(info.getSt08Dat());
    			request.setSt08Qrcode(info.getSt08Qrcode());
    			
    			Map<String, Object> seqMap = lotFaultService.getSeq(request);
    			request.setSt08Seq(  Integer.parseInt(String.valueOf(seqMap.get("st08Seq")))  );

    			// 이미 등록된 불량처리 데이터인지 확인.
    			Map<String, Object> resultMap = lotFaultService.getDuplicationLotFaultInfo(request);
    			if (resultMap != null) {
    				return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "이미 불량 등록 된 제품이 있습니다.");
    			}
    			
    			// Info에 SEQ 입력.
    			info.setSt08Seq(  Integer.parseInt(String.valueOf(seqMap.get("st08Seq")))  );

    		}
        	
        }
        
        // 제조(생산) 공정 불량처리 등록
        lotFaultService.insertOfLotFault(insertList);

		return ResponseEntityUtil.created("불량처리가 등록되었습니다.");
	}
}
