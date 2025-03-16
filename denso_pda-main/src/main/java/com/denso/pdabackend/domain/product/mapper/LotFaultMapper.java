package com.denso.pdabackend.domain.product.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.product.dto.LotFaultDto.Info;
import com.denso.pdabackend.domain.product.dto.LotFaultDto.Request;


@Mapper
public interface LotFaultMapper {

	Map<String, Object> getSeq(Request request);
	
	Map<String, Object> getLotFaultInfo(Request request);
	
	boolean insertOfLotFault(Info item);

}
