package com.denso.pdabackend.domain.pcb.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.pcb.dto.FaultDto.Info;
import com.denso.pdabackend.domain.pcb.dto.FaultDto.Request;

@Mapper
public interface FaultMapper {

	Map<String, Object> getSeq(Request request);
	
	Map<String, Object> getFaultInfo(Request request);
	
	boolean insertOfFault(Info item);

}
