package com.denso.pdabackend.domain.material.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.material.dto.MaterialsReleaseDto.Info;
import com.denso.pdabackend.domain.material.dto.MaterialsReleaseDto.Request;
import com.denso.pdabackend.domain.material.mapper.MaterialsReleaseMapper;
import com.denso.pdabackend.domain.output.mapper.OutputMapper;
import com.denso.pdabackend.domain.smd.mapper.SmdInputMapper;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialsReleaseService {
    
    private final OutputMapper outputMapper;
    private final SmdInputMapper smdInputMapper;
    private final MaterialsReleaseMapper materialsReleaseMapper;

	public Map<String, Object> getMaterialsRelease(Map<String,Object> params) throws Exception {
		return materialsReleaseMapper.getMaterialsRelease(params);
	}

	public Map<String, Object> getSeq(Request request) throws Exception {
		return materialsReleaseMapper.getSeq(request);
	}

	public Map<String, Object> getDuplicationMaterialsRelease(Request request) throws Exception {
		return materialsReleaseMapper.getDuplicationMaterialsRelease(request);
	}

	public boolean insertOfOutputHistory(List<Info> insertList) throws Exception {
		
		insertList.forEach(item -> {
			
            try {
                // 재고테이블 재고 감소
                StockDto.Info stockInfo = new StockDto.Info();
                BeanUtils.copyProperties(item, stockInfo);
                stockInfo.setOperator("minus");
                stockInfo.setCompany(item.getSt02Company());
                stockInfo.setFactory(item.getSt02Factory());
                stockInfo.setOutQty(item.getSt02Qty()); // 출고수량
                stockInfo.setCode(item.getSt02Code()); // 품목코드
                stockInfo.setStock(item.getSt02Stok()); // 보관창고
                stockInfo.setLot(item.getSt02Lot()); // LOT번호
                stockInfo.setGbn(item.getSt02Gbn()); // 품목구분
                stockInfo.setUnt(item.getSt02Ipunt()); // 출고단위 = 재고단위
                stockInfo.setLotSeq(String.valueOf(item.getSt02LotSeq())); // lotSEQ
                outputMapper.updateOfPdStock(stockInfo);

                // 출고 테이블 등록
                materialsReleaseMapper.insertOfOutputHistory(item);

            } catch(Exception e) {
                e.printStackTrace();
            }
        });
		
        return true;
		
	}

	public boolean insertOfInputHistory(List<Info> insertList) throws Exception {
		
		insertList.forEach(item -> {
			
            try {
                // 재고테이블 재고 증가
                StockDto.Info stockInfo = new StockDto.Info();
                BeanUtils.copyProperties(item, stockInfo);
                stockInfo.setOperator("plus");
                stockInfo.setCompany(item.getSt02Company());
                stockInfo.setFactory(item.getSt02Factory());
                stockInfo.setOutQty(item.getSt02Qty()); // 출고수량
                stockInfo.setIpQty(item.getSt02Qty()); // 출고수량
                stockInfo.setCode(item.getSt02Code()); // 품목코드
                stockInfo.setStock(item.getSt02Stok()); // 보관창고
                stockInfo.setLot(item.getSt02Lot()); // LOT번호
                stockInfo.setUnt(item.getSt02Ipunt()); // 출고단위 = 재고단위
                stockInfo.setLotSeq(String.valueOf(item.getSt02LotSeq())); // lotSEQ
                outputMapper.updateOfPdStock(stockInfo);
                
                stockInfo.setStock(item.getStok()); // 보관창고
                stockInfo.setSt01District(item.getDist()); // 보관창고
                smdInputMapper.updateOfStok(stockInfo);

                // 출고 테이블 등록
                materialsReleaseMapper.insertOfInputHistory(item);

            } catch(Exception e) {
                e.printStackTrace();
            }
        });
		
        return true;
		
	}

}


