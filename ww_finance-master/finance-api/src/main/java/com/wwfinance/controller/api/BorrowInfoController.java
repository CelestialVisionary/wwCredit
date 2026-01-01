package com.wwfinance.controller.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwfinance.entity.BorrowInfo;
import com.wwfinance.entity.dto.BorrowInfoApprovalDTO;
import com.wwfinance.service.BorrowInfoService;
import com.wwfinance.utils.TokenUtil;
import com.wwfinance.common.result.PccAjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/api/core/borrowInfo")
@Slf4j
public class BorrowInfoController {

    @Resource
    private BorrowInfoService borrowInfoService;

    /**
     * 获取借款额度
     * @param authorizationHeader 认证头
     * @return 借款额度
     */
    @Operation(summary = "获取借款额度")
    @GetMapping("/getBorrowAmount")
    public PccAjaxResult getBorrowAmount(@RequestHeader("Authorization") String authorizationHeader) {
        // 从token中获取用户ID
        Long userId = getUserIdFromToken(authorizationHeader);
        BigDecimal borrowAmount = borrowInfoService.getBorrowAmount(userId);
        return new PccAjaxResult(200, "获取借款额度成功", borrowAmount);
    }

    /**
     * 提交借款申请
     * @param borrowInfo 借款信息
     * @param authorizationHeader 认证头
     * @return 操作结果
     */
    @Operation(summary = "提交借款申请")
    @PostMapping("/save")
    public PccAjaxResult saveBorrowInfo(@RequestBody BorrowInfo borrowInfo, @RequestHeader("Authorization") String authorizationHeader) {
        // 从token中获取用户ID
        Long userId = getUserIdFromToken(authorizationHeader);
        borrowInfoService.saveBorrowInfo(borrowInfo, userId);
        return new PccAjaxResult(200, "提交借款申请成功");
    }

    /**
     * 获取借款信息列表（分页）
     * @param current 当前页码
     * @param limit 每页记录数
     * @param keyword 关键字搜索
     * @param status 状态筛选
     * @return 借款信息分页列表
     */
    @Operation(summary = "获取借款信息列表")
    @GetMapping("/list")
    public PccAjaxResult listBorrowInfo(@RequestParam Long current, @RequestParam Long limit, 
                                        @RequestParam(required = false) String keyword, 
                                        @RequestParam(required = false) Integer status) {
        Page<BorrowInfo> pageParam = new Page<>(current, limit);
        Page<BorrowInfo> borrowInfoPage = borrowInfoService.listPage(pageParam, keyword, status);
        return new PccAjaxResult(200, "获取借款信息列表成功", borrowInfoPage);
    }

    /**
     * 获取借款信息详情
     * @param id 借款ID
     * @return 借款信息详情
     */
    @Operation(summary = "获取借款信息详情")
    @GetMapping("/detail/{id}")
    public PccAjaxResult getBorrowInfoDetail(@PathVariable Long id) {
        BorrowInfo borrowInfo = borrowInfoService.getBorrowInfoDetail(id);
        return new PccAjaxResult(200, "获取借款信息详情成功", borrowInfo);
    }

    /**
     * 审批借款信息
     * @param borrowInfoApprovalDTO 审批信息
     * @return 操作结果
     */
    @Operation(summary = "审批借款信息")
    @PostMapping("/approval")
    public PccAjaxResult approval(@RequestBody BorrowInfoApprovalDTO borrowInfoApprovalDTO) {
        borrowInfoService.approval(borrowInfoApprovalDTO);
        return new PccAjaxResult(200, "审批借款信息成功");
    }

    /**
     * 获取借款人的借款列表
     * @param authorizationHeader 认证头
     * @return 借款列表
     */
    @Operation(summary = "获取借款人的借款列表")
    @GetMapping("/borrower/list")
    public PccAjaxResult getBorrowListByUserId(@RequestHeader("Authorization") String authorizationHeader) {
        // 从token中获取用户ID
        Long userId = getUserIdFromToken(authorizationHeader);
        List<BorrowInfo> borrowInfoList = borrowInfoService.getBorrowListByUserId(userId);
        return new PccAjaxResult(200, "获取借款人的借款列表成功", borrowInfoList);
    }

    /**
     * 从token中获取用户ID
     * @param authorizationHeader 认证头
     * @return 用户ID
     */
    private Long getUserIdFromToken(String authorizationHeader) {
        String token = authorizationHeader;
        log.info("token:" + token);
        Map<String, String> tokenInfo = TokenUtil.getMapInfoFromToken(token);
        String mobile = tokenInfo.get("token_phone");
        return Long.parseLong(tokenInfo.get("token_user_id"));
    }
}