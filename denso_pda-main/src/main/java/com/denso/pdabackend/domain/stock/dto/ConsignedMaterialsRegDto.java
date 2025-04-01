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

		private String cm08Gbn;        // 품목구분
		private String cm08Dgbn;        // 레벨정보구분
		private double cm08Moq;        // 최소포장단위

		private String st03No;        // 출고번호
		private String st03NoNew;     // 출고번호
		private Integer st03Seq;      // 순번
		private String st03Lot;       // LOT번호
		private String st03LotSeq;    // LOT번호시퀀스
		private double st03Qty;       // 수량
		private String st03Moq;       // MOQ
		private double st01Qty;       // 재고량
		private String st02Qrcode;    // QR
		private String st01Stok;      // 창고장소
		private String st01District;  // 구역장소
		private String st03Cus;  // 구역장소
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
		private String st03No;        // 출고번호
	}

	@Data
	@SuperBuilder
	@NoArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class Request extends CommonDto {
		private String company;
		private String factory;
		private String mf15No;
		private String st03No;
		private String mf15Sdt;
		private String mf15Edt;
		private String cm01Code;
		private String cm01CodeName;
	}

	@Data
	@SuperBuilder
	@NoArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class Lot extends CommonDto {
		private String company;
		private String factory;
		private String st01Code;
		private String lot;
		private String lotSeq;
	}

}
