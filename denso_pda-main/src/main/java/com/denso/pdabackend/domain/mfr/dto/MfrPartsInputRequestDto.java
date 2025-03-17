package com.denso.pdabackend.domain.mfr.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Data;

@Data
public class MfrPartsInputRequestDto {

    @Data
    public static class Info {
        private String company;         // 회사
        private String factory;         // 공장
        private int empno;			// 사번

        private String st01Gbn;
        private String st01Lot;
        private String st01Code;
        private String st01Stok;
        private String st01Distric;
        private String st01Field;
        private String st01Unt;
        private String st01Qty;
        private int st01LotSeq;
        private String st01Qrcode;

        private String cm08Code;        // 품목코드
        private String cm08Dgbn;        // 품목상세코드
        private String cm08Gbn;         // 품목구분
        private String cm08Cus;
        private String cm08Moq;

        private String mf02Pcc;		//생산지시번호
        private String mf02No;		//생산지시디테일번호

    }

    @Data
    public static class Request extends CommonDto {
        private String company;
        private String factory;

        private String st01Code;
        private String st01Lot;
        private int st01LotSeq;
        private String st01Qrcode;
    }
}

