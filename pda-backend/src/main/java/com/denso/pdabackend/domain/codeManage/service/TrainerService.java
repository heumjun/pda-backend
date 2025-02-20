package com.denso.pdabackend.domain.codeManage.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.denso.pdabackend.domain.codeManage.dto.TrainerDto.TrainerInfo;
import com.denso.pdabackend.domain.codeManage.dto.TrainerDto.TrainerRequest;
import com.denso.pdabackend.domain.codeManage.mapper.TrainerMapper;
import com.denso.pdabackend.response.exception.BusinessException;
import com.denso.pdabackend.utils.DensoStringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerMapper trainerMapper;
    /**
     * 트레이너 리스트
     * @param params
     * @return
     */
    public List<Map<String, Object>> getTrainerList(TrainerRequest params) throws Exception {

        if(ObjectUtils.isEmpty(params.getExcludeQuit())) params.setExcludeQuit(false);
        
        return trainerMapper.getTrainerList(params);
    }

    /**
     * 트레이너 저장
     * @param insertList
     * @param updateList
     */
    public void saveOfTrainer(List<TrainerInfo> insertList, List<TrainerInfo> updateList) throws Exception {

        if(ObjectUtils.isEmpty(insertList) && ObjectUtils.isEmpty(updateList)) throw new BusinessException("저장할 내역이 없습니다.");

        TrainerRequest params = TrainerRequest.builder().build();

        for(TrainerInfo info:insertList){
            if(ObjectUtils.isEmpty(info.getTraId())) throw new BusinessException("[아이디]가 없는 내역이 있습니다.");
            if(ObjectUtils.isEmpty(info.getTraName())) throw new BusinessException("[이름]이 없는 내역이 있습니다.");
            if(ObjectUtils.isEmpty(info.getTraEntdt())) throw new BusinessException("[입사일자]가 없는 내역이 있습니다.");
            if(ObjectUtils.isEmpty(info.getTraEmail())) throw new BusinessException("[이메일]이 없는 내역이 있습니다.");
            if(ObjectUtils.isEmpty(info.getTraPhone())) throw new BusinessException("[전화번호]가 없는 내역이 있습니다.");

            //중복아이디 여부 확인    
            params.setId(info.getTraId());
            params.setJan(info.getTraJan());
            trainerMapper.getTrainer(params).ifPresent(
                trainer->{
                    throw new BusinessException("[아이디]"+info.getTraId()+"가 이미 존재합니다.");
                }
            );
            //신규입사자 임시 비밀번호 부여
            String password = DensoStringUtils.getUniqueString(8);
            password = "12345!@";    //테스트용임시
            info.setPassword(password);
            info.setTraPw(DensoStringUtils.encryptSHA256(password));
        }

        for(TrainerInfo info:updateList){
            if(ObjectUtils.isEmpty(info.getTraId())) throw new BusinessException("[아이디]가 없는 내역이 있습니다.");
            if(ObjectUtils.isEmpty(info.getTraName())) throw new BusinessException("[이름]이 없는 내역이 있습니다.");
            if(ObjectUtils.isEmpty(info.getTraEntdt())) throw new BusinessException("[입사일자]가 없는 내역이 있습니다.");
            if(ObjectUtils.isEmpty(info.getTraEmail())) throw new BusinessException("[이메일]이 없는 내역이 있습니다.");
            if(ObjectUtils.isEmpty(info.getTraPhone())) throw new BusinessException("[전화번호]가 없는 내역이 있습니다.");
        }

        List<TrainerInfo> saveList = Stream.concat(insertList.stream(), updateList.stream()).collect(Collectors.toList());

        for(TrainerInfo info:saveList){
            trainerMapper.saveOfTrainer(info);
        }

        //TODO: 신규 입사자 비밀번호 이메일 발송해야함.




    }

    /**
     * 트레이너 정보
     * @param params
     * @return
     */
    public Map<String, Object> getTrainer(TrainerRequest params) throws Exception {
        if(ObjectUtils.isEmpty(params.getId())) throw new BusinessException("아이디가 없습니다.");

        return trainerMapper.getTrainer(params).orElseThrow(()->new BusinessException("트레이너가 존재하지 않습니다."));

    }
    /**
     * 트레이너 삭제
     * @param deleteList
     */
    public void deleteOfTrainer(List<TrainerInfo> deleteList) throws Exception {
        if(ObjectUtils.isEmpty(deleteList)) throw new BusinessException("삭제할 내역이 없습니다.");   

        trainerMapper.deleteOfTrainer(deleteList);
    }

    /**
     * 비밀번호 변경
     * @param trainerInfo
     */
    public void changeOfPassword(TrainerInfo trainerInfo) throws Exception {
        if(ObjectUtils.isEmpty(trainerInfo.getTraId())) throw new BusinessException("[아이디]가 없는 내역이 있습니다.");
        if(ObjectUtils.isEmpty(trainerInfo.getTraPw())) throw new BusinessException("[비밀번호]가 없는 내역이 있습니다.");
        if(ObjectUtils.isEmpty(trainerInfo.getTraJan())) throw new BusinessException("[지점코드]가 없는 내역이 있습니다.");

        //비밀번호 암호화
        trainerInfo.setTraPw(DensoStringUtils.encryptSHA256(trainerInfo.getTraPw()));

        trainerMapper.changeOfPassword(trainerInfo);
    }

    public List<Map<String, Object>> getCustomerCountByTrainer(String jan) throws Exception {
        return trainerMapper.getCustomerCountByTrainer(jan);
    }

}
