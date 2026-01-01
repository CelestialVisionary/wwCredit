package com.wwfinance.controller.api;

import com.wwfinance.client.CoreUserInfoClient;
import com.wwfinance.common.result.PccAjaxResult;
import com.wwfinance.util.SMSUtil;
import com.wwfinance.util.ValidateCodeUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sms/user")
@Tag(name = "短信管理")
//@CrossOrigin //跨域
@Slf4j
public class ApiSmsController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private SMSUtil smsUtil;

    @Resource
    private CoreUserInfoClient coreUserInfoClient;

    @Operation(summary = "获取验证码")
    @PostMapping("/sendSMS")
    public PccAjaxResult send(
            @Parameter(description = "手机号", required = true)
            @RequestParam String mobile){

        if(mobile != null) {

            boolean result = coreUserInfoClient.checkMobile(mobile);
            if(!result) {
                return new PccAjaxResult(500, "手机号已被注册");
            }

            // 生成随机的四位验证码
//            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //调用容联云提供的短信服务API完成发送短信
            String code2 = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
            String code = smsUtil.sendMsg(mobile, code2);

            log.info("code={}", code);
            //将验证码存入redis
            redisTemplate.opsForValue().set("xx:code:" + mobile, code, 5, TimeUnit.MINUTES);
            }
            return new PccAjaxResult(200, "手机验证码短信发送成功");
    }

    @Operation(summary = "发送催还款信息")
    @GetMapping("/sendMsg/{mobile}")
    public String sendMsg(
            @Parameter(description = "手机号", required = true)
            @PathVariable String mobile){
        //调用容联云提供的短信服务API完成发送短信
        String code = "还款时间剩余天数小于等于3天，请尽快还款";
        String msg = smsUtil.sendMsg(mobile, code);

        return msg;
    }
}
