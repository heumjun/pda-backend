package com.denso.pdabackend.domain.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MenuDto {

    @Data
    @Builder
    @NoArgsConstructor      //jackson을 사용해서 json에서 java object로 변경할때, @SuperBuilder사용시 생성자가 필요함.
    @AllArgsConstructor     //NoArgsConstructor를 사용하니 @Builer 패턴사용이 안됨. 모든필드 포함하는 생성자도 같이 필요함.
    public static class Menu{
        private String code;
        private String name;
        private Integer seq;
        private String uri;   
        private String upcode;
        private String upcodeName;
        private String icon;
    }

    @Data
    @Builder
    public static class MenuRequest{
    	private String company;    	//메뉴코드
    	private String factory;    	//메뉴코드
        private String dept;    	//메뉴코드
        private boolean lock;       //사용중지 코드포함 여부
    }
}
