package com.denso.pdabackend.domain.smd.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Data;

@Data
public class PartsInputRequestDto {

    @Data
    public static class Info {
        private String company;         // 회사
        private String factory;         // 공장

        private String st02No;			//입고번호
        private String st02Dat;			//입고일자
        private int st02Seq;			//입고순번
        private String st02Qno;
        private String st02Code;		//품목코드
        private String st02Lot;			//LOT
        private String st02Gbn;			//입구구분(PR:생산)
        private String st02Cus;
        private String st02Purno;		//납품확인서번호
        private String st02Ipunt;		//입고단위
        private String st02Opunt;
        private String st02Currency;
        private String st02Ipqty;		//입고수량
        private String st02Qty;			//투입수량
        private String st02Cost;
        private String st02Amt;
        private String st02Vat;
        private String st02Rmk;
        private String st02Stok;		//창고
        private String st02Field;
        private String st02Rfid;
        private String st02Indte;		//입고시간
        private int st02Empno;
        private String st02Wanyn;
        private String st02Purno2;
        private String st02Pkno;		//포장실적번호
        private String st02DtlNo;
        private String st02Qrcode;		//QR CODE 번호
        private String st02Status;		//상태
        private String st02Pno;
        private String st02Dist;		//구역
        private int st02LotSeq;			//LOT시퀀스
        private String st02Reno;		//반납요청서번호
        private String st02Line;		//라인코드
        private String st02Moq;			//입고 moq
        private String st02RequestNo;	//출고완료번호

        private String cm08Code;        // 품목코드
        private String cm08Dgbn;        // 품목상세코드
        private String cm08Gbn;         // 품목구분
    }

    @Data
    public static class Request extends CommonDto {
        private String company;
        private String factory;

        // QR CODE 번호
        private String st02Qrcode;

    }
}

