package com.denso.pdabackend.domain.smd.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.denso.pdabackend.domain.smd.controller.PartsInputController;
import com.denso.pdabackend.domain.smd.dto.PartsInputRequestDto;
import com.denso.pdabackend.domain.smd.dto.PartsInputRequestDto.Info;
import com.denso.pdabackend.domain.smd.dto.PartsInputRequestDto.Request;
import com.denso.pdabackend.domain.smd.mapper.PartsInputMapper;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;
import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.response.StatusCode;
import com.denso.pdabackend.utils.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PartsInputService {

    private final PartsInputMapper partsInputMapper;

    public Map<String, Object> getPartsInputRequestInfo(PartsInputRequestDto.Request params) {
		return partsInputMapper.getPartsInputRequestInfo(params);
	}

    /**
     * 부품투입 ( SMD 출고하고 현장입고)
     * @param insertParam
     * @return
     */
	public boolean insertOfPartsInputHistory(Map<String, Object> insertParam) {

		// 생산지시서 마스터 생성 필요 tb_mf_01
		partsInputMapper.createMfOrder(insertParam);

		log.debug("{}", insertParam);

		List<PartsInputRequestDto.Info> insertList = (List<Info>) insertParam.get("insertList");
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
                //재고 감소
                partsInputMapper.updateOfPdStock(stockInfo);

                // 생산 지시서 디테일 생성 -- tb_mf_01 ?
                item.setMf02Pcc((String)insertParam.get("mf01Pcc"));
                partsInputMapper.createMfOrderDetail(item);
                
                
                System.out.println(">>> " + item.getSt02Line());
                
                partsInputMapper.insertOfSmdOutStock(item);
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        return true;
	}

	public List<Map<String, Object>> getCompMfList(PartsInputRequestDto.Request params) {
		return partsInputMapper.getCompMfList(params);
	}

}
