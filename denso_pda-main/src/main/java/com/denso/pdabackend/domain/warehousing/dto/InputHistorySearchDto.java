package com.denso.pdabackend.domain.warehousing.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Data;

public class InputHistorySearchDto {
	@Data
	public static class Info {
		private String select;          // 선택여부
        private String company;         // 회사
        private String factory;         // 공장
        private String st02No;          // 입고번호
        private String st02DtlNo;      // 입고상세번호
        private String st02Dat;          // 입고일자
        private int st02Seq;          // 입고순번
        private String st02Qno;         // 품질검사번호
        private String st02RequestNo;   // 출고완료번호
        private String st02Code;       // 품목코드
        private String st02Lot;          // LOT번호
        private int st02LotSeq;          // LOT SEQ
        private String st02Gbn;         // 입고구분
        private String st02Cus;         // 거래처
        private String st02Line;        // 라인
        private String st02Purno;       // 구매발주번호
        private String st02Purno2;      // 납품확인서번호
        private String st02Ipunt;       // 입고단위
        private String st02Opunt;       // 출고단위
        private String st02Currency;    // 화폐단위
        private double st02Ipqty;       // 입고수량
        private int st02Moq;            // moq
        private double st02Qty;         // 환산수량
        private double st02Cost;        // 입고단가
        private double st02Amt;         // 입고금액
        private double st02Vat;         // 입고부가세
        private String st02Rmk;         // 비고
        private String st02Stok;        // 보관창고
        private String st02Rfid;        // rfid
        private String st02Indte;       // 입고시간
        private int st02Empno;          // 등록자 사번

        // 품질관리
        private String qa01No;          // 품질검사번호
        private String qa01Gbn;         // 생산입고등록여부

        private String cm08Gbn;         // 품목구분

        private String st01District;         // 구역코드

        private double oriIpqty;

        private String st03Lot;

        // 입고이력조회
        private String st02Qrcode;
        private String st02Status;
        private String st02Dist;
        private String st02Dgbn;
        private String cm08Dgbn;
        private String cm08Code;
        private String st02MstSeq;
        private String st02Pno;
        private Double oldVal;
        private String qa05Available;
        private String lot;
        private String modalChk;        // 모달여부체크

        // 입고이력조회 - head
        private String pst02No;
	}

	@Data
	public static class Request extends CommonDto {
		
		private String company;
        private String factory;
        private String qa01No;
        private String qa01Code;
        private String st02No;
        private String st02DtlNo;
        private String st02Qno;
        private String st02Stok;
        private String st02Dist;
        private String st02Code;       // 품목코드
        private String st02Dat;
        private String st02Lot;
        private String st02LotSeq;
        private String st02DatFr;
        private String st02DatTo;

        private String st02Purno;
        private String st02Pno;
        private String st02Cus;
        private String st02Gbn;

        private String napDatSt;
        private String napDatEd;
        private String code;
        private String delvNo;
        private String dat;


	}
}
