package com.wwfinance.service.impl;


import com.wwfinance.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Override
    public void send(String mobile, String templateCode, Map<String, Object> param) {

    }
}
