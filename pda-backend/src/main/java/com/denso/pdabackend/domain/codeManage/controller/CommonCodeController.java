package com.denso.pdabackend.domain.codeManage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.common.AuthenticationFacade;
import com.denso.pdabackend.domain.codeManage.dto.CommonCodeDto;
import com.denso.pdabackend.domain.codeManage.dto.CommonCodeDto.CommonCodeRequest;
import com.denso.pdabackend.domain.codeManage.service.CommonCodeService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("code-manage/common-codes")
public class CommonCodeController {
    private final AuthenticationFacade auth;
    private final CommonCodeService commonCodeService;

    @GetMapping("group")
    @Operation(summary = "공통코드 리스트", description = "공통코드의 상위그룹코드 리스트")
    public ResponseEntity<?> getGroupCodeList() throws Exception{

        String jan = auth.getUserInfo().getJan();
        CommonCodeRequest params = CommonCodeRequest.builder().jan(jan).build();

        Map<String,Object> data = new HashMap<String,Object>();
        List<Map<String,Object>> groupCodeList = commonCodeService.getGroupCodeList(params);
        data.put("groupCodeList", groupCodeList);

        return ResponseEntityUtil.ok(data);
    }

    @PostMapping("group")
    @Operation(summary = "공통코드 저장", description = "그룹코드 저장")
    public ResponseEntity<?> saveOfGroupCode(@RequestBody Map<String,Object> params) throws Exception{
        log.debug("{}", params);

        String id = auth.getUserInfo().getUserId();
        String jan = auth.getUserInfo().getJan();

        List<CommonCodeDto.CommonHead> saveList = JsonUtils.deserialize(params.get("saveList"), new TypeReference<List<CommonCodeDto.CommonHead>>() {});
        saveList.forEach(f->{
            f.setCohRegid(id);
            f.setCohJan(jan);
        });

        commonCodeService.saveOfGroupCode(saveList);

        return ResponseEntityUtil.created(null);
    }

    @DeleteMapping("group")
    @Operation(summary = "공통코드 삭제", description = "DB에서 삭제됨")
    public ResponseEntity<?> deleteOfGroupCode(@RequestBody Map<String,Object> params) throws Exception{
        log.debug("{}", params);
        String jan = auth.getUserInfo().getJan();
        
        List<CommonCodeDto.CommonHead> deleteList = JsonUtils.deserialize(params.get("deleteList"), new TypeReference<List<CommonCodeDto.CommonHead>>() {});
        deleteList.forEach(item->item.setCohJan(jan));

        commonCodeService.deleteOfGroupCode(deleteList);

        return ResponseEntityUtil.created(null);

    }

    @GetMapping("{groupCode}")
    @Operation(summary = "공통코드 리스트", description = "파라미터로 전달받은 그룹코드의 공통코드 리스트, lock걸린코드 제외시 unLock=true 파라미터 전달요망")
    public ResponseEntity<?> getCommonCodeList(@PathVariable String groupCode,@RequestParam(required = false) Boolean unLock) throws Exception{
        Map<String,Object> data = new HashMap<String,Object>();

        List<Map<String,Object>> commonCodeList = commonCodeService.getCommonCodeList(groupCode);

        //락걸린코드 제외시
        if(ObjectUtils.isEmpty(unLock)) unLock = false;
        if(unLock){
            commonCodeList = commonCodeList.stream().filter(f->f.get("codLock").equals("N")).toList();
        }


        data.put("commonCodeList", commonCodeList);

        return ResponseEntityUtil.ok(data);
    }

    @PostMapping
    @Operation(summary = "공통코드 저장", description = "공통코드 저장")
    public ResponseEntity<?> saveOfCommonCode(@RequestBody Map<String,Object> params) throws Exception{

        log.debug("{}", params);
        String id = auth.getUserInfo().getUserId();
        String jan = auth.getUserInfo().getJan();
        
        List<CommonCodeDto.CommonDesc> saveList = JsonUtils.deserialize(params.get("saveList"), new TypeReference<List<CommonCodeDto.CommonDesc>>() {});
        saveList.forEach(f->{
            f.setCodRegid(id);
            f.setCodJan(jan);
        });

        commonCodeService.saveOfCommonCode(saveList);

        return ResponseEntityUtil.created(null);
    }

    @DeleteMapping
    @Operation(summary = "공통코드 삭제", description = "DB에서 삭제됨")
    public ResponseEntity<?> deleteOfCommonCode(@RequestBody Map<String,Object> params) throws Exception{

        log.debug("{}", params);
        String jan = auth.getUserInfo().getJan();
        
        List<CommonCodeDto.CommonDesc> deleteList = JsonUtils.deserialize(params.get("deleteList"), new TypeReference<List<CommonCodeDto.CommonDesc>>() {});
        deleteList.forEach(f->f.setCodJan(jan));
        
        commonCodeService.deleteOfCommonCode(deleteList);

        return ResponseEntityUtil.created(null);

    }

    

    
}
