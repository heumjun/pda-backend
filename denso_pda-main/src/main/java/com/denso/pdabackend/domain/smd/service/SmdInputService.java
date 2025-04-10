package com.denso.pdabackend.domain.smd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.output.dto.OutputSearchDto;
import com.denso.pdabackend.domain.smd.dto.SmdInputRequestDto.Info;
import com.denso.pdabackend.domain.smd.dto.SmdInputRequestDto.Request;
import com.denso.pdabackend.domain.smd.mapper.SmdInputMapper;
import com.denso.pdabackend.domain.warehousing.dto.InputHistorySearchDto;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;
import com.denso.pdabackend.utils.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmdInputService {

	private final SmdInputMapper smdInputMapper;

	public boolean saveOfSmdInput(List<Info> insertList) {

		if(insertList != null){

			insertList.forEach(item -> {

				try{

					// N 인경우 출고 -> 출고
					// Y 인경우 출고요청완료 -> 입고
					// 출고요청완료 등록 시 compYn을 Y로 변환시킴.
					smdInputMapper.updateOfOutputRequestComp(item);

					// Y로 변환시키면 해당 품목들은 모두 입고 처리되어야함. -> tb_st_02 테이블 insert
					// tb_st_01 테이블에도 insert 재고 +
					// 재고 테이블 등록 - tb_st_01 테이블
					StockDto.Info stockInfo = new StockDto.Info();
					stockInfo.setCompany(item.getCompany());                            // 회사
					stockInfo.setFactory(item.getFactory());                            // 공장
					stockInfo.setGbn(item.getCm08Gbn());                                // 품목구분
					stockInfo.setIpQty(item.getSt02Ipqty());                            // 증가수량
					stockInfo.setLot(item.getSt02Lot());                                // LOT
					stockInfo.setLotSeq(item.getSt02LotSeq());                          // LOT
					stockInfo.setCode(item.getSt02Code());                              // 품목코드
					stockInfo.setStock(item.getSt02Stok());                             // 보관창고
					stockInfo.setSt01District(item.getSt02Dist());                      // 구역
					stockInfo.setUnt(item.getSt02Ipunt());                              // 입고구분
					stockInfo.setOperator("plus");
					smdInputMapper.updateOfStok(stockInfo);
					smdInputMapper.updateOfPdStock(stockInfo);

					// seq 가져오기
					Map<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("company", item.getCompany());
					hashMap.put("factory", item.getFactory());
					//hashMap.put("st02Dat", item.getMf13Dat());
					Map<String, Object> seq = smdInputMapper.getSeq(hashMap);
					int st02Seq = Integer.parseInt( StringUtils.nullString(seq.get("st02Seq")) );

					// tb_st_02 테이블에 insert
					// 입고 테이블 등록 - tb_st_02 테이블
					InputHistorySearchDto.Info inputInfo = new InputHistorySearchDto.Info();
					inputInfo.setCompany(item.getCompany());                                // 회사
					inputInfo.setFactory(item.getFactory());                                // 공장
					//inputInfo.setSt02Dat(item.getMf13Dat());                                // 입고일자 -> 출고요청완료일자

					inputInfo.setSt02Seq(st02Seq);                      // seq -> seq
					inputInfo.setCm08Code(item.getSt02Code());                              // 품번 -> 품번
					inputInfo.setSt02Lot(item.getSt02Lot());                                // LOT -> LOT
					inputInfo.setSt02LotSeq(Integer.parseInt(item.getSt02LotSeq()));        // LOT_SEQ -> LOT_SEQ
					inputInfo.setSt02Gbn("OC");                                             // 입고 구분
					inputInfo.setSt02Cus("");                                               // 제조사 -> 라인에서 오는 항목은 어떻게 처리?
					inputInfo.setSt02Line(item.getMf13LineCode());                              // 라인
					inputInfo.setSt02Pno("");                                               // 발주번호 존재하지않음
					inputInfo.setSt02Purno("");                                             // 발주상세번호 존재하지 않음.
					inputInfo.setSt02Ipunt(item.getSt02Ipunt());                            // 입고단위 -> 출고단위
					inputInfo.setSt02Ipqty(item.getSt02Ipqty());                            // 입고수량 -> 출고수량
					inputInfo.setSt02Moq(Integer.parseInt(item.getSt02Moq())); 								// 실제수량 Moq
					inputInfo.setSt02Qrcode(item.getSt02Qrcode());                          // QR코드
					inputInfo.setSt02Status("S");                                        	// 유무검사상태
					inputInfo.setSt02Stok(item.getSt02Stok());                              // 창고
					inputInfo.setSt02Dist(item.getSt02Dist());                              // 구역
					inputInfo.setSt02RequestNo(item.getSt02RequestNo());                    // 출고완료번호
					inputInfo.setSt02Qty(0);
					smdInputMapper.insertOfInputHistory(inputInfo);

				} catch (Exception e){
					e.printStackTrace();
				}
			});
		}

		return true;
	}

	public int getOutputGbn(Request request) {
		return smdInputMapper.getOutputGbn(request);
	}

	public Map<String, Object> getLotInfo(OutputSearchDto.Request params) {
		return smdInputMapper.getLotInfo(params);
	}

	public Map<String, Object> inspectChk(OutputSearchDto.Request params) throws Exception {
		List<Map<String,Object>> list = smdInputMapper.inspectChk(params);
		if(list.size()>0)
			return list.get(0);
		return null;
	}

	public Map<String, Object> inspectSpecChk(OutputSearchDto.Request params) throws Exception {
		List<Map<String,Object>> list = smdInputMapper.inspectSpecChk(params);
		if(list.size()>0)
			return list.get(0);
		return null;
	}

	public Map<String, Object> firstInOutChk(OutputSearchDto.Request params) throws Exception {
		List<Map<String,Object>> list = smdInputMapper.firstInOutChk(params);
		if(list.size()>0)
			return list.get(0);
		return null;
	}

	public Map<String, Object> firstInputData(OutputSearchDto.Request params) throws Exception {
		List<Map<String,Object>> list = smdInputMapper.firstInputData(params);
		if(list.size()>0)
			return list.get(0);
		return null;
	}

}
