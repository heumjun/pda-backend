package com.denso.pdabackend.domain.mfr.controller;

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
import com.denso.pdabackend.domain.mfr.dto.MfrPartsInputRequestDto;
import com.denso.pdabackend.domain.mfr.service.MfrPartsInputService;
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
@RequestMapping("mfr/partsInput")
public class MfrPartsInputController {

	private final AuthenticationFacade auth;
    private final MfrPartsInputService mfrPartsInputService;

    @GetMapping("/getPartsInputRequestInfo")
    @Operation(summary = "부품투입 QR READ", description = "부품투입 QR READ")
    public ResponseEntity<?> getPartsInputRequestInfo(MfrPartsInputRequestDto.Request request) throws Exception{

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

        Map<String, Object> selectInfo =  mfrPartsInputService.getPartsInputRequestInfo(request);

        if(selectInfo == null || ObjectUtils.isEmpty(selectInfo)) {
            return ResponseEntityUtil.error(StatusCode.NOT_FOUND,"유효한 부품식별표 QR이 아닙니다.");
        }

        if(selectInfo.get("st03Lot") != null) {
        	return ResponseEntityUtil.error(StatusCode.NOT_FOUND,"부품투입 수량이 없습니다.");
        }

        data.put("partsInputRequestInfo", selectInfo);

        return ResponseEntityUtil.ok(data);

    }

    @PostMapping("getMfrPartsInputInfo")
    @Operation(summary = "바코드 리딩 정보", description = "바코드 리딩 정보")
    public ResponseEntity<?> getMfrPartsInputInfo(@RequestBody Map<String,Object> params) throws Exception {

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

        Map<String,Object> materialsMove =  mfrPartsInputService.getMfrPartsInputInfo(params);

        return ResponseEntityUtil.ok(materialsMove);
    }

    @GetMapping("/getCompMfList")
    @Operation(summary = "완제품 목록", description = "완제품 목록 콤보박스용")
    public ResponseEntity<?> getCompMfList(MfrPartsInputRequestDto.Request request) throws Exception{

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

        List<Map<String, Object>> selectList =  mfrPartsInputService.getCompMfList(request);

        data.put("compMfList", selectList);

        return ResponseEntityUtil.ok(data);

    }

    @PostMapping
	@Operation(summary = "출고이력 등록", description = "출고이력 등록")
	public ResponseEntity<?> saveOfOutput(@RequestBody Map<String,Object> params) throws Exception {

		log.debug("{}", params);

		List<MfrPartsInputRequestDto.Info> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<MfrPartsInputRequestDto.Info>>() {});
		List<MfrPartsInputRequestDto.Info> updateList = JsonUtils.deserialize(params.get("updateList"), new TypeReference<List<MfrPartsInputRequestDto.Info>>() {});

		if(ObjectUtils.isEmpty(insertList) && ObjectUtils.isEmpty(updateList)) throw new BusinessException("저장할 내역이 없습니다.");

		UserDto userInfo = auth.getUserInfo();

        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        int empNo = userInfo.getEmpNo();
        String empName = userInfo.getEmpName();

        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 수정할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 수정할 수 없습니다.");
        }

        if(params.get("compMfCode") == null) {
        	return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "완성품이 선택되지 않았습니다.");
        }

        if(params.get("compMfQty") == null || Integer.parseInt(String.valueOf(params.get("compMfQty"))) == 0) {
        	return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "완성수량이 없거나 0이 될 수 없습니다.");
        }

        Map<String,Object> insertParam = new HashMap<String,Object>();
        if(insertList != null){

            for(MfrPartsInputRequestDto.Info info : insertList){
                info.setCompany(company);
                info.setFactory(factory);
                info.setEmpno(empNo);

                MfrPartsInputRequestDto.Request request = new MfrPartsInputRequestDto.Request();
                request.setCompany(company);
                request.setFactory(factory);
                request.setSt01Code(info.getSt01Code());
                request.setSt01Lot(info.getSt01Lot());
                request.setSt01LotSeq(info.getSt01LotSeq());

                // 같은 출고요청서 2개이상 출고하는 경우를 없애야함
                Map<String, Object> resultMap = mfrPartsInputService.getPartsInputRequestInfo(request);
                if (resultMap == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "유효하지 않은 부품이 있습니다.");
                }

                if(Integer.parseInt(String.valueOf(resultMap.get("st01Qty"))) == 0) {
                	return ResponseEntityUtil.error(StatusCode.NOT_FOUND,"부품투입된 항목입니다.");
                }
            }
        }
        insertParam.put("compMfCode", StringUtils.nullString(params.get("compMfCode")) );
        insertParam.put("compMfQty", StringUtils.nullString(params.get("compMfQty")) );
        insertParam.put("compMfLine", StringUtils.nullString(params.get("compMfLine")) );

        insertParam.put("company", company);
        insertParam.put("factory", factory);
        insertParam.put("empNo", empNo);

        insertParam.put("insertList", insertList);

        mfrPartsInputService.insertOfPartsInputHistory(insertParam);

		return ResponseEntityUtil.created("부품투입 등록이 완료되었습니다");
	}

}
