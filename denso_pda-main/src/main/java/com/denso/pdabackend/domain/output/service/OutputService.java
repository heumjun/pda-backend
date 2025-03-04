package com.denso.pdabackend.domain.output.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.output.dto.OutputRequestDto;
import com.denso.pdabackend.domain.output.dto.OutputSearchDto;
import com.denso.pdabackend.domain.output.dto.OutputSearchDto.Info;
import com.denso.pdabackend.domain.output.mapper.OutputMapper;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutputService {
    
    private final OutputMapper outputMapper;

	public List<Map<String, Object>> getOutputRequestSearch(OutputSearchDto.Request params) {
		return outputMapper.getOutputRequestSearch(params);
	}

	public List<Map<String, Object>> getOutputRequestSel(OutputRequestDto.Request params) {
		return outputMapper.getOutputRequestSel(params);
	}

	public Map<String, Object> getOutputHistorySearchInfo(OutputSearchDto.Request params) {
		 List<Map<String,Object>> list = outputMapper.getOutputHistorySearchInfo(params);
	        if(list.size()>0)
	            return list.get(0);
	        return null;
	}

	public Map<String, Object> inspectChk(OutputSearchDto.Request params) {
		List<Map<String,Object>> list = outputMapper.inspectChk(params);
        if(list.size()>0)
            return list.get(0);
        return null;
	}

	public Map<String, Object> inspectSpecChk(OutputSearchDto.Request params) {
		List<Map<String,Object>> list = outputMapper.inspectSpecChk(params);
        if(list.size()>0)
            return list.get(0);
        return null;
	}

	public boolean insertOfOutputHistory(List<OutputSearchDto.Info> insertList) {
		insertList.forEach(item ->{
            try{
                // 재고테이블 재고 감소
                StockDto.Info stockInfo = new StockDto.Info();
                BeanUtils.copyProperties(item, stockInfo);
                stockInfo.setOperator("minus");
                stockInfo.setOutQty(item.getSt03Qty()); // 출고수량
                stockInfo.setCode(item.getCm08Code()); // 품목코드
                stockInfo.setStock(item.getSt03Stok()); // 보관창고
                stockInfo.setLot(item.getSt03Lot()); // LOT번호
                stockInfo.setGbn(item.getCm08Gbn()); // 품목구분
                stockInfo.setUnt(item.getSt03Unt()); // 출고단위 = 재고단위
                stockInfo.setLotSeq(String.valueOf(item.getSt03LotSeq())); // lotSEQ
                stockMapper.updateOfPdStock(stockInfo);

                // 출고 테이블 등록
                outputMapper.insertOfOutputHistory(item);

            }catch(Exception e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
		
        return true;
	}
}
