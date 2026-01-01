package com.wwfinance.service.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwfinance.entity.BorrowInfo;
import com.wwfinance.entity.Borrower;
import com.wwfinance.entity.User;
import com.wwfinance.entity.dto.BorrowInfoApprovalDTO;
import com.wwfinance.enums.BorrowInfoStatusEnum;
import com.wwfinance.mapper.BorrowInfoMapper;
import com.wwfinance.mapper.BorrowerMapper;
import com.wwfinance.mapper.UserMapper;
import com.wwfinance.service.BorrowInfoService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 借款信息 服务实现类
 * </p>
 */
@Service
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements BorrowInfoService {

    @Resource
    private BorrowInfoMapper borrowInfoMapper;

    @Resource
    private BorrowerMapper borrowerMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public BigDecimal getBorrowAmount(Long userId) {
        // 1. 获取用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 获取借款人信息
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.eq("user_id", userId);
        Borrower borrower = borrowerMapper.selectOne(borrowerQueryWrapper);
        if (borrower == null) {
            return BigDecimal.ZERO;
        }

        // 3. 根据借款人资质计算借款额度
        // 这里简化处理，实际应该根据用户的信用评级、收入情况等因素计算
        BigDecimal borrowAmount = BigDecimal.ZERO;
        switch (borrower.getEducation()) {
            case 1: // 本科
                borrowAmount = new BigDecimal(50000);
                break;
            case 2: // 硕士
                borrowAmount = new BigDecimal(80000);
                break;
            case 3: // 博士
                borrowAmount = new BigDecimal(100000);
                break;
            default: // 其他学历
                borrowAmount = new BigDecimal(30000);
        }

        // 根据月收入调整借款额度
        if (borrower.getIncome() != null) {
            borrowAmount = borrowAmount.add(new BigDecimal(borrower.getIncome()).multiply(new BigDecimal(12)));
        }

        return borrowAmount;
    }

    @Override
    public void saveBorrowInfo(BorrowInfo borrowInfo, Long userId) {
        // 1. 验证借款额度
        BigDecimal borrowAmount = getBorrowAmount(userId);
        if (borrowInfo.getAmount().compareTo(borrowAmount) > 0) {
            throw new RuntimeException("借款金额超过可借额度");
        }

        // 2. 设置借款信息
        borrowInfo.setUserId(userId);
        borrowInfo.setStatus(BorrowInfoStatusEnum.CHECK_RUN.getStatus()); // 审核中

        // 3. 保存借款信息
        baseMapper.insert(borrowInfo);
    }

    @Override
    public Page<BorrowInfo> listPage(Page<BorrowInfo> pageParam, String keyword, Integer status) {
        QueryWrapper<BorrowInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .like(StringUtils.isNotBlank(keyword), "title", keyword)
                .eq(status != null, "status", status)
                .orderByDesc("create_time");
        return baseMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public BorrowInfo getBorrowInfoDetail(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public void approval(BorrowInfoApprovalDTO borrowInfoApprovalDTO) {
        // 1. 获取借款信息
        BorrowInfo borrowInfo = baseMapper.selectById(borrowInfoApprovalDTO.getId());
        if (borrowInfo == null) {
            throw new RuntimeException("借款信息不存在");
        }

        // 2. 更新借款状态
        borrowInfo.setStatus(borrowInfoApprovalDTO.getStatus());
        baseMapper.updateById(borrowInfo);

        // 3. 如果审核通过，可以在这里添加生成还款计划、放款等后续逻辑
        if (borrowInfoApprovalDTO.getStatus().equals(BorrowInfoStatusEnum.CHECK_OK.getStatus())) {
            // TODO: 生成还款计划
            // TODO: 放款处理
        }
    }

    @Override
    public List<BorrowInfo> getBorrowListByUserId(Long userId) {
        QueryWrapper<BorrowInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id", userId)
                .orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }
}