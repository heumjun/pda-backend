package com.denso.pdabackend.domain.criteria.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Data;

public class WarehouseDto {

    @Data
    public static class Info {
        private String company;
        private String factory;
        private String cm15Code;
        private String cm15Name;
        private String cm15Lock;
        private String cm15Exclusion;
        private String cm15Rmk;
        private String cm15Indte;
        private String cm15Updte;
        private String cm15Empno;
    }

    @Data
    public static class Request extends CommonDto {
        private String company;
        private String factory;
        private String cm15Code;
        private String cm15Name;
        private String cm15Lock;
        private String cm15Exclusion;
    }
}
