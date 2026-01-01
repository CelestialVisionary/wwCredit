package com.wwfinance.util;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Set;

@Slf4j
@Component
public class SMSUtil {

    @Resource
    Msg msg;


//    public static void main(String[] args) {
//        sendMsg("13621374472");
//    }
    public String sendMsg(String phone, String code) {
        boolean isFourDigitNumber = code.matches("\\d{4}");
        if(!isFourDigitNumber) {
            log.info(code);
            return code;
        }
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(msg.getServerIp(), msg.getServerPort());
        sdk.setAccount(msg.getAccountSId(), msg.getAccountToken());
        sdk.setAppId(msg.getAppId());
        sdk.setBodyType(BodyType.Type_JSON);
        String to = "18538313405";
        String[] datas = {code};
        HashMap<String, Object> result = sdk.sendTemplateSMS(to,msg.getTemplateId(),datas);
        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            Object dataObj = result.get("data");
            if (dataObj instanceof HashMap) {
                @SuppressWarnings("unchecked")
                HashMap<String, Object> data = (HashMap<String, Object>) dataObj;
                Set<String> keySet = data.keySet();
                for (String key : keySet) {
                    Object object = data.get(key);
                    log.info("key=" + key + " value=" + object);
                }
            }
            return code;
        } else {
            //异常返回输出错误码和错误信息
            log.info( "错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
        }
        return null;
    }


}
