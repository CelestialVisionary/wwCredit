package com.wwfinance.mapper;

import com.wwfinance.entity.BorrowerAttach;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.io.Serializable;
import java.util.List;

// 关键：删除所有注解，仅保留接口定义
public interface BorrowerAttachMapper extends BaseMapper<BorrowerAttach> {
    // 基础 CRUD 方法（MyBatis-Plus 自带，此处显式声明方便使用）
    @Override
    BorrowerAttach selectById(Serializable id);

    @Override
    int insert(BorrowerAttach entity);

    @Override
    int updateById(BorrowerAttach entity);

    @Override
    int deleteById(Serializable id);

    // 自定义查询示例（根据实际业务调整）
    List<BorrowerAttach> selectByBorrowerId(Long borrowerId);
}