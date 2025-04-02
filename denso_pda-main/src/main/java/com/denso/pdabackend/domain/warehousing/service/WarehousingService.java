package com.denso.pdabackend.domain.warehousing.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.warehousing.dto.InputHistorySearchDto;
import com.denso.pdabackend.domain.warehousing.dto.InputHistorySearchDto.Info;
import com.denso.pdabackend.domain.warehousing.dto.InputHistorySearchDto.Request;
import com.denso.pdabackend.domain.warehousing.dto.InspectionConfDto;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;
import com.denso.pdabackend.domain.warehousing.dto.WarehousingDto.WarehousingRequest;
import com.denso.pdabackend.domain.warehousing.mapper.WarehousingMapper;
import com.denso.pdabackend.utils.MapUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehousingService {

	private final WarehousingMapper warehousingMapper;

	public List<Map<String, Object>> getWarehousingList(WarehousingRequest params) {
		return warehousingMapper.getWarehousingList(params);
	}

	public Map<String, Object> getOutputChk(Request params) {
		List<Map<String,Object>> list = warehousingMapper.getOutputChk(params);
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public boolean updateOfInputHistory(List<Info> updateList) {

		if( updateList != null ) {
			updateList.forEach(item -> {
				try {
					// 재고 테이블 수정 - tb_st_01 테이블
					StockDto.Info stockInfo = new StockDto.Info();
					BeanUtils.copyProperties(item, stockInfo);
					stockInfo.setIpQty(item.getSt02Ipqty()); // 입고수량
					stockInfo.setOutQty(item.getOldVal()); // 기존입고수량(차감할수량)
					stockInfo.setOperator("change");
					stockInfo.setCode(item.getCm08Code()); // 품목코드
					stockInfo.setLot(item.getSt02Lot()); // LOT
					stockInfo.setLotSeq(String.valueOf(item.getSt02LotSeq())); // LOT SEQ
					stockInfo.setStock(item.getSt02Stok()); // 보관창고
					stockInfo.setGbn(item.getCm08Gbn()); // 품목구분
					stockInfo.setUnt(item.getSt02Ipunt()); // 입고단위 = 재고단위
					stockInfo.setSt01District(item.getSt02Dist());

					// 재고 테이블 개수 업데이트
					warehousingMapper.updateOfPdStock(stockInfo);

					// 입고 테이블 수정
					warehousingMapper.updateOfInputHistory(item);

				} catch (Exception e){
					e.printStackTrace();
				}
			});
		}

		return true;
	}

	public Map<String, Object> getInputHistorySearchInfo(InputHistorySearchDto.Request params) {
		List<Map<String,Object>> list = warehousingMapper.getInputHistorySearchInfo(params);
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public boolean insertOfInputHistory(List<InputHistorySearchDto.Info> insertList) {

		// 현재 날짜 구하기        
		LocalDate now = LocalDate.now();         
		// 포맷 정의        
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");         
		// 포맷 적용        
		String formatedNow = now.format(formatter);
		
		insertList.forEach(item -> {
			
			try{
				// seq 가져오기
				HashMap hashMap = new HashMap();
				hashMap.put("company", item.getCompany());
				hashMap.put("factory", item.getFactory());
				hashMap.put("st02Pno", item.getSt02Pno());
				hashMap.put("cm08Code",item.getCm08Code());

				Map<String, Object> seq = warehousingMapper.getSeq(hashMap);
				String st02Seq = String.valueOf(seq.get("st02Seq"));
				String lot = "";

				// LOT 번호 생성
				// 바뀐 LOT -> 품목구분 2자리 + 라벨상세구분 4자리 + 일자 8자리 -> 14자리
				if(item.getSt02Lot() != null){
					lot = item.getSt02Lot();
				} else {
					lot = item.getCm08Gbn().substring(0,2) + item.getCm08Dgbn().substring(0,4) + formatedNow;
				}

				// 재고 테이블 등록 - tb_st_01 테이블
				StockDto.Info stockInfo = new StockDto.Info();
				stockInfo.setCompany(item.getCompany());            // 회사
				stockInfo.setFactory(item.getFactory());            // 공장
				stockInfo.setGbn(item.getCm08Gbn());                // 품목구분
				stockInfo.setIpQty(item.getSt02Ipqty());            // 수량
				stockInfo.setLot(lot);                              // LOT
				stockInfo.setCode(item.getCm08Code());              // 품목코드
				stockInfo.setStock(item.getSt02Stok());             // 보관창고
				stockInfo.setSt01District(item.getSt02Dist());      // 구역
				stockInfo.setUnt(item.getSt02Ipunt());              // 입고구분

				// 입고테이블 LOT SEQ 새로 생성
				int newLotSeq = warehousingMapper.getNewLotSeq(item);

				// LOT
				item.setSt02Lot(lot);
				// LOT SEQ
				item.setSt02LotSeq(newLotSeq);
				// Seq
				item.setSt02Seq(Integer.parseInt(st02Seq));

				// 재고테이블에 LOT SEQ
				stockInfo.setLotSeq(String.valueOf(newLotSeq));
				// 재고 테이블 업데이트(+)
				warehousingMapper.insertOfLotStock(stockInfo);
				// 입고 테이블 등록
				
				warehousingMapper.insertOfInputHistory(item);

				// 유무검사 여부가 Y인 경우 tb_qa_05 테이블의 기간을 확인하여 기간안에 들어간 경우
				// tb_qa_06 테이블 insert 시켜줘야한다.
				if(item.getQa05Available().equals("Y")){
					//                    Map<String, Object> chk = inputHistorySearchMapper.getAvailable(hashMap);
					Map<String, Object> chk = warehousingMapper.getAvailable(hashMap);

					// chk 값이 있는 경우 - tb_qa_06 테이블에 insert
					if( !MapUtils.isEmpty(chk)) {
						InspectionConfDto.Info inspectionConf = new InspectionConfDto.Info();
						inspectionConf.setQa07Company(String.valueOf(chk.get("qa05Company")));
						inspectionConf.setQa07Factory(String.valueOf(chk.get("qa05Factory")));
						inspectionConf.setQa05No(String.valueOf(chk.get("qa05No")));
						inspectionConf.setQa07Code(String.valueOf(chk.get("qa05Code")));
						inspectionConf.setQa07Gbn("");
						inspectionConf.setQa07Dat(formatedNow);
						inspectionConf.setQa07Status(String.valueOf(item.getSt02Status()));
						inspectionConf.setQa07No(null);
						inspectionConf.setQa07Lot(lot);
						inspectionConf.setQa07LotSeq(String.valueOf(newLotSeq));
						inspectionConf.setQa07Empno(item.getSt02Empno());

						//                        inputHistorySearchMapper.insertInspectionConf(inspectionConf);
						warehousingMapper.insertInspectionConf(inspectionConf);
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		});

		return true;
	}

	public Map<String, Object> getInputInfo(StockDto.Request params) {
		List<Map<String, Object>> list = warehousingMapper.getInputInfo(params);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

}
