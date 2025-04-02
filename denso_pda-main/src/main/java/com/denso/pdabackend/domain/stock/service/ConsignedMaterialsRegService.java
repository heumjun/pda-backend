package com.denso.pdabackend.domain.stock.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.output.mapper.OutputMapper;
import com.denso.pdabackend.domain.restore.dto.ProductDto;
import com.denso.pdabackend.domain.restore.mapper.RestoreMapper;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto.Info;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto.Request;
import com.denso.pdabackend.domain.stock.mapper.ConsignedMaterialsRegMapper;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;
import com.denso.pdabackend.response.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsignedMaterialsRegService {

	private final OutputMapper outputMapper;
	
	private final RestoreMapper restoreMapper;
	
	private final ConsignedMaterialsRegMapper consignedMaterialsRegMapper;
	
	public List<Map<String, Object>> consignedMaterialsRegDetailList(Request request) {
		return consignedMaterialsRegMapper.consignedMaterialsRegDetailList(request);
	}

	public boolean saveConsignedMaterialsReq(ConsignedMaterialsRegDto.MasterInfo masterInfo, List<Info> insertList) throws Exception {
		
		String company = null; // 회사
        String factory = null; // 공장
        String mf15Cus = null; // 제조사
        String mf15No = null; // 사급번호

        if (masterInfo != null ) {
            
            company = masterInfo.getMf15Company();
            factory = masterInfo.getMf15Factory();
            mf15Cus = masterInfo.getCm01Code(); // 거래처코드
            mf15No = masterInfo.getMf15No(); // 사급요청번호

            if (mf15No == null || "".equals(mf15No)) {
                throw new BusinessException("사급요청서번호가 없어 등록할 수 없습니다.");
            }

            // 사급요청정보 체크
            ConsignedMaterialsRegDto.Request ckRequest = new ConsignedMaterialsRegDto.Request();
            ckRequest.setCompany(company);   // 회사
            ckRequest.setFactory(factory);   // 공장
            ckRequest.setMf15No(mf15No);     // 사급번호

            List<Map<String, Object>> resultMapList = consignedMaterialsRegMapper.searchConsignedMaterialsReqInfo(ckRequest);
            if (resultMapList == null || resultMapList.size() <= 0) {
                throw new BusinessException("등록되지 않은 사급정보는 수정할 수 없습니다.");
            }

        } else {
            throw new BusinessException("사급정보가 없어 등록할 수 없습니다.");
        }
        
        if (insertList != null ) {

            for(ConsignedMaterialsRegDto.Info info: insertList){

                if(info.getCm08Gbn() == null || info.getCm08Gbn() == ""){
                    throw new BusinessException("품목구분이 존재하지 않아 등록할 수 없습니다.");
                }
                if(info.getMf16Code() == null || info.getMf16Code() == ""){
                    throw new BusinessException("품목이 존재하지 않아 등록할 수 없습니다.");
                }
                if(info.getSt03Lot() == null || info.getSt03Lot() == ""){
                    throw new BusinessException("LOT번호가 존재하지 않아 등록할 수 없습니다.");
                }
                if(info.getSt03LotSeq() == null || info.getSt03LotSeq() == ""){
                    throw new BusinessException("LOT시퀀스가 존재하지 않아 등록할 수 없습니다.");
                }
                if(info.getSt02Qrcode() == null || info.getSt02Qrcode() == ""){
                    throw new BusinessException("QR코드가 존재하지 않아 등록할 수 없습니다.");
                }
                if(Double.isNaN(info.getCm08Moq()) || BigDecimal.valueOf(info.getCm08Moq()).compareTo(BigDecimal.ZERO) <= 0){
                    throw new BusinessException("MOQ가 존재하지 않아 수정할 수 없습니다.");
                }
                if(Double.isNaN(info.getSt03Qty()) || BigDecimal.valueOf(info.getSt03Qty()).compareTo(BigDecimal.ZERO) <= 0){
                    throw new BusinessException("박스수량이 존재하지 않아 수정할 수 없습니다.");
                }

                // 품목 체크
                ProductDto.Request pdtRequest = new ProductDto.Request(); // 객체 생성

                // 입력받은 데이터 할당
                pdtRequest.setCm08Code(info.getMf16Code());
                pdtRequest.setCompany(info.getMf16Company());
                pdtRequest.setFactory(info.getMf16Factory());

                List<Map<String,Object>> pdResultMap = restoreMapper.getProductInfo(pdtRequest); // DB 조회
                if (pdResultMap == null || pdResultMap.size() == 0) { // 품목코드가 존재하지 않을 경우
                    throw new BusinessException ("존재하지 않는 품목코드는 등록할 수 없습니다.");
                }

                // LOT번호 체크
                ConsignedMaterialsRegDto.Lot lotParams = new ConsignedMaterialsRegDto.Lot();
                lotParams.setCompany(info.getMf16Company());
                lotParams.setFactory(info.getMf16Factory());
                lotParams.setSt01Code(info.getMf16Code());
                lotParams.setLot(info.getSt03Lot());
                lotParams.setLotSeq(info.getSt03LotSeq());

                List<Map<String, Object>> lotList = consignedMaterialsRegMapper.getProdStockList(lotParams);

                if (lotList == null || lotList.size() == 0) { // LOT번호가 존재하지 않을 경우
                    throw new BusinessException ("존재하지 않는 LOT번호 및 재고량이 0인 품목의 LOT는 등록할 수 없습니다.");
                }

                info.setMf16Hno(mf15No);
                info.setSt03Cus(mf15Cus);

            }
        }
        
        // 등록 - 상세
        if (insertList != null) {
            String finalMf15No = mf15No;
            insertList.forEach(item -> {
                try {
                    // 1. 사급요청 저장
                    //item.setMf16No(null); // 저장시 생성됩니다. 값 변경.
                    item.setMf16Hno(finalMf15No);

                    // 2. 출고 생성
                    consignedMaterialsRegMapper.saveOfProductionOutput(item);

                    if (item != null && ("".equals(item.getSt03No()) || item.getSt03No() == null)) {
                        item.setSt03No(item.getSt03NoNew());
                    }

                    // 출고 번호 등록을 위해
                    // consignedMaterialsHistMapper.saveConsignedMaterialsHistDetail(item);

                    // 3. 재고 수량 변경
                    // 재고수량 변경
                    StockDto.Info params = new StockDto.Info();
                    params.setCompany(item.getMf16Company()); // 회사
                    params.setFactory(item.getMf16Factory()); // 공장
                    params.setCode(item.getMf16Code());  // 품목
                    params.setLot(item.getSt03Lot()); // LOT
                    params.setLotSeq(item.getSt03LotSeq()); // LOT 시퀀스
                    params.setOperator("change"); // 업데이트 구분자(재구 수량 변경)
                    params.setStock(item.getSt01Stok()); // 보관장소
                    // 수량 세팅
                    params.setOutQty(item.getSt03Qty()); // 빼기
                    params.setIpQty(0);  // 더하기

                    boolean pdStockResult = outputMapper.updateOfPdStock(params);

                    if (!pdStockResult) {
                        throw new BusinessException("재고 수량 수정에 실패하였습니다.");
                    }
                } catch(Exception e) {
                    throw new BusinessException("사입등록에 실패하였습니다.");
                }
            });
        }
        
		
		return true;
	}

}
