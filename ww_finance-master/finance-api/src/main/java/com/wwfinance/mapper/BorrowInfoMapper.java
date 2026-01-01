package com.wwfinance.mapper;

import com.wwfinance.entity.BorrowInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

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
}
