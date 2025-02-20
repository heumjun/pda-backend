package com.denso.pdabackend.domain.codeManage.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class CommonCodeDto {
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommonHead{
        private String cohJan;
        private String cohCode;
        private String cohName;
        private String cohIndte;
        private String cohRmk;
        private String cohRegid;
        
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommonDesc{
        private String codJan;
        private String codHcode;
        private String codCode;
        private String codName;
        private Integer codOrder;
        private String codLock;
        private String codIndte;
        private String codUpdte;
        private String codRegid;
        
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static class CommonCodeRequest extends CommonDto{
        private String groupCode;
        private boolean unLock;
    }
}
