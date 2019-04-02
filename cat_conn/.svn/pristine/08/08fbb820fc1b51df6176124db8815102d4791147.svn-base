package com.cgltech.cat_conn.util;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class JsonUtil {

    /**
     * 将json转化成map
     * @param jsonStr
     * @return
     */
    public static Map<String, String>   convertJsonStrToMap(String jsonStr){
        
        Map<String, String> map = JSON.parseObject(
                jsonStr,new TypeReference<Map<String, String>>(){} );
        
        return map;
    }

}