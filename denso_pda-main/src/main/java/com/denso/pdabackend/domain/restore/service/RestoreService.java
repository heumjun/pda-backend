package com.denso.pdabackend.domain.restore.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.output.mapper.OutputMapper;
import com.denso.pdabackend.domain.restore.dto.ProductDto;
import com.denso.pdabackend.domain.restore.dto.RestoreDto;
import com.denso.pdabackend.domain.restore.dto.RestoreDto.Request;
import com.denso.pdabackend.domain.restore.dto.RestoreMgmtDto;
import com.denso.pdabackend.domain.restore.mapper.RestoreMapper;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;
import com.denso.pdabackend.response.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestoreService {
	
	private final RestoreMapper restoreMapper;
	private final OutputMapper outputMapper;

	public List<Map<String, Object>> getRestoreList(Request param) {
		return restoreMapper.getRestoreList(param);
	}

	public boolean updateRestore(List<RestoreDto.Info> updateList) {
		
		// 반납등록 디테일 값 체크
        if(updateList != null) {
        	
            for( RestoreDto.Info info: updateList ) {

                if (info.getSt11No() == null  || info.getSt11No() == "" ) {
                    throw new BusinessException("반납요청서 상세번호가 존재하지 않아 수정할 수 없습니다.");
                }
                
//                if ( Double.isNaN(info.getSt02Qty()) || BigDecimal.valueOf(info.getSt02Qty()).compareTo(BigDecimal.ZERO) <= 0 ) {
//                    throw new BusinessException("수량이 존재하지 않아 수정할 수 없습니다.");
//                }
                
                if ( info.getSt11Stok() == null  || info.getSt11Stok() == "" ) {
                    throw new BusinessException("창고가 존재하지 않아 수정할 수 없습니다.");
                }
                
                if ( info.getSt11Dist() == null  || info.getSt11Dist() == "" ) {
                    throw new BusinessException("구역이 존재하지 않아 수정할 수 없습니다.");
                }

                // 품목 체크
                ProductDto.Request pdtRequest = new ProductDto.Request(); // 객체 생성

                // 입력받은 데이터 할당
                pdtRequest.setCm08Code(info.getSt11Code());
                pdtRequest.setCompany(info.getSt11Company());
                pdtRequest.setFactory(info.getSt11Factory());

                List<Map<String,Object>> pdResultMap = restoreMapper.getProductInfo(pdtRequest); // DB 조회
                if ( pdResultMap == null || pdResultMap.size() == 0 ) { // 품목코드가 존재하지 않을 경우
                    throw new BusinessException ("존재하지 않는 품번은 등록할 수 없습니다.");
                } else {
                    Map<String, Object> pdResultInfos = pdResultMap.get(0);

                    info.setCm08Gbn(String.valueOf(pdResultInfos.get("cm08Gbn")));
                    info.setCm08Dgbn(String.valueOf(pdResultInfos.get("cm08Dgbn")));
                    info.setCm08Ipunt(String.valueOf(pdResultInfos.get("cm08Ipunt")));
                    info.setCm08Opunt(String.valueOf(pdResultInfos.get("cm08Opunt")));
                    //info.setQrcode(String.valueOf(pdResultInfos.get("cm08Barcode"))); // QR
                }

                // 반납 요청 존재확인
                RestoreMgmtDto.Request request = new RestoreMgmtDto.Request();
                request.setCompany(info.getSt11Company());
                request.setFactory(info.getSt11Factory());
                request.setSt10No(info.getSt11Hno());

                List<Map<String, Object>> returnForReqMgmtList = restoreMapper.getRestoreMgmtInfo(request);

                if (returnForReqMgmtList == null || returnForReqMgmtList.size() <= 0) {
                    throw new BusinessException("존재하지 않는 반납요청 정보는 수정할 수 없습니다.");
                }
            }
        }

        if (updateList != null) {
        	updateList.forEach(item -> {
                try {
                    // 창고 / 구역 정보 변경 여부 체크
                    boolean changeDataYn = restoreMapper.getChangeDataYn(item);
                    
                    // 1. 반납정보 저장
                    boolean retSucc = restoreMapper.updateReturnForReg(item);

                    if (!retSucc) {
                        throw new BusinessException("반납 등록에 실패하였습니다.");
                    }

                    // 2. 출고 등록
                    // 수량 만 변경
                    boolean retOutSucc = restoreMapper.saveOfProductionOutput(item);

                    if (!retOutSucc) {
                        throw new BusinessException("출고 등록(수정)에 실패하였습니다.");
                    }

                    // 3. 재고 수량 변경 - 출고로 인한 수량 감소
                    // update

                    // 4. 재고 수량 계산
                    // 재고수량 변경
                    // 수량만 변경될때는 업데이트
                    // 구역, 장소가 변경되면 추가 재고 입력
                    StockDto.Info params = new StockDto.Info();
                    params.setCompany(item.getSt11Company()); // 회사
                    params.setFactory(item.getSt11Factory()); // 공장
                    params.setCode(item.getSt11Code());  // 품목
                    // 테스트를 위해
//                    params.setLot(item.getSt11Lot()); // LOT
//                    params.setLotSeq(item.getSt11LotSeq()); // LOT SEQ
                    
                    params.setOperator("change"); // 업데이트 구분자(재구 수량 변경)
                    params.setStock(item.getSt11Stok()); // 보관장소
                    params.setSt01District(item.getSt11Stok()); // 보관장소
                    // 수량 세팅
                    params.setOutQty(item.getSt02Qty()); // 빼기
                    params.setIpQty(0);  // 더하기

                    boolean pdStockOutResult = outputMapper.updateOfPdStock(params);

                    if (!pdStockOutResult) {
                        throw new BusinessException("재고 수량(출고) 수정에 실패하였습니다.");
                    }

                    // 3. 입고 등록
                    // 수량, 창고, 구역 변경
                    boolean retInSucc = restoreMapper.saveOfProductionInput(item);

                    if (!retInSucc) {
                        throw new BusinessException("입고 등록(수정)에 실패하였습니다.");
                    }

                    // 4. 재고 변경
                    if (changeDataYn) { // 변경사항이 있을때
                        // insert

                        // 4. 재고 신규 추가 - 창고나 구역이 변경 되었을때
                        boolean pdStockInResult = restoreMapper.insertOfPdStock(item);

                        if (!pdStockInResult) {
                            throw new BusinessException("재고(입고) 등록에 실패하였습니다.");
                        }
                        
                    } else { // 수량만 변경일때
                        // update

                        // 4. 재고 수량 계산
                        // 재고수량 변경
                        // 수량만 변경될때는 업데이트
                        // 구역, 장소가 변경되면 추가 재고 입력
                        StockDto.Info inParams = new StockDto.Info();
                        inParams.setCompany(item.getSt11Company()); // 회사
                        inParams.setFactory(item.getSt11Factory()); // 공장
                        inParams.setCode(item.getSt11Code());  // 품목
                        // 테스트 위해
//                        inParams.setLot(item.getSt11Lot()); // LOT
//                        inParams.setLotSeq(item.getSt11LotSeq()); // LOT SEQ
                        inParams.setOperator("change"); // 업데이트 구분자(재구 수량 변경)
                        inParams.setStock(item.getSt11Stok()); // 보관장소
                        inParams.setSt01District(item.getSt11Stok()); // 보관장소
                        // 수량 세팅
                        inParams.setOutQty(0); // 빼기
                        inParams.setIpQty(item.getSt02Qty());  // 더하기

                        boolean pdStockInResult = outputMapper.updateOfPdStock(inParams);

                        if (!pdStockInResult) {
                            throw new BusinessException("재고 수량(입고) 수정에 실패하였습니다.");
                        }
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                    throw new BusinessException("반납 등록에 실패하였습니다.");
                }
            });
        }
		
		return true;
	}
	
	
    
}
