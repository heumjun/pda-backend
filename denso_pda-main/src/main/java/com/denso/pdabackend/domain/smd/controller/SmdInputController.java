package com.denso.pdabackend.domain.smd.controller;

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
import com.denso.pdabackend.domain.output.dto.OutputSearchDto;
import com.denso.pdabackend.domain.smd.dto.PartsInputRequestDto;
import com.denso.pdabackend.domain.smd.dto.SmdInputRequestDto;
import com.denso.pdabackend.domain.smd.service.SmdInputService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.response.StatusCode;
import com.denso.pdabackend.token.dto.UserDto;
import com.denso.pdabackend.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("smdInput/smdInput")
public class SmdInputController {

	private final AuthenticationFacade auth;
	
	private final SmdInputService smdInputService;
	
	@GetMapping
    @Operation(summary = "출고요청 구분", description = "출고요청 구분")
    public ResponseEntity<?> getOutputGbn(SmdInputRequestDto.Request request) throws Exception{

        Map<String,Object> data = new HashMap<String,Object>();

        UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();

        request.setCompany(company);
        request.setFactory(factory);

        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
        }

        int selectCnt =  smdInputService.getOutputGbn(request);

        data.put("selectCnt", selectCnt);

        return ResponseEntityUtil.ok(data);

    }
	
	@GetMapping("/getLotInfo")
	@Operation(summary = "getLotInfo", description = "getLotInfo")
	public ResponseEntity<?> getLotInfo(OutputSearchDto.Request params) throws Exception{
		
		Map<String,Object> data = new HashMap<String,Object>();

		//토큰인증 사용자 정보
		UserDto userInfo = auth.getUserInfo();

		String company = userInfo.getCompany();
		String factory = userInfo.getFactory();

		params.setCompany(company);
		params.setFactory(factory);
		params.setSt03Code(params.getSt03Code());
		params.setSt03Lot(params.getSt03Lot());
		params.setSt03LotSeq(params.getSt03LotSeq());

		if (company == null) {
			return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
		}

		if (factory == null) {
			return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
		}

		Map<String,Object> getLotInfo = smdInputService.getLotInfo(params);

		data.put("lotInfo", getLotInfo);

		return ResponseEntityUtil.ok(data);
		
	}

    @PostMapping
    @Operation(summary = "출고완료요청 등록", description = "출고완료요청 등록")
    public ResponseEntity<?> saveOfSmdInput(@RequestBody Map<String, Object> params) throws Exception {

        List<SmdInputRequestDto.Info> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<SmdInputRequestDto.Info>>() {});

        UserDto userInfo = auth.getUserInfo();

        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();

        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 수정할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 수정할 수 없습니다.");
        }

        // 등록하는 경우
        if (insertList != null) {
        	
            for(SmdInputRequestDto.Info info : insertList) {
            	info.setCompany(company);
            	info.setFactory(factory);
            	info.setMf13Empno(userInfo.getEmpNo());
            }
            
            smdInputService.saveOfSmdInput(insertList);
        }

        return ResponseEntityUtil.created("출고요청완료가 등록되었습니다.");
    }
	

}
