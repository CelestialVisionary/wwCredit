package com.wwfinance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwfinance.entity.IntegralGrade;
import com.wwfinance.mapper.IntegralGradeMapper;
import com.wwfinance.service.IntegralGradeService;
import org.springframework.stereotype.Service;

/**
 *  类去实现接口
 */
@Service
public class IntegralGradeServiceImpl extends ServiceImpl<IntegralGradeMapper, IntegralGrade> implements IntegralGradeService {
    @Override
    public IntegralGrade getByIntegral(Integer integral) {
        QueryWrapper<IntegralGrade> wrapper = new QueryWrapper<>();
        wrapper.le("integral_start", integral)
               .ge("integral_end", integral)
               .eq("is_deleted", false);
        return baseMapper.selectOne(wrapper);
    }
}
