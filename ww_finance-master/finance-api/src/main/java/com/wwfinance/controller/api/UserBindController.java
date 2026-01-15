package com.wwfinance.controller.api;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wwfinance.entity.User;
import com.wwfinance.entity.UserBind;
import com.wwfinance.entity.dto.UserBindDTO;
import com.wwfinance.xxBank.RequestHelper;
import com.wwfinance.mapper.UserBindMapper;
import com.wwfinance.service.UserBindService;
import com.wwfinance.service.UserService;
import com.wwfinance.utils.TokenUtil;
import com.wwfinance.common.result.PccAjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/api/core/userBind")
@Slf4j
public class UserBindController {

    @Resource
    private UserBindService userBindService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserBindMapper userBindMapper;

    // 移除实例化，直接使用静态方法

    // 获取绑定信息
    @GetMapping("/getBindInfo")
    public PccAjaxResult getBindInfo(@RequestHeader("Authorization") String authorizationHeader) {
        // 获取 Authorization 头部
        String token = authorizationHeader;
        log.info("token:" + token);
        // 通过token获取手机号
        Map<String, String> phone = TokenUtil.getMapInfoFromToken(token);

        log.info(phone.toString());

        String mobile = (String) phone.get("token_phone");

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(mobile!=null, User::getPhone, mobile);
        User user = userService.getOne(lambdaQueryWrapper);
        Long userId = Long.valueOf(user.getId());
        // 获取绑定信息
        UserBind bindInfoByUserId = userBindMapper.getBindInfoByUserId(userId);

        return new PccAjaxResult(200, "获取成功", bindInfoByUserId);
    }

    @Operation(summary = "账户绑定提交数据")
    @PostMapping("/auth/bind")
    public PccAjaxResult bind(@RequestBody UserBindDTO userBindDTO, @RequestHeader("Authorization") String authorizationHeader) {

        // 获取 Authorization 头部
        String token = authorizationHeader;
        log.info("token:" + token);
        // 通过token获取手机号
        Map<String, String> phone = TokenUtil.getMapInfoFromToken(token);

        log.info(phone.toString());

        String mobile = (String) phone.get("token_phone");

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(mobile!=null, User::getPhone, mobile);
        User user = userService.getOne(lambdaQueryWrapper);
        Long userId = Long.valueOf(user.getId());
        String formStr = userBindService.commitBindUser(userBindDTO, userId);
        return new PccAjaxResult(200, "账户提交绑定数据成功", formStr);
    }

    @Operation(summary = "账户绑定异步回调")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("用户账号绑定异步回调：" + JSON.toJSONString(paramMap));

        //校验签名
        if(!RequestHelper.isSignEquals(paramMap)) {
            log.error("用户账号绑定异步回调签名错误：" + JSON.toJSONString(paramMap));
            return "fail";
        }

        //修改绑定状态
        userBindService.notify(paramMap);
        return "success";
    }
}


