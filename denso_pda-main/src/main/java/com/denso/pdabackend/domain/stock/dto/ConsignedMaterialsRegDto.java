package com.denso.pdabackend.domain.stock.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class ConsignedMaterialsRegDto {

	@Data
    @SuperBuilder
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
	public static class Info {
		private String mf16Company;     // 회사
		private String mf16Factory;     // 공장
		private String mf16Hno;         // 사급지시서번호
		private String mf16No;          // 사급지시서상세번호
		private Integer mf16Seq;        // 상세순번
		private String mf16Code;        // 품목코드
		private double mf16Qty;         // 지시수량
		private String mf16Gbn;         // 구분(유상, 무상)
		private String mf16Indte;       // 등록일자
		private Integer mf16Empno;       // 등록자
		private String mf16Updte;       // 수정일자
		private Integer mf16UpdEmpno;    // 수정자

		private Integer delCnt; // 삭제 후 상세 리스트 갯수
		private String st03No;       // 입고번호
	}

	@Data
    @SuperBuilder
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
	public static class MasterInfo {
		private String mf15Company;   // 회사
		private String mf15Factory;   // 공장
		private String mf15No;        // 사급지시서번호
		private String mf15Dat;       // 사급지시일자
		private Integer mf15Seq;      // 순번
		private String mf15Cus;       // 제조사코드
		private String mf15Indte;     // 등록일자
		private Integer mf15Empno;    // 등록자사번

		private String inputMode;     // 저장모드
		private String cm01Code;      // 제조사코드(등록 시)
	}

	@Data
    @SuperBuilder
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
	public static class Request extends CommonDto {
		private String mf15Company;   // 회사
		private String mf15Factory;   // 공장
		private String mf15No;
		private String mf15Sdt;
		private String mf15Edt;
		private String cm01Code;
		private String cm01CodeName;
	}

}
