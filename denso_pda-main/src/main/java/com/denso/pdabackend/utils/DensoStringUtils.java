package com.denso.pdabackend.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import com.denso.pdabackend.response.exception.BusinessException;

/**
 * String 관련 util class
 */
public final class DensoStringUtils {

    /**
     * UUID로 생성한 고유ID 반환
     * <pre>
     * String id = StringUtils.getUniqueString(13)
     * </pre>
     * @param digit 생성하고싶은 자릿수
     * @return String
     * @since 2024-11-21
     * @author 정윤재
     */
    public static String getUniqueString(int digit){
        if(digit <1) throw new BusinessException("생성할 자릿수를 입력하세요.");
        
        //return UUID.randomUUID().toString().replace("-", "").substring(0, digit);

        //uuid는 32개문자인데 원하는 자리로 잘라내기 때문에 중복확률이 생길 여지가 있어 (100만개 생성시 0.5% 확률)
        //중복확률을 없애기 위해서 uuid를 sha-256알고리즘으로 변경후 파라미터로 넘어온 자리수로 잘라냄
        
        //1. ID 생성
        String uuid = UUID.randomUUID().toString();

        //2. SHA-256 알고리즘으로 변환
        uuid = encryptSHA256(uuid);
        
        return uuid.substring(0,digit);
        
    }

    /**
     * SHA256 암호화
     * @param ch
     * @return
     */
    public static String encryptSHA256(String ch){
        
        try {
            MessageDigest digest;
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(ch.getBytes(StandardCharsets.UTF_8));
        
            StringBuilder hexString = new StringBuilder();
            for(byte b:hash){
                String hex = Integer.toHexString(0xff & b);
                if (hex.length()==1){
                    hexString.append('0');  //한자리일경우 앞에 0 추가
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            return ch;
        }

    }

}
