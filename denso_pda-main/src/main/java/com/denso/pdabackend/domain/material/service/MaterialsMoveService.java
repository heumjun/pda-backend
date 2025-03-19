package com.denso.pdabackend.domain.material.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.material.dto.MaterialsMoveDto.Info;
import com.denso.pdabackend.domain.material.dto.MaterialsMoveDto.Request;
import com.denso.pdabackend.domain.material.mapper.MaterialsMoveMapper;
import com.denso.pdabackend.domain.output.mapper.OutputMapper;
import com.denso.pdabackend.domain.warehousing.dto.InspectionConfDto;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;
import com.denso.pdabackend.utils.MapUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialsMoveService {
    
    private final OutputMapper outputMapper;
    private final MaterialsMoveMapper materialsMoveMapper;

	public Map<String, Object> getMaterialsMove(Request request) throws Exception {
		return materialsMoveMapper.getMaterialsMove(request);
	}

	public Map<String, Object> getSeq(Request request) throws Exception {
		return materialsMoveMapper.getSeq(request);
	}

	public Map<String, Object> getDuplicationMaterialsMove(Request request) throws Exception {
		return materialsMoveMapper.getDuplicationMaterialsMove(request);
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
                materialsMoveMapper.insertOfOutputHistory(item);

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

                // 출고 테이블 등록
                materialsMoveMapper.insertOfInputHistory(item);

            } catch(Exception e) {
                e.printStackTrace();
            }
        });
		
        return true;
		
	}

}


