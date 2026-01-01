package com.wwfinance.client;

import com.wwfinance.client.fallback.CoreUserInfoClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-core", url = "http://localhost:8120", fallback = CoreUserInfoClientFallback.class)
public interface CoreUserInfoClient {

    @GetMapping("/api/core/user/checkMobile/{mobile}")
    boolean checkMobile(@PathVariable String mobile);
}
