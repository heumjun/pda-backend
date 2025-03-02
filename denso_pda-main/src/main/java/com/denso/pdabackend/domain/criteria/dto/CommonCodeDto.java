package com.denso.pdabackend.domain.criteria.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

public class CommonCodeDto {
	
	
	/**
	 * 공통코드 master
	 * @author jyj
	 *
	 */
	@Data
	public static class MasterInfo{
		private String cm04Company;
		private String cm04Factory;
		private String cm04Code;
		private String cm04Name;
		private Integer cm04Gbn;
		private String cm04Table;
		private String cm04Rmk;
		private String cm04Indte;
		private String cm04Updte;
		private String cm04RegId;
	}
	
	/**
	 * 공통코드 detail
	 * @author jyj
	 *
	 */
	@Data
	public static class DetailInfo{
		private String cm05Company;
		private String cm05Factory;
		private String cm05Code;
		private String cm05Value;
		private String cm05Name;
		private Integer cm05Seq;
		private String cm05Indte;
		private String cm05Updte;
		private String cm05RegId;
		private String cm05Lock;
		
	}
	
	/**
	 * 조회부분 request
	 * @author jyj
	 *
	 */
	@Data
	@EqualsAndHashCode(callSuper=false)		// 자식클레스에서 lombok 사용할때
	public static class Request {
		private String code;
		private String value;
		private String lock;
		private Integer gbn;


		private String tempCode; // 온도단위
		private String speedCode; // 속도단위
		private String gbnCcp; //Ccp목록만 보여줄 경우
		
		private String company;
        private String factory;
	}
}
