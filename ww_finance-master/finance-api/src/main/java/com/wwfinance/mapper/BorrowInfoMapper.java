package com.wwfinance.mapper;

import com.wwfinance.entity.BorrowInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 借款信息表 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface BorrowInfoMapper extends BaseMapper<BorrowInfo> {

    List<BorrowInfo> selectBorrowInfoList();
    
    /**
     * 根据ID查询借款信息详情
     * @param id 借款ID
     * @return 借款信息详情
     */
    BorrowInfo selectBorrowInfoById(Long id);
    
    /**
     * 根据用户ID查询借款信息
     * @param userId 用户ID
     * @return 借款信息列表
     */
    List<BorrowInfo> selectBorrowInfoByUserId(Long userId);
    
    /**
     * 根据状态查询借款信息
     * @param status 状态
     * @return 借款信息列表
     */
    List<BorrowInfo> selectBorrowInfoByStatus(Integer status);
    
    /**
     * 借款信息统计查询
     * @return 统计结果
     */
    Map<String, Object> selectBorrowInfoStatistics();
    
    /**
     * 分页查询借款信息
     * @param page 分页对象
     * @param map 查询条件
     * @return 分页结果
     */
    Page<BorrowInfo> selectBorrowInfoPage(Page<BorrowInfo> page, Map<String, Object> map);
}
