package com.denso.pdabackend.domain.mfr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.denso.pdabackend.domain.mfr.dto.MfrPartsInputRequestDto;
import com.denso.pdabackend.domain.mfr.mapper.MfrPartsInputMapper;
import com.denso.pdabackend.domain.warehousing.dto.StockDto;
import com.denso.pdabackend.utils.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MfrPartsInputService {

    private final MfrPartsInputMapper mfrPartsInputMapper;

    public Map<String, Object> getPartsInputRequestInfo(MfrPartsInputRequestDto.Request params) {
		return mfrPartsInputMapper.getPartsInputRequestInfo(params);
	}

    public Map<String, Object> getMfrPartsInputInfo(Map<String,Object> params) throws Exception {
        return mfrPartsInputMapper.getMfrPartsInputInfo(params);
    }

    /**
     * 부품투입 ( SMD 출고하고 현장입고)
     * @param insertParam
     * @return
     */
	public boolean insertOfPartsInputHistory(Map<String, Object> insertParam) {
		// TODO Auto-generated method stub

		// 생산지시서 마스터 생성 필요 tb_mf_01
		mfrPartsInputMapper.createMfOrder(insertParam);

		log.debug("{}", insertParam);

		List<MfrPartsInputRequestDto.Info> insertList = (List<MfrPartsInputRequestDto.Info>) insertParam.get("insertList");
		insertList.forEach(item ->{
            try{
                // 재고테이블 재고 감소
                StockDto.Info stockInfo = new StockDto.Info();
                BeanUtils.copyProperties(item, stockInfo);
                stockInfo.setOperator("minus");
                stockInfo.setOutQty(Double.parseDouble(item.getSt01Qty())); // 출고수량
                stockInfo.setCode(item.getSt01Code()); // 품목코드
                stockInfo.setStock(item.getSt01Stok()); // 보관창고
                stockInfo.setSt01District( StringUtils.nullString(insertParam.get("compMfLine"))); // 보관창고
                stockInfo.setLot(item.getSt01Lot()); // LOT번호
                stockInfo.setGbn(item.getCm08Gbn()); // 품목구분
                stockInfo.setUnt(item.getSt01Unt()); // 출고단위 = 재고단위
                stockInfo.setLotSeq(String.valueOf(item.getSt01LotSeq())); // lotSEQ
                //재고 감소
                mfrPartsInputMapper.updateOfPdStock(stockInfo);

                stockInfo.setStock(item.getSt01Stok()); // 보관창고
                stockInfo.setSt01District(StringUtils.nullString(insertParam.get("compMfLine"))); // 보관창고
                mfrPartsInputMapper.updateOfStok(stockInfo);

                // 생산 지시서 디테일 생성 -- tb_mf_01 ?
                item.setMf02Pcc((String)insertParam.get("mf01Pcc"));
                mfrPartsInputMapper.createMfOrderDetail(item);
                mfrPartsInputMapper.insertOfSmdOutStock(item);
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        return true;
	}

	public List<Map<String, Object>> getCompMfList(MfrPartsInputRequestDto.Request params) {
		return mfrPartsInputMapper.getCompMfList(params);
	}

}
