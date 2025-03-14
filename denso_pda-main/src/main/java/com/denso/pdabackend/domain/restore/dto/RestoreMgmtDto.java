package com.denso.pdabackend.domain.restore.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 반납요청관리 DTO
 * 생성일자 : 2025.02.18
 * 생성자 : S.Y.G
 */
public class RestoreMgmtDto {

    /*
      반납요청관리 상세 리스트 항목들
     */
    @Data
    public static class Info {
        private String st11Company;      /* 회사 */
        private String st11Factory;      /* 공장 */
        private String st11Hno;          /* 반납요청서번호 */
        private String st11No;           /* 반납요청상세번호 */
        private Integer st11Seq;         /* 상세순번 */
        private String st11Code;         /* 품목코드 */
        private String st11Lot;          /* 로트번호 */
        private double st11Qty;          /* 반납수량 */
        private String st11Stok;         /* 보관장소 */
        private String st11Dist;     /* 구역 */

        private String st11Indte;         /* 등록일자 */
        private String st11Updte;         /* 수정일자 */
        private Integer st11Empno;        /* 등록자 사번 */
        private Integer st11UpdEmpno;     /* 수정자 사번 */
    }

    /*
      반납요청 마스터 항목들
     */
    @Data
    public static class MasterInfo {
        private String st10Company;      /* 회사 */
        private String st10Factory;      /* 공장 */
        private String st10No;           /* 반납요청서번호 */
        private String st10Dat;          /* 반납요청일자 */
        private String st10Seq;          /* 순번 */
        private String st10Dept;         /* 부서코드 */
        private String st10Rmk;          /* 사유 */
        private String st10Yn;           /* 승인여부 */
        private String st10Indte;        /* 등록일자 */
        private Integer st10Empno;       /* 등록자 사번 */

        private String st10NRmk;         /* 미승인 사유 */

        private String inputMode;        /* 저장모드 */

    }

    /*
     반납요청 검색 조건
     */
    @Data
    @EqualsAndHashCode(callSuper=false)
    public static class Request extends CommonDto {
        private String company;  /* 회사 */
        private String factory;  /* 공장 */
        private String st10No;   /* 반납요청서번호 */
        private String st10Sdt;  /* 반납요청검색시작일자 */
        private String st10Edt;  /* 반납요청검색종료일자 */
        private String st10Yn;   /* 상태코드 */

        private String st02Gbn;   /* 입고코드 - 생산 : PR */
    }
    
}
