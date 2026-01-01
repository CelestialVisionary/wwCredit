package com.wwfinance.mapper;

import com.wwfinance.entity.LendReturn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 还款记录表 Mapper 接口
 * </p>
 *
 */
public interface LendReturnMapper extends BaseMapper<LendReturn> {

    List<LendReturn> selectLendReturnByUserId(Long userId);

}
