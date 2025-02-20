package com.denso.pdabackend.domain.system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.domain.system.dto.MenuDto;
import com.denso.pdabackend.domain.system.service.MenuService;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.response.exception.BusinessException;
import com.denso.pdabackend.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("system/menus")
public class MenuController {
    
    private final MenuService menuService;

    @GetMapping
    @Operation(summary = "전체 메뉴리스트 반환", description = "전체 메뉴리스트 반환(사용중지코드 포함)")
    public ResponseEntity<?> getAllMenuList() throws Exception {
        Map<String,Object> data = new HashMap<String,Object>();

        MenuDto.MenuRequest request = MenuDto.MenuRequest.builder()
                                             .menuCode("ROOT")
                                             .lock(true).build();
        
        List<Map<String,Object>> menuList = menuService.getMenuList(request);
        data.put("menuList", menuList);

        return ResponseEntityUtil.ok(data);
    }

    @GetMapping("{menuCode}")
    @Operation(summary = "메뉴정보 반환", description = "파라미터 코드의 메뉴코드 정보 반환")
    public ResponseEntity<?> getMenuInfo(@PathVariable String menuCode) throws Exception{
        Map<String,Object> data = new HashMap<String,Object>();
        
        MenuDto.MenuRequest params = MenuDto.MenuRequest.builder()
                                             .menuCode(menuCode)
                                             .lock(false).build();
        
        data.put("menuInfo", menuService.getMenuInfo(params));

        return ResponseEntityUtil.ok(data);
    }

    @PostMapping
    @Operation(summary = "메뉴저장", description = "메뉴저장 및 수정 순번변경")
    public ResponseEntity<?> saveOfMenu(@RequestBody Map<String,Object> params) throws Exception{

        log.debug("{}",params);
        //등록할 메뉴정보
        MenuDto.Menu menuIns = JsonUtils.deserialize(params.get("menuInfo"), MenuDto.Menu.class);

        //신규/수정여부 파라미터 확인
        if(ObjectUtils.isEmpty(params.get("isUpdateMode"))) throw new BusinessException("isUpdateMode(수정여부) 파라미터가 없습니다.");
        
        //신규등록일경우 사용중인코드인지 확인
        if(!(boolean) params.get("isUpdateMode")){
            MenuDto.MenuRequest menuRequest = MenuDto.MenuRequest.builder().menuCode(menuIns.getMenCode()).build();
            if(menuService.isMenu(menuRequest)) throw new BusinessException("이미 사용중인 [메뉴코드] 입니다.");
        }
        
        //메뉴 순서 리스트
        List<MenuDto.Menu> menuOrderList = JsonUtils.deserialize(params.get("menuOrderList"), new TypeReference<List<MenuDto.Menu>>() {});
        menuService.saveOfMenu(menuIns,menuOrderList);
        
        return ResponseEntityUtil.created(null);   
    }

    
    @DeleteMapping
    @Operation(summary = "메뉴삭제", description = "메뉴삭제")
    public ResponseEntity<?> deleteOfMenu(@RequestBody MenuDto.MenuRequest params) throws Exception{
        log.debug("{}",params);

        menuService.deleteOfMenu(params);

        return ResponseEntityUtil.created(null);   
    }

    @GetMapping("{menuCode}/children")
    @Operation(summary = "메뉴코드별 메뉴리스트", description = "파라미터 코드를 포함한 자식메뉴 반환")
    public ResponseEntity<?> getMenuList(@PathVariable String menuCode) throws Exception{

        Map<String,Object> data = new HashMap<String,Object>();
        MenuDto.MenuRequest params = MenuDto.MenuRequest.builder()
                                             .menuCode(menuCode)
                                             .lock(false).build();
        
        List<Map<String,Object>> menuList = menuService.getMenuList(params);
        data.put("menuList", menuList);

        return ResponseEntityUtil.ok(data);
    }

    // @GetMapping("{menuCode}/siblings")
    // @Operation(summary = "선택한 메뉴와 같은 레벨의 메뉴 반환", description = "같은 레벨의 메뉴반환(순서변경시 사용)")
    // public ResponseEntity<?> getSiblingMenuList(@PathVariable String menuCode) throws Exception{

    //     Map<String,Object> data = new HashMap<String,Object>();

    //     MenuDto.MenuRequest params = MenuDto.MenuRequest.builder()
    //                                          .menuCode(menuCode)
    //                                          .lock(false).build();
        
    //     List<Map<String,Object>> menuList = menuService.getSiblingMenuList(params);
    //     data.put("menuList", menuList);

    //     return ResponseEntityUtil.ok(data);
    // }

    
}
