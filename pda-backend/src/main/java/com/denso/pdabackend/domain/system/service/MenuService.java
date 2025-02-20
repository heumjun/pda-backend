package com.denso.pdabackend.domain.system.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.denso.pdabackend.domain.system.dto.AuthorityDto;
import com.denso.pdabackend.domain.system.dto.MenuDto;
import com.denso.pdabackend.domain.system.dto.MenuDto.Menu;
import com.denso.pdabackend.domain.system.dto.MenuDto.MenuRequest;
import com.denso.pdabackend.domain.system.mapper.MenuMapper;
import com.denso.pdabackend.response.exception.BusinessException;
import com.denso.pdabackend.utils.JsonUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuMapper menuMapper;
    private final AuthorityService authorityService;

    /**
     * 메뉴코드를 포함한 자식메뉴 반환
     * @param request
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getMenuList(MenuRequest params) throws Exception{

        return menuMapper.getMenuList(params);
    }

    
    /**
     * 같은 레벨의 메뉴리스트 반환
     * @param request
     * @return
     
    public List<Map<String, Object>> getSiblingMenuList(MenuRequest params) throws Exception {
        return menuMapper.getSiblingMenuList(params);
    }
        */

    /**
     * 자식 메뉴리스트 반환
     * @param request
     * @return
     */
    public List<Map<String,Object>> getChildMenuList(MenuRequest params) throws Exception{
        return menuMapper.getChildMenuList(params);
    }

    /**
     * 메뉴정보 반환
     * @param params
     * @return
     */
    public Map<String,Object> getMenuInfo(MenuRequest params) throws Exception{
        
        return menuMapper.getMenuInfo(params).orElseThrow(()->new BusinessException("메뉴가 존재하지 않습니다."));
    }

    /**
     * 메뉴 존재여부
     * @param params
     * @return
     */
    public boolean isMenu(MenuRequest params) throws Exception{
        return menuMapper.getMenuInfo(params).isPresent();
        
    }
    /**
     * url 이 포함된 모든 메뉴코드
     * @param url
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getMenuOfUrl(String url) throws Exception{
        return menuMapper.getMenuOfUrl(url);
    }

    /**
     * 메뉴저장
     * @param menuIns 저장할 메뉴정보
     * @param menuOrderList 순서변경할 메뉴리스트
     * 
     * @apiNote
     * 1. 메뉴저장
     * 2. 메뉴순서 정렬업데이트
     * 3. 메뉴가 사용중지 할경우 자식노드 모두 사용중지
     */
    public void saveOfMenu(Menu menuIns, List<Menu> menuOrderList) throws Exception{

        if(ObjectUtils.isEmpty(menuIns.getMenUpcd())) throw new BusinessException("[상위 메뉴코드]를 입력하세요.");
        if(ObjectUtils.isEmpty(menuIns.getMenCode())) throw new BusinessException("[메뉴코드]를 입력하세요.");
        if(ObjectUtils.isEmpty(menuIns.getMenName())) throw new BusinessException("[메뉴명]를 입력하세요.");

        if(ObjectUtils.isNotEmpty(menuOrderList))
            updateOfMenuOrder(menuOrderList);

        //url값이 변경될 경우 기존 url값을 가지는 권한관리의 프로그램을 변경된 url로 변경해준다.
        String oldMenuUrl = "";
        MenuDto.MenuRequest menuRequestDto = MenuDto.MenuRequest.builder().menuCode(menuIns.getMenCode()).build();
        Optional<Map<String, Object>> oldMenuInfoMap =  menuMapper.getMenuInfo(menuRequestDto);
        if(oldMenuInfoMap.isPresent()){
            if(ObjectUtils.isNotEmpty(oldMenuInfoMap.get().get("menUrl")))
                oldMenuUrl = oldMenuInfoMap.get().get("menUrl").toString();
        }
        if(!oldMenuUrl.equals("") && !oldMenuUrl.equals(menuIns.getMenUrl())){
            AuthorityDto.AuthorityRequest authorityRequestDto = AuthorityDto.AuthorityRequest.builder()
                                                                            .menuUrl(menuIns.getMenUrl())
                                                                            .menuOldUrl(oldMenuUrl)
                                                                            .build();
            authorityService.updateOfAuthorityUrl(authorityRequestDto);
        }

        menuMapper.saveOfMenu(menuIns);

        if(menuIns.getMenLock().equalsIgnoreCase("Y")){
            MenuDto.MenuRequest request = MenuDto.MenuRequest.builder().menuCode(menuIns.getMenCode()).build();
            List<MenuDto.Menu> childMenuList = JsonUtils.deserialize(getChildMenuList(request), new TypeReference<List<MenuDto.Menu>>() {});

            for(MenuDto.Menu menu:childMenuList){
                menu.setMenLock("Y");
                menuMapper.saveOfMenu(menu);
            }
        }
        
    }

    /**
     * 메뉴순서 업데이트
     * @param menuOrderList 순서변경할 메뉴리스트
     */
    public void updateOfMenuOrder(List<Menu> menuOrderList) throws Exception{
        menuMapper.updateOfMenuOrder(menuOrderList);
    }

    /**
     * 메뉴 삭제
     * @param params
     * @apiNote
     * 1. 메뉴삭제
     * 2. 같은 노드의 메뉴들 순번 재정렬
     * 3. 삭제후 같은 url를 가지는 메뉴가 존재하지 않을경우 권한삭제
     */
    public void deleteOfMenu(MenuRequest params) throws Exception {
        if(ObjectUtils.isEmpty(params.getMenuCode())) throw new BusinessException("메뉴코드를 입력하세요.");
        if(params.getMenuCode().equalsIgnoreCase("ROOT")) throw new BusinessException("최상위 메뉴는 삭제 할 수 없습니다.");

        MenuDto.Menu menuInfo = JsonUtils.deserialize(getMenuInfo(params), MenuDto.Menu.class);
        if(getChildMenuList(params).stream().count()>0) throw new BusinessException("자식 메뉴가 존재하여 삭제가 불가능 합니다.");


        //1. 메뉴삭제
        menuMapper.deleteOfMenu(params);

        //2. 같은 노드의 메뉴들 순번 재정렬
        params.setMenuCode(menuInfo.getMenUpcd());
        List<MenuDto.Menu> menuOrderList = JsonUtils.deserialize(getChildMenuList(params), new TypeReference<List<MenuDto.Menu>>() {});
        IntStream.range(0, menuOrderList.size())
                 .forEach(i->menuOrderList.get(i).setMenSeq(i+1));
        
        if(ObjectUtils.isNotEmpty(menuOrderList))
            menuMapper.updateOfMenuOrder(menuOrderList);

        //3. 해당메뉴 삭제 후 같은 url을 가지는 메뉴가 존재 하지 않을 경우 권한도 삭제한다.
        if(ObjectUtils.isNotEmpty(menuInfo.getMenUrl())){
            if(getMenuOfUrl(menuInfo.getMenUrl()).stream().count()<1){
                AuthorityDto.AuthorityRequest authorityRequest = AuthorityDto.AuthorityRequest.builder().menuUrl(menuInfo.getMenUrl()).build();
                authorityService.deleteAllOfAuthority(authorityRequest);
            }
        }
        
        

    }
    

    
}
