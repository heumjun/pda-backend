package com.denso.pdabackend.domain.stock.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class ConsignedMaterialsRegDto {

    @Data
    @Builder
    public static class Info {
    	
    }
    
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static class Request extends CommonDto {
    	
        private String mf15Company;
        private String mf15Factory;
        private String mf15No;

    }

}
