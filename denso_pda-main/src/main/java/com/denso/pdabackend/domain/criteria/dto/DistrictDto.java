package com.denso.pdabackend.domain.criteria.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Data;

public class DistrictDto {

    @Data
    public static class Info {
        private String company;
        private String factory;
        private String cm16Stok;
        private String cm16Code;
        private String cm16Name;
        private String cm16Lock;
        private String cm16Rmk;
        private String cm16Indte;
        private String cm16Updte;
        private String cm16Empno;
    }

    @Data
    public static class Request extends CommonDto {
        private String company;
        private String factory;
        private String cm16Stok;
        private String cm16Code;
        private String cm16Name;
        private String cm16Lock;
    }
}
