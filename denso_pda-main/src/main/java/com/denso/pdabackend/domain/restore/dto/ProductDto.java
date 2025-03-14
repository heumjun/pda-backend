package com.denso.pdabackend.domain.restore.dto;

import com.denso.pdabackend.common.CommonDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class ProductDto {

	@Data
	public static class ProductInfo {
		private String cm08Company;   // 회사 코드
		private String cm08Factory;   // 공장 코드
		private String cm08Code;   // 품목 코드
		private String cm08Name;   // 품목명
		private String cm08Ename;   // 품목명(영문)
		private String cm08Gbn;      //품목구분
		private String cm08Ipunt;   // 입고단위
		private String cm08Opunt;   // 출고단위
		private double cm08Convert; // 환산단위
		private double cm08ProdConvert; // 생산환산단위
		private int cm08Nap;      // 발주후 납기일
		private double cm08Minqty;   // 최소발주량
		private double cm08Avgqty;   // 적정재고량
		private String cm08Avgyn;   // 적정재고관리여부
		private String cm08Cert;   // 인증협회
		private String cm08Rmk;      // 비고
		private String cm08Indte;   // 입력일자
		private String cm08Updte;   // 수정일자
		private int cm08Empno;      // 등록자사번
		private double cm08Ordqty;   // 기수주량

		private String cm08Modelnum; // 형번
		private String cm08Dgbn;    // 품목상세구분
		private String cm08Moq;    // 최소포장단위
		private String cm08Fifo;    //선입선출 구분
		private String cm08Iqc;    // 수입검사 구분
		private String cm08LotYn; // LOT 추적 사용구분
		private String cm08Barcode;// 바코드 정보
		private String cm08Inputyn;// 입고재발행여부
		private String cm08Outputyn;// 출고재발행여부
		private String cm08Cartype; // 차종
		private String cm08Yndte;     // 유무전환일
		private String cm08RiDte;  // 정기검사일
		private String cm08Release; //불출요청
		private String cm08Input;     // 투입여부
		private String cm08Over;     //과다체크
		private String cm08Abn;     //이품체크
		private String cm08ChangeQty; // 변경 SET 수량
		private String cm08Ratiomt;    // 비율자재여부
		private String cm08RatiomtId; // 비율대표자재 ID
		private String cm08Mt;       // 대체자재여부
		private String cm08MtId;       //대체자재ID
		private String cm08Order;       // 오더종속여부
		private String cm08Prftime;    // 실적대기시간
		private String cm08RatioId;    // 대표비율 ID
		private String cm08RatioStd;    // 대표비율기준
		private String cm08Cmf;       //품목등록공장
		private String cm08Cus;       //
		private String cm08VapackYn;   //
		private String cm08MesYn;       //

	}

	@Data
	@EqualsAndHashCode(callSuper=false)
	public static class Request extends CommonDto {
		private String company;
		private String factory;
		private String cm08Code;
		private String cm08Gbn;
		private String cm08Name;
		private String cm08Ename;
		private String cm08Avgyn;
		private String cm08Minqty;
		//기수주량 증가/감소 구분값
		private String cm08Operator;
		//기수주량 증가/감소량
		private double cm08_ordqty;
		private String st02No; // 입고번호
		private String st02Sdat; //입고일자 s
		private String st02Edat; //입고일자 e
		private String cm08Dgbn; //입고일자 e
		private String cm08LotYn; //로트추적
	}

}
