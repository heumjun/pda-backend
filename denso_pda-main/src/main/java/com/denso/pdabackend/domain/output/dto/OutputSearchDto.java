package com.denso.pdabackend.domain.output.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class OutputSearchDto {

	@Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private String company;         // 회사
        private String factory;         // 공장
        private String st03No;		    // 출고번호
        private String st03Dat;		    // 출고일자
        private int st03Seq;		    // 출고순번
        private String st03Code;	    // 품목코드
        private String st03Lot;		    // LOT번호
        private String st03Gbn;         // 출고구분
        private String st03Cus;         // 거래처
        private String st03Unt;	        // 출고단위
        private double st03Qty;         // 출고수량
        private String st03Rmk;         // 비고
        private String st03Stok; 	    // 보관창고
        private String st03Rfid;        // rfid
        private String st03Indte;	    // 출고시간
        private int st03Empno;		    // 등록자 사번
        private String st03Line;        // 라인코드
        private String st03Qr;          // QR코드
        private String st03Status;      // 상태
        private String st03Dist;        // 구역
        private String st03Dgbn;        // 납품출고여부?
        private String cm08Dgbn;        // 품목상세코드
        private String cm08Code;        // 품목코드
        private String cm08Gbn;         // 품목구분
        private String st03OutputNo;    // 출고요청서번호
        private String st03OutputDtlNo; // 출고요청서상세번호
        private String st03Pcc;         // 생산지시서번호
        private String st03Reno;        // 반납요청서번호
        private String st03Rtno;        // 반품요청서번호
        private int st03LotSeq;         // LOT SEQ
        private int oriOutqty;          // 기존 출고수량

        private String pst03No;             // update 인지 insert 확인용
        private String modalChk;            // modal 여부 체크(삭제)

        private String mf13No;

    }

	@Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request extends CommonDto {
        private String company;
        private String factory;
        private String st03No;
        private String st03Stok;
        private String st03Dist;
        private String st03Code;	    // 품목코드
        private String st03Dat;
        private String st03DatFr;
        private String st03DatTo;
        private String st03OutputNo;        // 출고요청서번호
        private String st03OutputDtlNo;     // 출고요청서상세번호
        private String st03Line;            // 라인코드
        private String st03Lot;             // LOT번호
        private int st03LotSeq;             // LOT SEQ

        private String st03Cus;
        private String st03Gbn;

        private String mf13No;             // 출고요청서번호
        private String mf13DatFr;          // 출고요청시작일
        private String mf13DatTo;          // 출고요청종료일
        

    }
    
}
