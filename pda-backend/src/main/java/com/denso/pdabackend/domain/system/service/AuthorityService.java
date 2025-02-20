package com.denso.pdabackend.domain.system.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.system.dto.AuthorityDto.Authority;
import com.denso.pdabackend.domain.system.dto.AuthorityDto.AuthorityCopyRequest;
import com.denso.pdabackend.domain.system.dto.AuthorityDto.AuthorityRequest;
import com.denso.pdabackend.domain.system.mapper.AuthorityMapper;
import com.denso.pdabackend.response.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityMapper authorityMapper;
    /**
     * url 권한 리스트
     * @param params
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getAuthority(AuthorityRequest params) throws Exception {
        return authorityMapper.getAuthority(params);
    }

    /**
     * 권한 저장
     * @param saveList
     * @throws Exception
     */
    public void saveOfAuthority(List<Authority> saveList) throws Exception{
        if(ObjectUtils.isEmpty(saveList)) throw new BusinessException("저장할 내역이 없습니다.");

        for(Authority authority:saveList){
            if(ObjectUtils.isEmpty(authority.getAthJan())) throw new BusinessException("지점 코드가 없습니다.");
            if(ObjectUtils.isEmpty(authority.getAthUrl())) throw new BusinessException("권한 메뉴URL이 없습니다.");
            if(ObjectUtils.isEmpty(authority.getAthId())) throw new BusinessException("권한자 ID가 없습니다.");
        }

        long sameCnt = saveList.stream()
                              .collect(Collectors.groupingBy(Authority::getAthId,Collectors.counting()))
                              .values().stream().filter(f->f>1).count();
                                
        if(sameCnt > 0){
            throw new BusinessException("내역중 중복된 내역이 존재합니다.");
        }

        for(Authority authority:saveList){
            authorityMapper.saveOfAuthority(authority);
        }
        
        
    }
    /**
     * 권한 삭제제
     * @param deleteList
     */
    public void deleteOfAuthority(List<Authority> deleteList) {
        if(ObjectUtils.isEmpty(deleteList)) throw new BusinessException("삭제할 내역이 없습니다.");

        for(Authority authority:deleteList){
            if(ObjectUtils.isEmpty(authority.getAthJan())) throw new BusinessException("지점 코드가 없습니다.");
            if(ObjectUtils.isEmpty(authority.getAthUrl())) throw new BusinessException("권한 메뉴URL이 없습니다.");
            if(ObjectUtils.isEmpty(authority.getAthId())) throw new BusinessException("권한자 ID가 없습니다.");
        }

        for(Authority authority:deleteList){
            authorityMapper.deleteOfAuthority(authority);
        }

    }
    /**
     * 권한 복사
     * @param params
     */
    public void copyOfAuthority(AuthorityCopyRequest params) throws Exception {

        if(ObjectUtils.isEmpty(params.getJan())) throw new BusinessException("지점 코드가 없습니다.");
        if(ObjectUtils.isEmpty(params.getId())) throw new BusinessException("권한 복사 대상 사번이 없습니다.");
        if(ObjectUtils.isEmpty(params.getCopyList())) throw new BusinessException("권한 복사 적용자 내역이 없습니다.");

        authorityMapper.copyOfAuthority(params);
    }
    /**
     * 해당 페이지의 관리자 권한 반환환
     * @param params
     * @return
     * @throws Exception
     */
    public Map<String, Object> getAuthorityInfo(AuthorityRequest params) throws Exception{
        List<Map<String, Object>> authorityList = authorityMapper.getAuthority(params);
        if(authorityList.size()>0) return authorityList.get(0);

        return null;
    }

    /**
     * 권한 url 변경
     * 메뉴에서 url변경될 경우 권한키(mna_url)도 함께 변경해준다.
     * @param params
     * @throws Exception
     */
    public void updateOfAuthorityUrl(AuthorityRequest params) throws Exception{
        if(ObjectUtils.isEmpty(params.getMenuOldUrl())) throw new BusinessException("변경전 URL이 없습니다.");
        if(ObjectUtils.isEmpty(params.getMenuUrl())) throw new BusinessException("변경후 URL이 없습니다.");
        authorityMapper.updateOfAuthorityUrl(params);
    }

    /**
     * 권한 전체 삭제
     * @param params
     * @throws Exception
     */
    public void deleteAllOfAuthority(AuthorityRequest params) throws Exception{
        if(ObjectUtils.isEmpty(params.getMenuUrl())) throw new BusinessException("URL이 없습니다.");
        authorityMapper.deleteAllOfAuthority(params);
    }
}
