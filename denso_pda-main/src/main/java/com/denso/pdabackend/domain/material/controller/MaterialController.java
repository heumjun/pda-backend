package com.denso.pdabackend.domain.material.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.domain.material.dto.MaterialDto;
import com.denso.pdabackend.domain.material.service.MaterialService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("materials")
public class MaterialController {

    private final MaterialService materialService;

    @GetMapping
    @Operation(summary = "자재 리스트", description = "자재")
    public ResponseEntity<?> getMaterialList(MaterialDto.MaterialRequest params) throws Exception {
        Map<String,Object> data = new HashMap<String,Object>();

        List<Map<String,Object>> materialList = materialService.getMaterialList(params);
        data.put("materialList", materialList);

        return ResponseEntityUtil.ok(data);
    }

    @GetMapping("info")
    @Operation(summary = "자재 정보", description = "자재재고 정보")
    public ResponseEntity<?> getMaterial(MaterialDto.MaterialRequest params) throws Exception {

        Map<String,Object> data = materialService.getMaterial(params);
        
        return ResponseEntityUtil.ok(data);
    }
    

    @PostMapping
    @Operation(summary = "자재 저장", description = "자재재고 저장")
    public ResponseEntity<?> saveOfMaterial(@RequestBody Map<String,Object> params) throws Exception {
        log.debug("{}",params);
        //신규데이터
        List<MaterialDto.Material> insertList = JsonUtils.deserialize(params.get("insertList"), new TypeReference<List<MaterialDto.Material>>() {});
        //수정데이터
        List<MaterialDto.Material> updateList = JsonUtils.deserialize(params.get("updateList"), new TypeReference<List<MaterialDto.Material>>() {});

        materialService.saveOfMaterial(insertList,updateList);
        
        return ResponseEntityUtil.created(null);
    }
    
    @DeleteMapping
    @Operation(summary = "자재 삭제", description = "자재재고 삭제")
    public ResponseEntity<?> deleteOfMaterial(@RequestBody Map<String,Object> params) throws Exception {
        log.debug("{}",params);
        //신규데이터
        List<MaterialDto.Material> deleteList = JsonUtils.deserialize(params.get("deleteList"), new TypeReference<List<MaterialDto.Material>>() {});
        
        materialService.delteOfMaterial(deleteList);
        
        return ResponseEntityUtil.created(null);
    }


    @GetMapping("autocomplete/{code}")
    @Operation(summary = "자재코드 자동완성", description = "코드 자동완성")
    public ResponseEntity<?> getAutocompleteCode(@PathVariable String code) throws Exception {
        
        List<Map<String,Object>> codeList = materialService.getAutocompleteCode(code);
        
        return ResponseEntityUtil.ok(codeList);
    }
    

}
