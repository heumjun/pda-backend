package com.denso.pdabackend.domain.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.system.dto.AuthorityDto.Authority;
import com.denso.pdabackend.domain.system.dto.AuthorityDto.AuthorityCopyRequest;
import com.denso.pdabackend.domain.system.dto.AuthorityDto.AuthorityRequest;

@Mapper
public interface AuthorityMapper {

    List<Map<String, Object>> getAuthority(AuthorityRequest params);

    void saveOfAuthority(Authority authority);

    void deleteOfAuthority(Authority authority);

    void copyOfAuthority(AuthorityCopyRequest params);

    void updateOfAuthorityUrl(AuthorityRequest params);

    void deleteAllOfAuthority(AuthorityRequest params);

}
