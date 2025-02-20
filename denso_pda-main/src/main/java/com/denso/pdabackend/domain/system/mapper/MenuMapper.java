package com.denso.pdabackend.domain.system.mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.system.dto.MenuDto.MenuRequest;
import com.denso.pdabackend.domain.system.dto.MenuDto.Menu;

@Mapper
public interface MenuMapper {

    List<Map<String, Object>> getMenuList(MenuRequest params);

    //List<Map<String, Object>> getSiblingMenuList(MenuRequest params);

    void saveOfMenu(Menu menuIns);

    void updateOfMenuOrder(List<Menu> menuOrderList);

    List<Map<String, Object>> getChildMenuList(MenuRequest params);

    void deleteOfMenu(MenuRequest params);

    Optional<Map<String, Object>> getMenuInfo(MenuRequest params);

    List<Map<String, Object>> getMenuOfUrl(String url);
    
}
