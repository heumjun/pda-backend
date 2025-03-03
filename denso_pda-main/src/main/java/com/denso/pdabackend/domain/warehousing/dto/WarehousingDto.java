package com.denso.pdabackend.domain.warehousing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WarehousingDto {

	@Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Warehousing{
        private String matCode;
        private String matJil;
        private String matHcd;
        private String matUnit;
        private Integer matQty;
        private String matStock;
        private String matIndte;
        private String matUpdte;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WarehousingRequest{
        private String pu01No;
        private String st02Dat;
        private String st02Cus;
    }
	
}
