package com.wwfinance.xxBank;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wwfinance.common.utils.HttpUtils;
import com.wwfinance.common.utils.MD5;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class RequestHelper {

    public static void main(String[] args) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("d", "4");
        paramMap.put("b", "2");
        paramMap.put("c", "3");
        paramMap.put("a", "1");
    }

    /**
     * 请求数据获取签名
     * @param paramMap
     * @return
     */
    public static String getSign(Map<String, Object> paramMap) {
        if(paramMap.containsKey("sign")) {
            paramMap.remove("sign");
        }
        TreeMap<String, Object> sorted = new TreeMap<>(paramMap);
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, Object> param : sorted.entrySet()) {
            str.append(param.getValue()).append("|");
        }
        str.append(HfbConst.SIGN_KEY);
        log.info("加密前：" + str.toString());
        String md5Str = MD5.encrypt(str.toString());
        log.info("加密后：" + md5Str);
        return md5Str;
    }

    /**
     * Map转换
     * @param paramMap
     * @return
     */
    public static Map<String, Object> switchMap(Map<String, String[]> paramMap) {
        Map<String, Object> resultMap = new HashMap<>();
        for (Map.Entry<String, String[]> param : paramMap.entrySet()) {
            resultMap.put(param.getKey(), param.getValue()[0]);
        }
        return resultMap;
    }

    /**
     *  签名校验
     * @param paramMap
     * @return
     */
    public static boolean isSignEquals(Map<String, Object> paramMap) {
        String sign = (String)paramMap.get("sign");
        String md5Str = getSign(paramMap);
        if(!sign.equals(md5Str)) {
            return false;
        }
        return true;
    }

    /**
     * 获取时间戳
     * @return
     */
    public static long getTimestamp() {
        // 使用java.time API替代旧的Date API
        return Instant.now().toEpochMilli();
    }

    /**
     * 封装同步请求
     * @param paramMap
     * @param url
     * @return
     */
    public static JSONObject sendRequest(Map<String, Object> paramMap, String url){
        String result = "";
        try {
            //封装post参数
            StringBuilder postdata = new StringBuilder();
            for (Map.Entry<String, Object> param : paramMap.entrySet()) {
                postdata.append(param.getKey()).append("=")
                        .append(param.getValue()).append("&");
            }
            log.info(String.format("--> 发送请求到威武银行：post data %1s", postdata));
            byte[] reqData = postdata.toString().getBytes("utf-8");
            byte[] respdata = HttpUtils.doPost(url,reqData);
            result = new String(respdata);
            log.info(String.format("--> 威武银行应答结果：result data %1s", result));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONObject.parseObject(result);
    }
    
    /**
     * 将Map转换为字符串
     * @param map
     * @return
     */
    public static String convertMapToString(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=")
              .append(entry.getValue()).append("&");
        }
        // 移除最后一个&符号
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
    
    /**
     * 将字符串转换为Map
     * @param str
     * @return
     */
    public static Map<String, Object> convertStringToMap(String str) {
        Map<String, Object> map = new HashMap<>();
        if (str == null || str.isEmpty()) {
            return map;
        }
        String[] pairs = str.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }
    
    /**
     * 将对象转换为Map
     * @param obj
     * @return
     */
    public static Map<String, Object> convertObjectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            log.error("Object to Map conversion error: " + e.getMessage(), e);
        }
        return map;
    }
    
    /**
     * 将Map转换为JSON字符串
     * @param map
     * @return
     */
    public static String convertMapToJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * 将JSON字符串转换为Map
     * @param json
     * @return
     */
    public static Map<String, Object> convertJsonToMap(String json) {
        Map<String, Object> map = new HashMap<>();
        if (json == null || json.isEmpty()) {
            return map;
        }
        try {
            map = JSON.parseObject(json, Map.class);
        } catch (Exception e) {
            log.error("JSON to Map conversion error: " + e.getMessage(), e);
        }
        return map;
    }
    
    /**
     * 合并两个Map
     * @param map1
     * @param map2
     * @return
     */
    public static Map<String, Object> mergeMaps(Map<String, Object> map1, Map<String, Object> map2) {
        Map<String, Object> mergedMap = new HashMap<>(map1);
        mergedMap.putAll(map2);
        return mergedMap;
    }
    
    /**
     * 过滤Map中的空值
     * @param map
     * @return
     */
    public static Map<String, Object> filterEmptyValues(Map<String, Object> map) {
        Map<String, Object> filteredMap = new HashMap<>();
        if (map == null || map.isEmpty()) {
            return filteredMap;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().toString().isEmpty()) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }
}
