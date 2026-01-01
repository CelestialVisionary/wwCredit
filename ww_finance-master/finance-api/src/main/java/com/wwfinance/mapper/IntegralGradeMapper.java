package com.wwfinance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwfinance.entity.IntegralGrade;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 积分等级表 Mapper 接口
 * </p>
 */
@Repository // 替换为 @Repository，Spring 会自动识别并扫描
public interface IntegralGradeMapper extends BaseMapper<IntegralGrade> {

}