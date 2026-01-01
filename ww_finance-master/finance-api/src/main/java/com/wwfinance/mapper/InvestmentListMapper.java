package com.wwfinance.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwfinance.entity.InvestmentList;

import java.util.List;

/**
 * <p>
 * 投资列表表 Mapper 接口
 * </p>
 *
 */
public interface InvestmentListMapper extends BaseMapper<InvestmentList> {
    List<InvestmentList> selectAllInvestmentList();
}
