package com.denso.pdabackend.domain.material.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.material.dto.MaterialDto.Material;
import com.denso.pdabackend.domain.material.dto.MaterialDto.MaterialRequest;
import com.denso.pdabackend.domain.material.mapper.MaterialMapper;
import com.denso.pdabackend.response.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialService {
    
    private final MaterialMapper materialMapper;

    /**
     * 재고리스트
     * @param params
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getMaterialList(MaterialRequest params) throws Exception {
        return materialMapper.getMaterialList(params);
    }

    /**
     * 코드 자동완성
     * @param code
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getAutocompleteCode(String code) throws Exception {
        if(ObjectUtils.isEmpty(code)) throw new BusinessException("코드를 입력하세요.");
        return materialMapper.getAutocompleteCode(code);
    }

    /**
     * 재고 정보
     * @param params
     * @return
     * @throws Exception
     */
    public Map<String,Object> getMaterial(MaterialRequest params) throws Exception{
        if(ObjectUtils.isEmpty(params.getCode())) throw new BusinessException("코드를 입력하세요.");
        if(ObjectUtils.isEmpty(params.getJil())) throw new BusinessException("재질을 입력하세요.");
        if(ObjectUtils.isEmpty(params.getHcd())) throw new BusinessException("HCD를 입력하세요.");

        return materialMapper.getMaterial(params);
    }

    /**
     *  재고 등록
     * @param insertList
     * @param updateList
     * @throws Exception
     */
    public void saveOfMaterial(List<Material> insertList, List<Material> updateList) throws Exception {

        //신규코드인데 코드가 존재하는지 확인
        for(Material item:insertList){
            if(ObjectUtils.isEmpty(item.getMatCode())) throw new BusinessException("코드가 존재하지 않습니다.");
            if(ObjectUtils.isEmpty(item.getMatJil())) throw new BusinessException("재질가 존재하지 않습니다.");
            if(ObjectUtils.isEmpty(item.getMatHcd())) throw new BusinessException("HCD가 존재하지 않습니다.");

            MaterialRequest materialRequest= MaterialRequest.builder().code(item.getMatCode())
                                                                    .jil(item.getMatJil())
                                                                    .hcd(item.getMatHcd())
                                                                    .build();
            if(ObjectUtils.isNotEmpty(getMaterial(materialRequest))){
                throw new BusinessException("신규코드 인데 코드가 존재합니다.");
            }
        }

        List<Material> saveList = Stream.concat(insertList.stream(),updateList.stream()).collect(Collectors.toList());

        for(Material item:saveList){
            materialMapper.saveOfMaterial(item);
        }
        

    }
    /**
     * 재고 삭제
     * @param deleteList
     */
    public void delteOfMaterial(List<Material> deleteList) throws Exception {
        
        for(Material item:deleteList){
            if(ObjectUtils.isEmpty(item.getMatCode())) throw new BusinessException("코드가 존재하지 않습니다.");
            if(ObjectUtils.isEmpty(item.getMatJil())) throw new BusinessException("재질가 존재하지 않습니다.");
            if(ObjectUtils.isEmpty(item.getMatHcd())) throw new BusinessException("HCD가 존재하지 않습니다.");
        }

        for(Material item:deleteList){
            materialMapper.deleteOfMaterial(item);
        }

    }

}
