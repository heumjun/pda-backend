package com.denso.pdabackend.domain.packaging.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class RecycleDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
    	private String st12Company;
    	private String st12Factory;
    	private String st12No;
    	private int st12Cnt;
    	private String st12LotSeq;
    	private String st12Code;
    	private String st12Pcc;
    	private String st12Qr;
    	private String st12LineCode;
    	private String st12EquipCode;
    	private int st12Empno;
    }
    
    @Data
    @SuperBuilder
    @NoArgsConstructor  //superBuilder 를 사용해서 상속받은 부모클래스도 builder를 사용하고자 할때 @NoArgsConstructor(기본생성자) 를 추가해야함.
    @EqualsAndHashCode(callSuper = false)
    public static class Request extends CommonDto {
    	private String st12Company;
    	private String st12Factory;
    	private String st12No;
        private String st12Qr;
    }

}
