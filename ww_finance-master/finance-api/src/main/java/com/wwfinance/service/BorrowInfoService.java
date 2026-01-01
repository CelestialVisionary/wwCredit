package com.wwfinance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwfinance.entity.BorrowInfo;
import com.wwfinance.entity.dto.BorrowInfoApprovalDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 借款信息 服务类
 * </p>
 */
public interface BorrowInfoService extends IService<BorrowInfo> {

    /**
     * 获取借款额度
     * @param userId 用户ID
     * @return 借款额度
     */
    BigDecimal getBorrowAmount(Long userId);

    /**
     * 提交借款申请
     * @param borrowInfo 借款信息
     * @param userId 用户ID
     */
    void saveBorrowInfo(BorrowInfo borrowInfo, Long userId);

    /**
     * 获取借款信息列表（分页）
     * @param pageParam 分页参数
     * @param keyword 关键字搜索
     * @param status 状态筛选
     * @return 借款信息分页列表
     */
    Page<BorrowInfo> listPage(Page<BorrowInfo> pageParam, String keyword, Integer status);

    /**
     * 获取借款信息详情
     * @param id 借款ID
     * @return 借款信息详情
     */
    BorrowInfo getBorrowInfoDetail(Long id);

    /**
     * 审批借款信息
     * @param borrowInfoApprovalDTO 审批信息
     */
    void approval(BorrowInfoApprovalDTO borrowInfoApprovalDTO);

    /**
     * 获取借款人的借款列表
     * @param userId 用户ID
     * @return 借款列表
     */
    List<BorrowInfo> getBorrowListByUserId(Long userId);
}