package com.denso.pdabackend.response;

import java.util.HashMap;

import org.springframework.jdbc.support.JdbcUtils;

public class ResultMap extends HashMap<String,Object>{
    
    @Override
    public Object put(String key, Object value){
        return  super.put(JdbcUtils.convertUnderscoreNameToPropertyName((String)key), value);
    }
    
}

