package com.denso.pdabackend.domain.restore.controller;

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
import com.denso.pdabackend.domain.restore.dto.RestoreDto;
import com.denso.pdabackend.domain.restore.service.RestoreService;
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
@RequestMapping("restore/restore")
public class RestoreController {

	private final AuthenticationFacade auth;
    private final RestoreService restoreService;
    
    @GetMapping
    @Operation(summary = "반납등록 리스트", description = "반납등록 리스트")
    public ResponseEntity<?> getRestoreList(RestoreDto.Request request) throws Exception {

    	Map<String, Object> data = new HashMap<String, Object>();

        //토큰인증 사용자 정보
        UserDto userInfo = auth.getUserInfo();

        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();

        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
        }

        // 조회 INPUT 설정
        // 사용자 회사, 공장 정보 세팅.
        request.setCompany(company);
        request.setFactory(factory);

        // 반품요청 상세 리스트 조회
        List<Map<String, Object>> restoreList = restoreService.getRestoreList(request);

        // 조회 데이터 바운딩
        data.put("restoreList", restoreList);

        return ResponseEntityUtil.ok(data);

    }
    
    @PostMapping
    @Operation(summary = "반납 등록", description = "반납 등록")
    public ResponseEntity<?> updateReturnForReg(@RequestBody Map<String,Object> params) throws Exception {

    	log.debug("{}", params);
    	
		List<RestoreDto.Info> updateList = JsonUtils.deserialize(params.get("updateList"), new TypeReference<List<RestoreDto.Info>>() {});
    	
		if(ObjectUtils.isEmpty(updateList)) throw new BusinessException("저장할 내역이 없습니다.");
		
		UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();

        if (company == null) {
        	return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 수정할 수 없습니다.");
        }

        if (factory == null) {
        	return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 수정할 수 없습니다.");
        }

        // 공통정보 세팅 - 등록
        if(updateList != null) {
            for (RestoreDto.Info info : updateList) {
                info.setSt11Company(company);
                info.setSt11Factory(factory);
                info.setSt11Empno(userInfo.getEmpNo()); // 등록자
                info.setSt11UpdEmpno(userInfo.getEmpNo()); // 수정자
            }
        }

        if(!restoreService.updateRestore(updateList)) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "반납등록에 실패하였습니다.");
        }

        return ResponseEntityUtil.created("반납이 등록되었습니다.");

    }
    
}
