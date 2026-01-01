package com.wwfinance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwfinance.entity.IntegralGrade;

public interface IntegralGradeService extends IService<IntegralGrade> {
    IntegralGrade getByIntegral(Integer integral);
}
