package com.denso.pdabackend.domain.partsInput.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.partsInput.dto.PartsInputRequestDto;
import com.denso.pdabackend.domain.partsInput.dto.PartsInputRequestDto.Info;
import com.denso.pdabackend.domain.partsInput.mapper.PartsInputMapper;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartsInputService {

    private final PartsInputMapper partsInputMapper;

    public Map<String, Object> getPartsInputRequestInfo(PartsInputRequestDto.Request params) {
		return partsInputMapper.getPartsInputRequestInfo(params);
	}

	public boolean insertOfPartsInputHistory(List<Info> insertList) {
		// TODO Auto-generated method stub
		insertList.forEach(item ->{
            try{
                // 재고테이블 재고 감소
                StockDto.Info stockInfo = new StockDto.Info();
                BeanUtils.copyProperties(item, stockInfo);
                stockInfo.setOperator("minus");
                stockInfo.setOutQty(Double.parseDouble(item.getSt02Ipqty())); // 출고수량
                stockInfo.setCode(item.getSt02Code()); // 품목코드
                stockInfo.setStock(item.getSt02Stok()); // 보관창고
                stockInfo.setLot(item.getSt02Lot()); // LOT번호
                stockInfo.setGbn(item.getCm08Gbn()); // 품목구분
                stockInfo.setUnt(item.getSt02Ipunt()); // 출고단위 = 재고단위
                stockInfo.setLotSeq(String.valueOf(item.getSt02LotSeq())); // lotSEQ
                partsInputMapper.updateOfPdStock(stockInfo);
                partsInputMapper.updateOfSmdStock(stockInfo);
                // 히스토리 테이블 등록 필요.. 히스토리 테이블이 없어요~~
                //outputMapper.insertOfOutputHistory(item);

            }catch(Exception e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        return true;
	}

}
