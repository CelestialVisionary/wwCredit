package com.wwfinance.controller.admin;


import com.wwfinance.entity.UserLoginRecord;
import com.wwfinance.service.UserLoginRecordService;
import com.wwfinance.common.result.PccAjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户登录记录表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/admin/core/userLoginRecord")
public class AdminUserLoginRecordController {

    @Resource
    private UserLoginRecordService userLoginRecordService;

    @Operation(summary = "获取会员登录日志列表")
    @GetMapping("/listTop50/{userId}")
    public PccAjaxResult listTop50(
            @Parameter(description = "用户id", required = true)
            @PathVariable Long userId) {
        List<UserLoginRecord> userLoginRecordList = userLoginRecordService.listTop50(userId);
        return new PccAjaxResult(200, "列表数据获取成功", userLoginRecordList);
    }



}



