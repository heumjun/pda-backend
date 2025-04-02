package com.denso.pdabackend.domain.material.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MaterialsMoveDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
    	private String st02Company;         // 회사
        private String st02Factory;         // 공장
        private int st02Empno;
        private String st02No;
        private String st02Dat;
        private int st02Seq;
        private String st02Qno;
        private String st02Code;
        private String st02Lot;
        private String st02Gbn;
        private String st02Cus;
        private String st02Purno;
        private String st02Ipunt;
        private String st02Opunt;
        private String st02Currency;
        private int st02Ipqty;
        private int st02Qty;
        private int st02Cost;
        private int st02Amt;
        private int st02Vat;
        private String st02Rmk;
        private String st02Stok;
        private String st02CurStok;
        private String st02Field;
        private String st02Rfid;
        private String st02Indte;
        private String st02Wanyn;
        private String st02Purno2;
        private String st02Pkno;
        private String st02DtlNo;
        private String st02Qrcode;
        private String st02Status;
        private String st02Pno;
        private String st02Dist;
        private String st02CurDist;
        private int st02LotSeq;
        private String st02Reno;
        private String st02Line;
        private int st02Moq;
        private String st02RequestNo;
        
        private String stok;
        private String dist;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
    	private String st02Company;         // 회사
        private String st02Factory;         // 공장
        private int st02Empno;
        private int st02Seq;
    	private String st02Qrcode;
    }
    
}
