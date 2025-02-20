package com.denso.pdabackend.domain.codeManage.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class TrainerDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrainerInfo{
        //지점
        private String traJan;
        //아이디
        private String traId;
        //패스워드
        private String traPw;
        //이름
        private String traName;
        //생년월일
        private String traBirth;
        //입사일자
        private String traEntdt;
        //퇴사일자
        private String traTaedt;
        //주소
        private String traAddress1;
        //상세주소
        private String traAddress2;
        //남녀구분
        private String traSex;
        //전화번호
        private String traPhone;
        //이메일
        private String traEmail;
        //리마크
        private String traRmk;
        //등록일자
        private String traIndte;
        //수정일자
        private String traUpdte;
        //등록자
        private String traRegid;

        //암호화 되기전 비밀번호(신규입사자 임시비밀번호)
        private String password;
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor  //superBuilder 를 사용해서 상속받은 부모클래스도 builder를 사용하고자 할때 @NoArgsConstructor(기본생성자) 를 추가해야함.
    @EqualsAndHashCode(callSuper = false)
    public static class TrainerRequest extends CommonDto{
        private String searchName;
        private String searchStartEntdt;
        private String searchEndEntdt;
        private String searchStartTaedt;
        private String searchEndTaedt;

        private String id;
        private Boolean excludeQuit;        //퇴사자 제외        
    }
    
}
