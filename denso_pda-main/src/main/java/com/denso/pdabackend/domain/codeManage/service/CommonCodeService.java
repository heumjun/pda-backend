package com.denso.pdabackend.domain.codeManage.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.codeManage.dto.CommonCodeDto.CommonCodeRequest;
import com.denso.pdabackend.domain.codeManage.dto.CommonCodeDto.CommonDesc;
import com.denso.pdabackend.domain.codeManage.dto.CommonCodeDto.CommonHead;
import com.denso.pdabackend.domain.codeManage.mapper.CommonCodeMapper;
import com.denso.pdabackend.response.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonCodeService {

    private final CommonCodeMapper commonCodeMapper;
    /**
     * 공통코드 그룹리스트
     * @param params 
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getGroupCodeList(CommonCodeRequest params) throws Exception {
        return commonCodeMapper.getGroupCodeList(params);
    }

    /**
     * 공통코드 그룹 저장
     * @param saveList
     */
    public void saveOfGroupCode(List<CommonHead> saveList) throws Exception {
        
        if(ObjectUtils.isEmpty(saveList)) throw new BusinessException("저장할 내역이 없습니다.");

        for(CommonHead commonHead:saveList){
            if(ObjectUtils.isEmpty(commonHead.getCohCode())){
                throw new BusinessException("공통코드가 없는 내역이 있습니다.");
            }

            if(ObjectUtils.isEmpty(commonHead.getCohName())){
                throw new BusinessException("공통코드명이 없는 내역이 있습니다.");
            }

            if(saveList.stream().filter(f->f.getCohCode().equals(commonHead.getCohCode())).count() >1)
                throw new BusinessException("저장할 내역 중 공통코드가 중복됩니다.");
        }

        for(CommonHead commonHead:saveList){
            commonCodeMapper.saveOfGroupCode(commonHead);
        }
        

    }
    /**
     * 그룹코드 삭제
     * @param deleteList
     */
    public void deleteOfGroupCode(List<CommonHead> deleteList) {
        if(ObjectUtils.isEmpty(deleteList)) throw new BusinessException("삭제할 내역이 없습니다.");

        for(CommonHead commonHead:deleteList){
            if(ObjectUtils.isEmpty(commonHead.getCohJan()))
                throw new BusinessException("지점코드가 존재하지 않습니다.");
            if(ObjectUtils.isEmpty(commonHead.getCohCode()))
                throw new BusinessException("공통코드가 존재하지 않습니다.");
            if(commonCodeMapper.getCommonCodeList(commonHead.getCohCode()).stream().count()>0)
                throw new BusinessException("등록된 공통코드가 존재합니다. 코드("+commonHead.getCohCode()+")");
        }
        for(CommonHead commonHead:deleteList){
            commonCodeMapper.deleteOfGroupCode(commonHead);
        }

    }
    /**
     * 공통코드 리스트
     * @param groupCode
     * @param lock
     * @return
     */
    public List<Map<String, Object>> getCommonCodeList(String groupCode) {
        
        if(ObjectUtils.isEmpty(groupCode)) throw new BusinessException("공통코드를 입력하세요.");

        return commonCodeMapper.getCommonCodeList(groupCode);
    }

    /**
     * 공통코드 저장
     * @param saveList
     */
    public void saveOfCommonCode(List<CommonDesc> saveList) {
        if(ObjectUtils.isEmpty(saveList)) throw new BusinessException("저장할 내역이 없습니다.");

        for(CommonDesc commonDesc:saveList){
            if(ObjectUtils.isEmpty(commonDesc.getCodJan())){
                throw new BusinessException("지점 코드가 없는 내역이 있습니다.");
            }
            if(ObjectUtils.isEmpty(commonDesc.getCodHcode())){
                throw new BusinessException("공통 코드가 없는 내역이 있습니다.");
            }
            if(ObjectUtils.isEmpty(commonDesc.getCodCode())){
                throw new BusinessException("상세 코드가 없는 내역이 있습니다.");
            }

            if(ObjectUtils.isEmpty(commonDesc.getCodName())){
                throw new BusinessException("상세 코드명이 없는 내역이 있습니다.");
            }

            if(saveList.stream().filter(f->f.getCodCode().equals(commonDesc.getCodCode())).count() >1)
                throw new BusinessException("저장할 내역 중 상세코드가 중복됩니다.");

            if(ObjectUtils.isEmpty(commonDesc.getCodLock())){
                commonDesc.setCodLock("N");
            }
        }

        for(CommonDesc commonDesc:saveList){
            commonCodeMapper.saveOfCommonCode(commonDesc);
        }

        //전체 순번 재정렬
        
        CommonCodeRequest request = CommonCodeRequest.builder().jan(saveList.get(0).getCodJan())
                                        .groupCode(saveList.get(0).getCodHcode())
                                        .build();
        commonCodeMapper.updateOfCommonCodeOrder(request);

        

    }

    public void deleteOfCommonCode(List<CommonDesc> deleteList) throws Exception {
        
        if(ObjectUtils.isEmpty(deleteList)) throw new BusinessException("삭제할 내역이 없습니다.");

        for(CommonDesc commonDesc:deleteList){
            if(ObjectUtils.isEmpty(commonDesc.getCodJan()))
            throw new BusinessException("지점코드가 존재하지 않습니다.");
            if(ObjectUtils.isEmpty(commonDesc.getCodHcode()))
                throw new BusinessException("공통코드가 존재하지 않습니다.");
            if(ObjectUtils.isEmpty(commonDesc.getCodCode()))
                throw new BusinessException("상세코드가 존재하지 않습니다.");
        }

        for(CommonDesc commonDesc:deleteList){
            commonCodeMapper.deleteOfCommonCode(commonDesc);
        }

        //전체 순번 재정렬
        CommonCodeRequest request = CommonCodeRequest.builder().jan(deleteList.get(0).getCodJan())
                                        .groupCode(deleteList.get(0).getCodHcode())
                                        .build();

        commonCodeMapper.updateOfCommonCodeOrder(request);
        
    }
    
    /**
     * 오라클 테이블 리스트
     * @param code 
     * @param code
     * @return
     */
    public List<Map<String, Object>> getTableList(String code) {
        return commonCodeMapper.getTableList(code);
    }

    
    
    
}
