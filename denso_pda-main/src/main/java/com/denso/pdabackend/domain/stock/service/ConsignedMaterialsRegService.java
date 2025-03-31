package com.denso.pdabackend.domain.stock.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.restore.dto.ProductDto;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto;
import com.denso.pdabackend.domain.stock.dto.ConsignedMaterialsRegDto.Info;
import com.denso.pdabackend.domain.stock.mapper.ConsignedMaterialsRegMapper;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.response.StatusCode;
import com.denso.pdabackend.response.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsignedMaterialsRegService {

	private final ConsignedMaterialsRegMapper consignedMaterialsRegMapper;

	public List<Map<String, Object>> consignedMaterialsRegDetailList(ConsignedMaterialsRegDto.Request request) {
		return consignedMaterialsRegMapper.consignedMaterialsRegDetailList(request);
	}

	public boolean saveConsignedMaterialsReq(List<Info> insertList) throws Exception {
		
		String company = null; // 회사
        String factory = null; // 공장
        String mf15Cus = null; // 제조사
        String mf15No = null; // 사급번호
        String inputMode = null; // 기준모드
        
        
        // 수정 키 값 체크
        if (insertList != null ) {

            for(ConsignedMaterialsRegDto.Info info: insertList) {

                if(info.getMf16Code() == null || info.getMf16Code() == ""){
                    throw new BusinessException("품목이 존재하지 않아 수정할 수 없습니다.");
                }
                if(Double.isNaN(info.getMf16Qty()) || BigDecimal.valueOf(info.getMf16Qty()).compareTo(BigDecimal.ZERO) <= 0){
                    throw new BusinessException("수량이 존재하지 않아 수정할 수 없습니다.");
                }
                if(info.getMf16Gbn() == null || !("G001".equals(info.getMf16Gbn()) || "G002".equals(info.getMf16Gbn()))){
                    throw new BusinessException("구분이 존재하지 않아 수정할 수 없습니다.");
                }

                // 품목 체크
                ProductDto.Request pdtRequest = new ProductDto.Request(); // 객체 생성

                // 입력받은 데이터 할당
                pdtRequest.setCm08Code(info.getMf16Code());
                pdtRequest.setCompany(info.getMf16Company());
                pdtRequest.setFactory(info.getMf16Factory());

                // TODO
//                List<Map<String,Object>> pdResultMap = productMapper.getProductInfo(pdtRequest); // DB 조회
//                if (pdResultMap == null || pdResultMap.size() == 0) { // 품목코드가 존재하지 않을 경우
//                    throw new BusinessException ("존재하지 않는 품번은 등록할 수 없습니다.");
//                }

            }
        }
        
        // 등록 - 상세
        if (insertList != null) {
            String finalMf15No = mf15No;
            insertList.forEach(item -> {
                try {
                    item.setMf16No(null); // 저장시 생성됩니다. 값 변경.
                    item.setMf16Hno(finalMf15No);
                    consignedMaterialsRegMapper.saveConsignedMaterialsReqDetail(item);
                } catch(Exception e) {
                    e.printStackTrace();
                    new RuntimeException();
                }
            });
        }
		
		return true;
	}
	
	
}
