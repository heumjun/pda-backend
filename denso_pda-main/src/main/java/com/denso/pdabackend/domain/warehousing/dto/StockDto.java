package com.denso.pdabackend.domain.warehousing.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Data;

public class StockDto {

	@Data
	public static class Info{
		private String company;      // 회사 코드
		private String factory;      // 공장 코드
		private String st01Code;   // 품목 코드
		private String st01Lot;      // LOT 번호
		private String st01Gbn;      // 품목구분
		private String st01Stok;   // 보관장소
		private String st01District; // 구역
		private String st01Field; // 밭위치
		private String st01Unt;      // 재고단위
		private double st01Qty;      // 재고수량
		private String st01Indte;   // 입력일자
		private String st01Updte;   // 수정일자
		private int st01Empno;      // 등록자 사번

		private String operator; // 재고 증가, 감소 구분값

		private double ipQty; // 입고수량
		private double outQty; // 출고수량
		private String code;
		private String stock;
		private String lot;
		private String lotSeq;      // LOT 번호 시퀀스
		private String gbn;
		private String unt;
		private String dat;
		private Integer seq;
	}

	@Data
	public static class puInputInfo{
		private String company;
		private String factory;

		private int mt02Seq;
		private int pt02Seq;
		private int pd02Seq;
		private int st02Seq;

		private String lot;

		private String pu01No;
		private String pu02No;
		private String puInputNo;
		private String pu01Cus;
		private String pu01CusNm;
		private String pu02Gbn;
		private String pu02Code;
		private String pu02CodeNm;
		private String pu02Unt;
		private String inputUnit;
		private String outputUnit;
		private String pu02Currency;
		private Double pu02Qty;
		private Double puIpQty;
		private Double puQty;
		private String pu02Cost;
		private String puInputCost;
		private String inputGbn;
		private Double puInputAmt;
		private Double puInputVat;
		private String puInputStok;
		private String pu02Wanyn;
		private String puInputRmk;
		private String inputDat;

		private int puInputSum;

		private String st02No;


	}

	// 창고별 온/습도 개더링 데이터 조회
	@Data
	public static class tempHumInfo{
		private String company;
		private String factory;

		private String datetime;
		private String id1;
		private String id2;
		private Double value;
		private String type;
		private String indte;
		private String ref;
	}

	@Data
	public static class Request extends CommonDto {
		private String code;
		private String st01Code;
		private String st01Lot;
		private String st01Stok;
		private String st01Qty;
		private String st01Ordqty;
		private String st01Dedqty;
		private String st01Avgqty;
		private String st01Gbn;

		private String whCode;
		private String agdtCode;
		private String inDat;
		private String lot;

		private String cm15Exclusion;
		private String exclusionStock;
	}

}
