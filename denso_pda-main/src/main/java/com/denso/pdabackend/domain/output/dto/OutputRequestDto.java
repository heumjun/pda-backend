package com.denso.pdabackend.domain.output.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Data;

@Data
public class OutputRequestDto {

    @Data
    public static class Info {
        private String company;         // 회사
        private String factory;         // 공장
        private String mf13No;		    // 출고요청번호(MST)
        private String mf13Dat;		    // 출고요청일자
        private String mf13DatTime;     // 출고요청시간
        private int mf13Seq;		    // 출고요청순번(MST)
        private int mf13Rev;            // 출고요청 revisition번호
        private String mf13Line;	    // 라인코드
        private String mf13LineCode;    // 라인코드
        private String mf13Indte;		// 출고요청 등록일자
        private int mf13Empno;       // 출고요청 등록자사번
        
        private String mf14No;          // 출고요청상세번호(DTL)
        private String mf14Hno;         // 출고요청번호
        private int mf14Seq;         // 출고요청상세순번(DTL)
        private String mf14Code;        // 품목코드
        private String cm08Code;        // 품목코드
        private String cm08Dgbn;        // 품목상세코드
        private String mf14Qty;         // 출고요청수량
        private String mf14Unt;         // 출고요청단위
        private String mf14Gbn;         // 품목구분
        private String cm08Gbn;         // 품목구분
        private String modalChk;        // 모달여부체크
        
        private String pmf13No;         // update, insert 체크용

    }

    @Data
    public static class Request extends CommonDto {
        private String company;
        private String factory;

        // 출고요청서 Master
        private String mf13No;
        private String mf13LineCode;
        private String mf13Dat;
        private String mf13DatFr;
        private String mf13DatTo;

        // 출고요청서 Detail
        private String mf14Hno;
        private String mf14No;
        private String mf14Code;

    }
}