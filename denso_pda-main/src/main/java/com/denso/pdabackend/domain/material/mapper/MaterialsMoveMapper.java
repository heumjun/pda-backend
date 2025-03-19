package com.denso.pdabackend.domain.material.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.material.dto.MaterialsMoveDto.Info;
import com.denso.pdabackend.domain.material.dto.MaterialsMoveDto.Request;

@Mapper
public interface MaterialsMoveMapper {

	Map<String, Object> getMaterialsMove(Request request) throws Exception;

	Map<String, Object> getSeq(Request request) throws Exception;

	Map<String, Object> getDuplicationMaterialsMove(Request request) throws Exception;

	boolean insertOfOutputHistory(Info item) throws Exception;

	boolean insertOfInputHistory(Info item) throws Exception;

}
