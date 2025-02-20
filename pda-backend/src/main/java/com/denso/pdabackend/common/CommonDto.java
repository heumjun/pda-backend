package com.denso.pdabackend.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data               //getter,setter 자동생성
@NoArgsConstructor  //인자값없는 생성자 생성
@SuperBuilder       //builder 패턴으로 자식들이 부모까지 생성가능토록함.
public abstract class CommonDto {

    private String jan;            //지점(1001:창원본점,1002:마산점,1003:진해점 등)
    
}
