package com.denso.pdabackend.domain.material.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MaterialDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Material{
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
    public static class MaterialRequest{
        private String code;
        private String jil;
        private String hcd;
    }
    
}
