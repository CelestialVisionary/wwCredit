package com.wwfinance.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "custom.sms-util")
@Data
public class Msg {
    //生产环境请求地址
    private String serverIp;

    //请求端口
    private String serverPort;

    //    免费开发测试使用的模板ID为1
    private String templateId;

    //    应用的APPID
    private String appId;

    //    开发者主账号ACCOUNT SID
    private String accountSId;

    //    主账号令牌AUTH TOKEN
    private String accountToken;
}
