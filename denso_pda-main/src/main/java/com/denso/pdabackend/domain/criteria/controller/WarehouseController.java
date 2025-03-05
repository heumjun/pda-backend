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
import com.denso.pdabackend.domain.criteria.dto.WarehouseDto;
import com.denso.pdabackend.domain.criteria.service.WarehouseService;
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
@RequestMapping("criteria/warehouse")
@Slf4j
public class WarehouseController {

    private final AuthenticationFacade auth;  //토큰인증된 사용자정보관리
    private final WarehouseService warehouseService;

    @Operation(summary = "창고관리조회", description = "창고관리조회")
    @GetMapping()
    public ResponseEntity<?> getWarehouseList(WarehouseDto.Request params) throws Exception {

        Map<String, Object> data = new HashMap<String, Object>();

        UserDto userInfo = auth.getUserInfo();

        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();

//        params.setCompany(company);
//        params.setFactory(factory);
        
        params.setCompany("DNKR");
        params.setFactory("0001");

        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않아 조회할 수 없습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않아 조회할 수 없습니다.");
        }

        List<Map<String, Object>> warehouseList = warehouseService.getWarehouseList(params);

        data.put("warehouseList", warehouseList);

        return ResponseEntityUtil.ok(data);
    }

    @Operation(summary = "창고관리", description = "창고관리")
    @GetMapping("/detail")
    public ResponseEntity<?> getWarehouseInfo(WarehouseDto.Request params) throws Exception {

        Map<String, Object> data = new HashMap<String, Object>();

        UserDto userInfo = auth.getUserInfo();

        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        String code = params.getCm15Code();

//        params.setCompany(company);
//        params.setFactory(factory);
        params.setCompany("DNKR");
        params.setFactory("0001");

        if (company == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "회사정보가 존재하지 않습니다.");
        }

        if (factory == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "공장코드가 존재하지 않습니다.");
        }

        if (code == null) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "창고코드가 존재하지 않습니다.");
        }

        Map<String, Object> warehouseInfo = warehouseService.getWarehouseInfo(params);

        String appCheck;
        if(warehouseService.getWarehouseCheck(params)) {
            appCheck = "false"; // 정상 창고코드
        } else {
            appCheck = "error"; // 창고코드 없음 OR 창고코드 미사용
        }

        data.put("warehouseInfo", warehouseInfo);
        data.put("appCheck", appCheck);

        return ResponseEntityUtil.ok(data);
    }

    @Operation(summary = "창고관리 등록", description = "창고관리 등록")
    @PostMapping
    public ResponseEntity<?> saveOfWarehouse(@RequestBody Map<String, Object> params) throws Exception {

        List<WarehouseDto.Info> warehouseAddedInfo = JsonUtils.deserialize(params.get("warehouseAddedInfo"), new TypeReference<List<WarehouseDto.Info>>(){});
        List<WarehouseDto.Info> warehouseEditedInfo = JsonUtils.deserialize(params.get("warehouseEditedInfo"), new TypeReference<List<WarehouseDto.Info>>(){});

        UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        company = "DNKR";
        factory = "0001";

        if (warehouseAddedInfo != null) {
            for (WarehouseDto.Info info : warehouseAddedInfo) {
                info.setCompany(company);
                info.setFactory(factory);

                if (info.getCm15Code() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "창고코드가 존재하지 않아 수정할 수 없습니다.");
                }

                if (info.getCm15Name() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "창고명이 존재하지 않아 수정할 수 없습니다.");
                }

                if (info.getCm15Lock() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "사용여부가 존재하지 않아 수정할 수 없습니다.");
                }

                WarehouseDto.Request request = new WarehouseDto.Request();

                request.setCompany(company);
                request.setFactory(factory);
                request.setCm15Code(info.getCm15Code());

                Map<String, Object> resultCodeMap = warehouseService.getWarehouseInfo(request);

                if(resultCodeMap != null) {
                    return ResponseEntityUtil.error(StatusCode.NOT_FOUND, resultCodeMap.get("cm15Code") + "창고코드는 이미 존재하는 창고코드입니다.");
                }
            }
        }

        if (warehouseEditedInfo != null) {
            for (WarehouseDto.Info info : warehouseEditedInfo) {
                info.setCompany(company);
                info.setFactory(factory);

                if (info.getCm15Code() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "창고코드가 존재하지 않아 수정할 수 없습니다.");
                }

                if (info.getCm15Name() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "창고명이 존재하지 않아 수정할 수 없습니다.");
                }

                if (info.getCm15Lock() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "사용여부가 존재하지 않아 수정할 수 없습니다.");
                }
            }
        }

        try {
            boolean result = warehouseService.saveOfWarehouse(warehouseAddedInfo, warehouseEditedInfo);
            if (!result) {
                return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "창고 등록에 실패하였습니다.");
            }
        } catch (Exception e) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, e.getMessage());
        }


        return ResponseEntityUtil.created("창고가 등록되었습니다.");
    }

    @Operation(summary = "창고관리 삭제", description = "창고관리 삭제")
    @DeleteMapping
    public ResponseEntity<?> deleteOfWarehouse(@RequestBody Map<String, Object> params) throws Exception {

        List<WarehouseDto.Info> warehouseDeletedInfo = JsonUtils.deserialize(params.get("warehouseDeletedInfo"), new TypeReference<List<WarehouseDto.Info>>(){});

        UserDto userInfo = auth.getUserInfo();
        String company = userInfo.getCompany();
        String factory = userInfo.getFactory();
        company = "DNKR";
        factory = "0001";

        if (warehouseDeletedInfo != null) {
            for (WarehouseDto.Info info : warehouseDeletedInfo) {
                info.setCompany(company);
                info.setFactory(factory);

                if (info.getCm15Code() == null) {
                    return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "창고코드가 존재하지 않아 삭제할 수 없습니다.");
                }
            }
        }

        if (!warehouseService.deleteOfWarehouse(warehouseDeletedInfo)) {
            return ResponseEntityUtil.error(StatusCode.NO_CONTENT, "창고 삭제에 실패하였습니다.");
        }

        return ResponseEntityUtil.created("창고가 삭제되었습니다.");
    }
}
