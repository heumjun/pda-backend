package com.denso.pdabackend.domain.codeManage.mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.denso.pdabackend.domain.codeManage.dto.TrainerDto.TrainerInfo;
import com.denso.pdabackend.domain.codeManage.dto.TrainerDto.TrainerRequest;

@Mapper
public interface TrainerMapper {

    List<Map<String, Object>> getTrainerList(TrainerRequest params);

    Optional<Map<String, Object>>  getTrainer(TrainerRequest params);

    void saveOfTrainer(TrainerInfo info);

    void deleteOfTrainer(List<TrainerInfo> deleteList);

    void changeOfPassword(TrainerInfo trainerInfo);

    List<Map<String, Object>> getCustomerCountByTrainer(String jan);

}
