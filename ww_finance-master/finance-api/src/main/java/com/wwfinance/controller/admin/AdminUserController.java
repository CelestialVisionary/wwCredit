package com.wwfinance.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwfinance.entity.User;
import com.wwfinance.entity.dto.AdminUserQuery;
import com.wwfinance.service.UserService;
import com.wwfinance.common.result.PccAjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/admin/core/user")
@Slf4j
public class AdminUserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "获取会员分页列表")
    @PostMapping("/list/{page}/{limit}")
    public PccAjaxResult listPage(
            @Parameter(description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(description = "每页记录数", required = true)
            @PathVariable Long limit,
            @Parameter(description = "查询对象", required = false)
            @RequestBody AdminUserQuery adminUserQuery) {
        Page<User> pageParam = new Page<>(page, limit);
        IPage<User> pageModel = userService.listPage(pageParam, adminUserQuery);
        return new PccAjaxResult(200, "获取会员分页列表成功", pageModel);
    }

    @Operation(summary = "锁定和解锁")
    @PostMapping("/lock/{id}/{status}")
    public PccAjaxResult lock(
            @Parameter(description = "用户id", required = true)
            @PathVariable("id") Long id,
            @Parameter(description = "锁定状态（0：锁定 1：解锁）", required = true)
            @PathVariable("status") Integer status){
        userService.lock(id, status);
        return new PccAjaxResult(200, status==1?"解锁成功":"锁定成功");
    }

}

