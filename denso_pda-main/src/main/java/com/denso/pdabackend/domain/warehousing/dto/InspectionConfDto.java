package com.denso.pdabackend.domain.warehousing.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

public class InspectionConfDto {
	/*
    유무검사 완료 리스트 항목들
	 */
	@Data
	public static class Info {
		private String qa07Company;      /* 회사 */
		private String qa07Factory;      /* 공장 */
		private String qa07No;           /* 유무검사확인번호(등록일자+순번) */
		private String qa07Code;         /* 품목 */
		private String qa07Lot;          /* LOT번호 */
		private String qa07LotSeq;          /* LOT시퀀스 */
		private String qa07Status;         /* 상태 */
		private String qa07Gbn;         /* 구분 */
		private String qa07Dat;         /* 검사일자 */
		private String qa07Rmk;         /* 사유 */
		private String qa07Indte;        /* 등록일자 */
		private String qa07Updte;        /* 수정일자 */
		private Integer qa07Empno;       /* 등록자 사번 */
		private Integer qa07UpdEmpno;       /* 수정자 사번 */
		private String qa05No;           /* 유무검사번호(등록일자+순번) */
		private String qa05Available;    /* 유무검사 상태(공통코드 Y:완료, N:미완료) */
		private String qa05Total;        /* 전수검사 상태(공통코드 Y:완료, N:미완료) */
		private String qa05Sampling;     /* 샘플링검사 상태(공통코드 Y:완료, N:미완료) */
		private Integer qa05Qty;         /* 개수 지정 */
		private String cm19FileNo;       /* 파일번호 */
		private String st02Dat;          /* 입고일자 */
		private String cm08Name;         /* 품목명 */
		private String cm08Ename;        /* 품목명(영어) */
		private String cm08Gbn;          /* 품목구분(cm08_gbn 시스템 공통코드 G1:원자재, G2:부품, G3:제품) */

	}

	/*
   유무검사완료 검색 조건
	 */
	@Data
	@EqualsAndHashCode(callSuper=false)
	public static class Request extends CommonDto {
		private String company;   /* 회사 */
		private String factory;   /* 공장 */
		private String cm08Code;  /* 품목코드 */
		private String qa07Status;  /* 수입검사 상태 */
		private String st02Sdt;   /* 시작일시 */
		private String st02Edt;   /* 종료일시 */
	}
}
