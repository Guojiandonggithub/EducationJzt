package org.springside.examples.bootapi.repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gongzhen on 2018/6/3.
 */
public class Response {
    public static Map<String,Object> responseJson(int code,String msg,Object data){
        Map<String,Object> result=new HashMap<>();
        result.put("code",code);
        result.put("msg",msg);
        result.put("data",data);
        return  result;
    }
    public static Map<String,Object> responseJson(int code,String msg){
        Map<String,Object> result=new HashMap<>();
        result.put("code",code);
        result.put("msg",msg);
        return  result;
    }
}
