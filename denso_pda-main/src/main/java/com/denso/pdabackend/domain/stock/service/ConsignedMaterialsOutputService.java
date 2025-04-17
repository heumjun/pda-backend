package com.denso.pdabackend.domain.stock.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.output.mapper.OutputMapper;
import com.denso.pdabackend.domain.restore.dto.ProductDto;
import com.denso.pdabackend.domain.restore.mapper.RestoreMapper;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsOutputDto;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsOutputDto.DetailInfo;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsOutputDto.MasterInfo;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto.Request;
import com.denso.pdabackend.domain.stock.mapper.ConsignedMaterialsOutputMapper;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;
import com.denso.pdabackend.response.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsignedMaterialsOutputService {
	
	private final OutputMapper outputMapper;
	
	private final RestoreMapper restoreMapper;

	private final ConsignedMaterialsOutputMapper consignedMaterialsOutputMapper;

	public List<Map<String, Object>> getComboCusList(Request request) throws Exception {
		return consignedMaterialsOutputMapper.getComboCusList(request);
	}

	public Map<String, Object> getConsignedMaterialsOutput(Map<String, Object> params) throws Exception {
		return consignedMaterialsOutputMapper.getConsignedMaterialsOutput(params);
	}

	public Map<String, Object> getConsignedMaterialsOutputCheck(Map<String, Object> params) throws Exception {
		return consignedMaterialsOutputMapper.getConsignedMaterialsOutputCheck(params);
	}

	public Map<String, Object> createMf15No(ConsignedMaterialsOutputDto.Request request) throws Exception {
		return consignedMaterialsOutputMapper.createMf15No(request);
	}

	public boolean saveConsignedMaterialsOutput(MasterInfo masterInfo, List<DetailInfo> insertList) throws Exception {
		
		try {
		
			// 사급 마스터 등록
			consignedMaterialsOutputMapper.insertConsignedMaster(masterInfo);
			
			// 사급 상세 등록
			if (insertList != null ) {
	
	            for(ConsignedMaterialsOutputDto.DetailInfo detailInfo: insertList) {
	
	                if(detailInfo.getMf16Code() == null || detailInfo.getMf16Code() == ""){
	                    throw new BusinessException("품목이 존재하지 않아 등록할 수 없습니다.");
	                }
	                if(Double.isNaN(detailInfo.getSt02Moq()) || BigDecimal.valueOf(detailInfo.getSt02Moq()).compareTo(BigDecimal.ZERO) <= 0){
	                    throw new BusinessException("MOQ가 존재하지 않아 수정할 수 없습니다.");
	                }
	
	                // 품목 체크
	                ProductDto.Request pdtRequest = new ProductDto.Request(); // 객체 생성
	
	                // 입력받은 데이터 할당
	                pdtRequest.setCm08Code(detailInfo.getMf16Code());
	                pdtRequest.setCompany(detailInfo.getMf16Company());
	                pdtRequest.setFactory(detailInfo.getMf16Factory());
	
	                List<Map<String,Object>> pdResultMap = restoreMapper.getProductInfo(pdtRequest); // DB 조회
	                if (pdResultMap == null || pdResultMap.size() == 0) { // 품목코드가 존재하지 않을 경우
	                    throw new BusinessException ("존재하지 않는 품목코드는 등록할 수 없습니다.");
	                }
	
	                // LOT번호 체크
	                ConsignedMaterialsRegDto.Lot lotParams = new ConsignedMaterialsRegDto.Lot();
	                lotParams.setCompany(detailInfo.getMf16Company());
	                lotParams.setFactory(detailInfo.getMf16Factory());
	                lotParams.setSt01Code(detailInfo.getMf16Code());
	                lotParams.setLot(detailInfo.getSt02Lot());
	                lotParams.setLotSeq(detailInfo.getSt02LotSeq());
	
	                List<Map<String, Object>> lotList = consignedMaterialsOutputMapper.getProdStockList(lotParams);
	
	                if (lotList == null || lotList.size() == 0) { // LOT번호가 존재하지 않을 경우
	                    throw new BusinessException ("존재하지 않는 LOT번호 및 재고량이 0인 품목의 LOT는 등록할 수 없습니다.");
	                }
	                
	                // 사급 상세 등록
	                consignedMaterialsOutputMapper.insertConsignedDetail(detailInfo);
	                
	                // 출고 테이블 이력 등록
	                consignedMaterialsOutputMapper.saveOfProductionOutput(detailInfo);
	        		
	        		// 재고 테이블 수량 업데이트
	                if (detailInfo != null && ("".equals(detailInfo.getSt03No()) || detailInfo.getSt03No() == null)) {
	                	detailInfo.setSt03No(detailInfo.getSt03NoNew());
	                }
	                
	                // 재고수량 변경
	                StockDto.Info params = new StockDto.Info();
	                params.setCompany(detailInfo.getMf16Company()); // 회사
	                params.setFactory(detailInfo.getMf16Factory()); // 공장
	                params.setCode(detailInfo.getMf16Code());  // 품목
	                params.setLot(detailInfo.getSt02Lot()); // LOT
	                params.setLotSeq(detailInfo.getSt02LotSeq()); // LOT 시퀀스
	                params.setOperator("change"); // 업데이트 구분자(재구 수량 변경)
	                params.setStock(detailInfo.getSt02Stok()); // 보관장소
	                // 수량 세팅
	                params.setOutQty(detailInfo.getSt02Qty()); // 빼기
	                params.setIpQty(0);  // 더하기
	
	                boolean pdStockResult = outputMapper.updateOfPdStock(params);
	
	                if (!pdStockResult) {
	                    throw new BusinessException("재고 수량 수정에 실패하였습니다.");
	                }
	                
	            }
	            
	        }
		
		} catch(Exception e) {
            throw new BusinessException("사급등록에 실패하였습니다.");
        }
		
		return true;
	}
	
}
