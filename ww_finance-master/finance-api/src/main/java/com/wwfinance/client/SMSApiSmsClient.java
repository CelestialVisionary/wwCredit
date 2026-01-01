package com.wwfinance.client;

import com.wwfinance.client.fallback.SMSApiSmsClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-sms", fallback = SMSApiSmsClientFallback.class)
public interface SMSApiSmsClient {
    @GetMapping("/api/sms/user/sendMsg/{mobile}")
    String sendMsg( @PathVariable String mobile);
}
