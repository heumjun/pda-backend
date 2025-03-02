package com.denso.pdabackend.domain.criteria.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.criteria.dto.DistrictDto;
import com.denso.pdabackend.domain.criteria.service.DistrictService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.response.StatusCode;
import com.denso.pdabackend.token.dto.UserDto;
import com.denso.pdabackend.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("criteria/district")
@Slf4j
public class DistrictController {

    private final AuthenticationFacade auth;  //토큰인증된 사용자정보관리
    private final DistrictService districtService;

    @Operation(summary = "구역관리 조회", description = "구역관리 조회")
    @GetMapping
    public ResponseEntity<?> getDistrictList(DistrictDto.Request params) throws Exception {

        Map<String, Object> data = new HashMap<String, Object>();

        UserDto userInfo = auth.getUserInfo();

        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();

        params.setCompany(company);
        params.setFactory(factory);

        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
        }

        List<Map<String, Object>> districtList = districtService.getDistrictList(params);

        data.put("districtList", districtList);

        return ResponseEntityUtil.ok(data);
    }

    @Operation(summary = "구역관리", description = "구역관리")
    @GetMapping("/detail")
    public ResponseEntity<?> getDistrictInfo(DistrictDto.Request params) throws Exception {

        Map<String, Object> data = new HashMap<String, Object>();

        UserDto userInfo = auth.getUserInfo();

        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        String stock = params.getCm16Stok();

        params.setCompany(company);
        params.setFactory(factory);

        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않습니다.");
        }

        if (stock == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "창고코드가 존재하지 않습니다.");
        }

        Map<String, Object> districtInfo = districtService.getDistrictInfo(params);

        data.put("districtInfo", districtInfo);

        return ResponseEntityUtil.ok(data);
    }

    @Operation(summary = "구역관리 등록", description = "구역관리 등록")
    @PostMapping
    public ResponseEntity<?> saveOfDistrict(@RequestBody Map<String, Object> params) throws Exception {

        List<DistrictDto.Info> districtAddedInfo = JsonUtils.deserialize(params.get("districtAddedInfo"), new TypeReference<List<DistrictDto.Info>>(){});
        List<DistrictDto.Info> districtEditedInfo = JsonUtils.deserialize(params.get("districtEditedInfo"), new TypeReference<List<DistrictDto.Info>>(){});

        UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();

        if (districtAddedInfo != null) {
            for (DistrictDto.Info info : districtAddedInfo) {
                info.setCompany(company);
                info.setFactory(factory);

                if (info.getCm16Stok() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "창고코드가 존재하지 않아 추가할 수 없습니다.");
                }

                if (info.getCm16Code() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "구역코드가 존재하지 않아 추가할 수 없습니다.");
                }

                if (info.getCm16Name() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "구역명이 존재하지 않아 추가할 수 없습니다.");
                }

                if (info.getCm16Lock() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "사용여부가 존재하지 않아 추가할 수 없습니다.");
                }

                DistrictDto.Request request = new DistrictDto.Request();

                request.setCompany(company);
                request.setFactory(factory);
                request.setCm16Code(info.getCm16Code());
                request.setCm16Stok(info.getCm16Stok());

                Map<String, Object> resultCodeMap = districtService.getDistrictInfo(request);

                if(resultCodeMap != null) {
                    return ResponseEntityUtil.error(StatusCode.NOT_FOUND, resultCodeMap.get("cm16Code") + "구역코드는 이미 존재하는 구역코드입니다.");
                }
            }
        }

        if (districtEditedInfo != null) {
            for (DistrictDto.Info info : districtEditedInfo) {
                info.setCompany(company);
                info.setFactory(factory);

                if (info.getCm16Stok() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "창고코드가 존재하지 않아 수정할 수 없습니다.");
                }

                if (info.getCm16Code() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "구역코드가 존재하지 않아 수정할 수 없습니다.");
                }

                if (info.getCm16Name() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "구역명이 존재하지 않아 수정할 수 없습니다.");
                }

                if (info.getCm16Lock() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "사용여부가 존재하지 않아 수정할 수 없습니다.");
                }
            }
        }

        try {
            boolean result = districtService.saveOfDistrict(districtAddedInfo, districtEditedInfo);
            if (!result) {
                return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "구역 등록에 실패하였습니다.");
            }
        } catch (Exception e) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, e.getMessage());
        }


        return ResponseEntityUtil.created("구역이 등록되었습니다.");
    }

    @Operation(summary = "구역관리 삭제", description = "구역관리 삭제")
    @DeleteMapping
    public ResponseEntity<?> deleteOfDistrict(@RequestBody Map<String, Object> params) throws Exception {

        List<DistrictDto.Info> districtDeletedInfo = JsonUtils.deserialize(params.get("districtDeletedInfo"), new TypeReference<List<DistrictDto.Info>>(){});

        UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();

        if (districtDeletedInfo != null) {
            for (DistrictDto.Info info : districtDeletedInfo) {
                info.setCompany(company);
                info.setFactory(factory);

                if (info.getCm16Stok() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "창고코드가 존재하지 않아 삭제할 수 없습니다.");
                }

                if (info.getCm16Code() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "구역코드가 존재하지 않아 삭제할 수 없습니다.");
                }
            }
        }

        if (!districtService.deleteOfDistrict(districtDeletedInfo)) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "구역 삭제에 실패하였습니다.");
        }

        return ResponseEntityUtil.created("구역이 삭제되었습니다.");
    }

    @Operation(summary = "구역관리 QR용 조회", description = "구역관리 QR용 조회")
    @GetMapping("/qr")
    public ResponseEntity<?> getDistrictQRList(DistrictDto.Request params) throws Exception {

        Map<String, Object> data = new HashMap<String, Object>();

        UserDto userInfo = auth.getUserInfo();

        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();

        params.setCompany(company);
        params.setFactory(factory);

        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
        }

        List<Map<String, Object>> districtList = districtService.getDistrictQrList(params);

        data.put("districtList", districtList);

        return ResponseEntityUtil.ok(data);
    }

}
