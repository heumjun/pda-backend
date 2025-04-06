package com.denso.pdabackend.domain.stock.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class ConsignedMaterialsOutputDto {

	@Data
	@SuperBuilder
	@NoArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class MasterInfo {
		private String mf15Company;   	// 회사
		private String mf15Factory;   	// 공장
		private String mf15No;        	// 사급지시서번호
		private String mf15Requestno;   // 사급지시서번호
		private int mf15Seq;      		// 순번
		private String mf15Cus;       	// 제조사코드
		private int mf15EmpNo;       	// 로그인 사용자
	}
	
	@Data
	@SuperBuilder
	@NoArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class DetailInfo {
		private String mf16Company;   	// 회사
		private String mf16Factory;   	// 공장
		private String mf16Hno;        	// 사급지시서 마스터 번호
		private String mf16No;        	// 사급지시서 상세번호
		private int mf16Seq;      		// 순번
		private String mf16Code;       	// 품목코드
		private int mf16Qty;       		// 품목 MOQ
		private String mf16Gbn;       	// 품목 (무상,유상)
		private int mf16EmpNo;       	// 로그인 사용자
		
		private String mf15Cus;       	// 제조사코드
		private String st02Code;
		private String st02Lot;
		private String st02LotSeq;
		private String st02Qrcode;
		private String st02Dgbn;
		private String st02Stok;
		private String st02Dist;
		private int st02Qty;
		private int st02Moq;
		
		private String st03No;        // 출고번호
		private String st03NoNew;     // 출고번호
		private Integer st03Seq;      // 순번
		private String st03Cus;
	}
	
	@Data
	@SuperBuilder
	@NoArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class Request extends CommonDto {
		private String company;
		private String factory;
	}

}
