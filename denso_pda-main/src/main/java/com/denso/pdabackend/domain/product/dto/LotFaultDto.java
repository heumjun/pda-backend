package com.denso.pdabackend.domain.product.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class LotFaultDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
    	
    	private String st08Company;
    	private String st08Factory;
    	private String st08No;
    	private String st08Dat;
    	private int st08Seq;
    	private String st08Gbn;
    	private String st08Pgbn;
    	private String st08Code;
    	private String st08Unt;
    	private String st08Lot;
    	private String st08Rmk;
    	private String st08Stok;
    	private String st08Indte;
    	private int st08Empno;
    	private int st08Qty;
    	private String st08Dist;
    	private int st08LotSeq;
    	private String st08Qrcode;
    	private String st08Line;
    	private String st08EquipCode;
    	
    }
    
    @Data
    @SuperBuilder
    @NoArgsConstructor  //superBuilder 를 사용해서 상속받은 부모클래스도 builder를 사용하고자 할때 @NoArgsConstructor(기본생성자) 를 추가해야함.
    @EqualsAndHashCode(callSuper = false)
    public static class Request extends CommonDto {
    	
        private String st08Company;
        private String st08Factory;
        private String st08Dat;
        private int st08Seq;
        private String st08Qrcode;
        
        private String smdFlag;
        private String arrayCnt;
        
    }

}
