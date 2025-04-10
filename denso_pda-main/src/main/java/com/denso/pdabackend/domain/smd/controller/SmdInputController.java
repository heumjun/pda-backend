package com.denso.pdabackend.domain.smd.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        params.setStok(params.getStok());

		if (company == null) {
			return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
		}

		if (factory == null) {
			return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
		}

        Map<String,Object> getLotInfo = smdInputService.getLotInfo(params);
        // qr코드에 따른 데이터 가져오기 전에 선입선출 여부 확인하고, 선입선출인 경우 제대로 섭입선출되고 있는 지 확인되어야한다.
        // 1. 해당 품번 선입선출 확인
        Map<String, Object> getFirstInOut = smdInputService.firstInOutChk(params);

        // 해당 품번이 존재할 때
        if(getLotInfo!= null) {

            // 해당 품번이 수입검사대기/불량인지 체크해서 경고문 리턴해줘야함.
            Map<String, Object> inspectChkMap = smdInputService.inspectChk(params);

            // inspectChkMap이 없는 경우는 출고가능
            // inspectChkMap의 상태가 수입검사대기인 경우 출고불가능
            if (inspectChkMap != null) {
                if (String.valueOf(inspectChkMap.get("qa07Status")).equals("W")) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "수입검사대기인 품목은 출고 불가능합니다.");
                } else if (String.valueOf(inspectChkMap.get("qa07Status")).equals("E")) {

                    // 불량인데 특채처리여부가 Y이면 출고가능하도록 설정
                    Map<String, Object> inspectSpecChkMap = smdInputService.inspectSpecChk(params);
                    if (inspectSpecChkMap == null) {
                        return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "수입검사불량인 항목은 출고 불가능합니다.\n" +
                                "특채처리여부를 확인하세요");
                    }
                }
            }

            // 해당 품번이 선입선출에 해당하지 않는 경우
            if(getFirstInOut.get("cm08Fifo").equals("N")){
                if(getLotInfo != null){
                    data.put("lotInfo", getLotInfo);
                } else {
                    data.put("lotInfo", null);
                }
                // 해당 품번이 선입선출에 해당하는 경우
            } else {
                // 해당 품번의 가장 먼저 입고된 일자를 가져와서 리딩한 품번의 입고일자를 비교
                Map<String, Object> firstInputData = smdInputService.firstInputData(params);

                // 리딩한 품번의 입고일자
                String inputDate = getLotInfo.get("st02Dat").toString().substring(0,10).trim();
                // 선입한 품번의 입고일자
                String firstInputDate = firstInputData.get("st02Dat").toString().substring(0,10).trim();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate inputD = LocalDate.parse(inputDate, formatter);
                LocalDate firstD = LocalDate.parse(firstInputDate, formatter);

                // firstD < inputD : 선입한 입고일자보다 리딩한 품번의 입고일자가 큰 경우 에러
                if(inputD.isAfter(firstD)){
                    data.put("lotInfo", "fifoError");
//                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "먼저 입고된 품번의 LOT를 출고 후 진행해주십시오.");
                } else {
                    data.put("lotInfo", getLotInfo);
                }
            }

        } else {
            data.put("lotInfo", null);
        }

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
