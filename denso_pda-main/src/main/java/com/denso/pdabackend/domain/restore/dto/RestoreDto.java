package com.denso.pdabackend.domain.restore.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

public class RestoreDto {

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
        private String st11DefLot;          /* 기존 로트번호 */
        private String st11LotSeq;          /* 로트번호 시퀀스 */
        private double st11Qty;          /* 반납수량 */
        private double st02Qty;          /* 반납등록수량(입고) */
        private String st11Stok;         /* 보관장소 */
        private String st11Dist;     /* 구역 */

        private String st11Indte;         /* 등록일자 */
        private String st11Updte;         /* 수정일자 */
        private Integer st11Empno;        /* 등록자 사번 */
        private Integer st11UpdEmpno;     /* 수정자 사번 */

        private Integer st02Seq;         /* 상세순번 - 입고 */
        private String st02Dat;        /* 등록일자 */
        private String qrcode;        /* QR 번호 - LOT에 있음 */
        private String cm08Ipunt;     /* 입고 단위 */
        private String cm08Opunt;     /* 출고 단위 */
        private String cm08Gbn;       /* 품목 구분 */
        private String cm08Dgbn;       /* 품목 상세구분 */

        private Integer st03Seq;     /* 순번 - 출고 */
        private Integer st01Seq;     /* 순번 - 재고 */
        private Integer st01LotSeq;     /* LOT 번호 시퀀스 - 재고 */

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
    }
    
}
