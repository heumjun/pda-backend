package com.denso.pdabackend.domain.restore.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.restore.dto.ProductDto;
import com.denso.pdabackend.domain.restore.dto.RestoreDto;
import com.denso.pdabackend.domain.restore.dto.RestoreDto.Request;
import com.denso.pdabackend.domain.restore.dto.RestoreMgmtDto;

@Mapper
public interface RestoreMapper {

	List<Map<String, Object>> getRestoreList(Request param);
	
	List<Map<String, Object>> getRestoreMgmtInfo(RestoreMgmtDto.Request request);

	/**
     * 창고/구역 정보가 변경되었는지 확인
     * @param info
     * @return true : 변경, false : 변경사항 없음
     * @throws Exception
     */
    boolean getChangeDataYn(RestoreDto.Info info);
    
    /**
     * 반납등록
     * @param info
     * @return
     * @throws Exception
     */
    boolean updateReturnForReg(RestoreDto.Info info);
    
    /**
     * 출고 등록
     * @param info
     * @return
     * @throws Exception
     */
    boolean saveOfProductionOutput(RestoreDto.Info info);
    
    /**
     * 입고 등록
     * @param info
     * @return
     * @throws Exception
     */
    boolean saveOfProductionInput(RestoreDto.Info info);
    
    /**
     * 재고 등록
     * @param info
     * @return
     * @throws Exception
     */
    boolean insertOfPdStock(RestoreDto.Info info);

	List<Map<String, Object>> getProductInfo(ProductDto.Request pdtRequest);
}
