package com.denso.pdabackend.domain.output.controller;

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
import com.denso.pdabackend.domain.output.dto.OutputRequestDto;
import com.denso.pdabackend.domain.output.dto.OutputSearchDto;
import com.denso.pdabackend.domain.output.service.OutputService;
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
@Slf4j
@RequiredArgsConstructor
@RequestMapping("output")
public class OutputController {

	private final AuthenticationFacade auth;
    private final OutputService outputService;
    
    /**
     * 출고요청서 조회
     * @param params
     * @return
     * @throws Exception
     */
    @GetMapping("outputReqSel")
    @Operation(summary = "출고요청서 조회", description = "출고요청서 조회")
    public ResponseEntity<?> getOutputRequestSearch(OutputSearchDto.Request request) throws Exception {

    	Map<String,Object> data = new HashMap<String,Object>();

        UserDto userInfo = auth.getUserInfo();
        
        System.out.println("userInfo >> " + userInfo);
        
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        int empNo = userInfo.getEmpNo();
        String empName = userInfo.getEmpName();
        
        System.out.println(empNo);
        System.out.println(empName);

        request.setCompany(company);
        request.setFactory(factory);
        
        request.setCompany(userInfo.getCompany());
        request.setFactory(userInfo.getFactory());

        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
        }

        //request.setMf13No(String.valueOf(request.getMf13No()));
        //request.setMf13DatFr(String.valueOf(request.getSt03DatFr()));
        //request.setMf13DatTo(String.valueOf(request.getSt03DatTo()));
        List<Map<String, Object>> detailInfo =  outputService.getOutputRequestSearch(request);

        if(detailInfo.size() < 1) {
            return ResponseEntityUtil.error(StatusCode.NOT_FOUND,"출고요청서가 존재하지 않습니다.");
        }

        data.put("detailInfo", detailInfo);
        
        return ResponseEntityUtil.ok(data);
    }
    
    @GetMapping("/getOutputRegisterList")
    @Operation(summary = "출고요청서 선택", description = "출고요청서 선택")
    public ResponseEntity<?> getOutputRequestSel(OutputRequestDto.Request request) throws Exception{

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

        request.setMf13No(String.valueOf(request.getMf13No()));
        List<Map<String, Object>> selectInfo =  outputService.getOutputRequestSel(request);

        if(selectInfo.size() < 1) {
            return ResponseEntityUtil.error(StatusCode.NOT_FOUND,"출고요청서가 존재하지 않습니다.");
        }

        data.put("outputRegisterList", selectInfo);

        return ResponseEntityUtil.ok(data);

    }
    
    @PostMapping
	@Operation(summary = "출고이력 등록", description = "출고이력 등록")
	public ResponseEntity<?> saveOfOutput(@RequestBody Map<String,Object> params) throws Exception {

		log.debug("{}", params);
		
		List<OutputSearchDto.Info> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<OutputSearchDto.Info>>() {});
		List<OutputSearchDto.Info> updateList = JsonUtils.deserialize(params.get("updateList"), new TypeReference<List<OutputSearchDto.Info>>() {});

		if(ObjectUtils.isEmpty(insertList) && ObjectUtils.isEmpty(updateList)) throw new BusinessException("저장할 내역이 없습니다.");
		
		UserDto userInfo = auth.getUserInfo();

        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        
//        if (company == null) {
//            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 수정할 수 없습니다.");
//        }
//
//        if (factory == null) {
//            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 수정할 수 없습니다.");
//        }
		
        if(insertList != null){
        	
            for(OutputSearchDto.Info info : insertList){
                info.setCompany(company);
                info.setFactory(factory);
                info.setSt03Empno(0);

                OutputSearchDto.Request request = new OutputSearchDto.Request();
                request.setCompany(company);
                request.setFactory(factory);
                request.setSt03Code(info.getCm08Code());
                request.setSt03Lot(info.getSt03Lot());
                request.setSt03OutputNo(info.getSt03OutputNo());
                request.setSt03LotSeq(info.getSt03LotSeq());

                // 같은 출고요청서 2개이상 출고하는 경우를 없애야함
                Map<String, Object> resultMap = outputService.getOutputHistorySearchInfo(request);
                if (resultMap != null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "이미 등록된 출고요청서입니다.");
                }

                // 수입검사대기인 품목인 경우 출고가 되지 않도록 해야함.
                Map<String, Object> inspectChkMap = outputService.inspectChk(request);

                // inspectChkMap이 없는 경우는 출고가능
                // inspectChkMap의 상태가 수입검사대기인 경우 출고불가능
                if(inspectChkMap != null){
                    if(String.valueOf(inspectChkMap.get("qa07Status")).equals("W")){
                        return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "수입검사대기인 품목은 출고 불가능합니다.");
                    } else if(String.valueOf(inspectChkMap.get("qa07Status")).equals("E")){
                        
                        // 불량인데 특채처리여부가 Y이면 출고가능하도록 설정
                        Map<String, Object> inspectSpecChkMap = outputService.inspectSpecChk(request);
                        if(inspectSpecChkMap == null){
                            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "수입검사불량인 항목은 출고 불가능합니다.\n특채처리여부를 확인하세요");
                        }
                    }
                }
            }
        }

        outputService.insertOfOutputHistory(insertList);

		return ResponseEntityUtil.created("출고이력이 등록되었습니다.");
	}
    
    @Operation(summary = "창고, 구역 가져오기(리딩기)", description = "창고, 구역 가져오기(리딩기)")
    @GetMapping("getStokDist")
    public ResponseEntity<?> getStokDist(OutputSearchDto.Request params) throws Exception {
    	
        Map<String,Object> data = new HashMap<String,Object>();

        //토큰인증 사용자 정보
        UserDto userInfo = auth.getUserInfo();

        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        company = "DNKR";
        factory = "0001";

        params.setCompany(company);
        params.setFactory(factory);
        params.setSt03Code(params.getSt03Code());
        params.setSt03Lot(params.getSt03Lot());
        params.setSt03LotSeq(params.getSt03LotSeq());

//        if (company == null) {
//            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
//        }
//
//        if (factory == null) {
//            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
//        }

        Map<String,Object> getStokDist = outputService.getStokDist(params);

        data.put("stokDistInfo", getStokDist);

        return ResponseEntityUtil.ok(data);
    }
    
}
