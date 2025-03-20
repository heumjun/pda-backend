package com.denso.pdabackend.domain.packaging.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class AnomalyDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
    	
    	private String st09Company;
    	private String st09Factory;
    	private String st09No;
    	private String st09Dat;
    	private int st09Seq;
    	private String st09Gbn;
    	private String st09Pgbn;
    	private String st09Code;
    	private String st09Unt;
    	private String st09Lot;
    	private String st09Rmk;
    	private String st09Indte;
    	private int st09Empno;
    	private int st09Qty;
    	private int st09LotSeq;
    	private String st09Qrcode;
    	private String st09Line;
    	private String st09EquipCode;
    	private String st09Dept;
    	
    }
    
    @Data
    @SuperBuilder
    @NoArgsConstructor  //superBuilder 를 사용해서 상속받은 부모클래스도 builder를 사용하고자 할때 @NoArgsConstructor(기본생성자) 를 추가해야함.
    @EqualsAndHashCode(callSuper = false)
    public static class Request extends CommonDto {
    	
        private String st09Company;
        private String st09Factory;
        private String st09Dat;
        private int st09Seq;
        private String st09Qrcode;
    }

}
