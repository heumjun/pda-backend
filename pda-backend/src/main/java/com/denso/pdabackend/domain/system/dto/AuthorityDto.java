package com.denso.pdabackend.domain.system.dto;

import java.util.List;

import com.denso.pdabackend.common.CommonDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class AuthorityDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Authority{
        //지점
        private String athJan;
        //메뉴경로
        private String athUrl;
        //트레이너id
        private String athId;
        //등록권한
        private String athIns;
        //삭제권한
        private String athDel;
        //락여부
        private String athLock;
        private String athIndte;
        private String athUpdte;
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor  //superBuilder 를 사용해서 상속받은 부모클래스도 builder를 사용하고자 할때 @NoArgsConstructor(기본생성자) 를 추가해야함.
    @EqualsAndHashCode(callSuper = false)
    public static class AuthorityRequest extends CommonDto{
        //권한자 id
        private String id;
        //메뉴ril
        private String menuUrl;     
        //메뉴url이 변경될 경우 권한url변경이 필요하기에 기존url값을 가지는 변수 updateOfAuthorityUrl에서 사용
        private String menuOldUrl;
    }

    /**
     * 권한복사 request DTO
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor  //superBuilder 를 사용해서 상속받은 부모클래스도 builder를 사용하고자 할때 @NoArgsConstructor(기본생성자) 를 추가해야함.
    @EqualsAndHashCode(callSuper = false)
    public static class AuthorityCopyRequest extends CommonDto {

        private String id;        //복사대상
        private List<String> copyList;     //권한 복사시 복사 적용대상
        
    }
    
}
