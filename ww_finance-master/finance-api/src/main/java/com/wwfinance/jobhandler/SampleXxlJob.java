package com.wwfinance.jobhandler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wwfinance.client.SMSApiSmsClient;
import com.wwfinance.entity.LendReturn;
import com.wwfinance.entity.User;
import com.wwfinance.mapper.LendReturnMapper;
import com.wwfinance.service.UserService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@Slf4j
public class SampleXxlJob {


    @Resource
    private SMSApiSmsClient smsApiSmsClient;

    @Resource
    private LendReturnMapper lendReturnMapper;

    @Resource
    private UserService userService;

    @XxlJob("demoJobHandler")
    public void demoJobHandler(){
        log.info("开始执行...");
    }

    @XxlJob( "listJobHandler")
    public void list() {
        // 1.获取所有未还的还款计划列表
        QueryWrapper<LendReturn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0);
        List<LendReturn> list = lendReturnMapper.selectList(queryWrapper);
        // 2.遍历还款计划列表，判断剩余天数是否小于等于3天，如果小于，则调用短信服务API发送催还短信
        for (LendReturn lendReturn : list) {

            // 获取当前日期
            LocalDate currentDate = LocalDate.now();

            // 计算剩余天数
            long daysRemaining = ChronoUnit.DAYS.between(currentDate, lendReturn.getReturnDate());
            log.info(String.valueOf(daysRemaining));
            if (daysRemaining <= 30) {
                // 调用容联云提供的短信服务API完成发送短信
                QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id", lendReturn.getUserId());
                User user = userService.getOne(queryWrapper1);
                smsApiSmsClient.sendMsg(user.getMobile());
                log.info("还款时间剩余天数小于等于3天，请尽快还款");
            }
        }
    }

}
