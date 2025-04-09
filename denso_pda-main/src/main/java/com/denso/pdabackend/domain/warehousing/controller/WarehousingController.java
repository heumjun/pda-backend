package com.denso.pdabackend.domain.warehousing.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.denso.pdabackend.domain.output.dto.OutputSearchDto;
import com.denso.pdabackend.domain.smd.service.SmdInputService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.output.service.OutputService;
import com.denso.pdabackend.domain.warehousing.dto.InputHistorySearchDto;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;
import com.denso.pdabackend.domain.warehousing.dto.WarehousingDto;
import com.denso.pdabackend.domain.warehousing.dto.WarehousingDto.WarehousingRequest;
import com.denso.pdabackend.domain.warehousing.service.WarehousingService;
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
@RequestMapping("warehousing/warehousing")
public class WarehousingController {

	private final AuthenticationFacade auth;
	private final WarehousingService warehousingService;
	private final SmdInputService smdInputService;
	private final OutputService outputService;

	@GetMapping
	@Operation(summary = "품목입고 조회", description = "품목입고 조회")
	public ResponseEntity<?> getWarehousingList(WarehousingDto.WarehousingRequest params) throws Exception {

		Map<String,Object> data = new HashMap<String,Object>();
		
		params.setCompany(auth.getUserInfo().getCompany());
		params.setFactory(auth.getUserInfo().getFactory());
		
		String completeFlag = warehousingService.completeFlag(params);
		
		List<Map<String,Object>> warehousingList = new ArrayList<Map<String, Object>>();
		
		if (completeFlag.equals("N")) {
			warehousingList =  warehousingService.getWarehousingList(params);
		} else {
			warehousingList =  warehousingService.getCompleteWarehousingList(params);
		}
		//data.put("puInfo", puInfo); // 발주번호 및 제조사 정보를 가져오기 위해서
		data.put("completeFlag", completeFlag);
		data.put("warehousingList", warehousingList);

		return ResponseEntityUtil.ok(data);
	}

	@PostMapping
	@Operation(summary = "품목입고 저장", description = "품목입고 저장")
	public ResponseEntity<?> saveOfWarehousing(@RequestBody Map<String,Object> params) throws Exception {

		log.debug("{}", params);

		List<InputHistorySearchDto.Info> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<InputHistorySearchDto.Info>>() {});
		List<InputHistorySearchDto.Info> updateList = JsonUtils.deserialize(params.get("updateList"), new TypeReference<List<InputHistorySearchDto.Info>>() {});

		if(ObjectUtils.isEmpty(insertList) && ObjectUtils.isEmpty(updateList)) throw new BusinessException("저장할 내역이 없습니다.");

		WarehousingRequest warehousingRequest = WarehousingRequest.builder().build();

		for(InputHistorySearchDto.Info info : updateList) {

			info.setCompany(auth.getUserInfo().getCompany());
			info.setFactory(auth.getUserInfo().getFactory());
			info.setSt02Empno(auth.getUserInfo().getEmpNo());

			InputHistorySearchDto.Request request = new InputHistorySearchDto.Request();
			request.setCompany(auth.getUserInfo().getCompany());
			request.setFactory(auth.getUserInfo().getFactory());
			request.setSt02Code(info.getCm08Code());
			request.setSt02LotSeq(String.valueOf(info.getSt02LotSeq()));
			request.setSt02Lot(info.getSt02Lot());
			// 수정 시 출고이력이 없는 경우에만 출고 가능
			Map<String, Object> resultMap = warehousingService.getOutputChk(request);
			if (resultMap != null) {
				return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "이미 출고된 품목은 수정불가능합니다.");
			}
		}
		warehousingService.updateOfInputHistory(updateList);

		for( InputHistorySearchDto.Info info : insertList ) {

			info.setCompany(auth.getUserInfo().getCompany());
			info.setFactory(auth.getUserInfo().getFactory());
			info.setSt02Empno(auth.getUserInfo().getEmpNo());

			InputHistorySearchDto.Request request = new InputHistorySearchDto.Request();
			request.setCompany(auth.getUserInfo().getCompany());
			request.setFactory(auth.getUserInfo().getFactory());
			request.setSt02Pno(info.getSt02Pno());

			// 같은 납품확인서로 2개이상 입고하는 경우를 없애야함
			Map<String, Object> resultMap = warehousingService.getInputHistorySearchInfo(request);
			if (resultMap != null) {
				return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "이미 등록된 납품확인서입니다.");
			}

		}
		warehousingService.insertOfInputHistory(insertList);

		return ResponseEntityUtil.created(null);
	}


	@Operation(summary = "SCM라벨 품목정보", description = "SCM라벨 품목정보")
	@GetMapping("/stock/getInputInfo")
	public ResponseEntity<?> getInputInfo(StockDto.Request request) throws Exception {

		Map<String,Object> data = new HashMap<String,Object>();

		UserDto userInfo = auth.getUserInfo();
		String company = userInfo.getCompany();
		String factory = userInfo.getFactory();

		request.setCompany(company);
		request.setFactory(factory);
		request.setStok(request.getStok());

		if (company == null) {
			return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
		}

		if (factory == null) {
			return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
		}

		Map<String,Object> inputInfo = warehousingService.getInputInfo(request);

		// 데이터가 존재하는 경우
		if(inputInfo != null) {

			OutputSearchDto.Request params = new OutputSearchDto.Request();
			params.setCompany(inputInfo.get("st01Company").toString());
			params.setFactory(inputInfo.get("st01Factory").toString());
			params.setSt03Code(inputInfo.get("st01Code").toString());
			params.setSt03Lot(inputInfo.get("st01Lot").toString());
			params.setSt03LotSeq(Integer.parseInt(inputInfo.get("st01LotSeq").toString()));

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
		}

		data.put("inputInfo", inputInfo);

		return ResponseEntityUtil.ok(data);

	}
	

}
