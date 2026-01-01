package com.wwfinance.mapper;

import com.wwfinance.entity.UserBind;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户绑定表 Mapper 接口
 * </p>
 *
 */
public interface UserBindMapper extends BaseMapper<UserBind> {

    UserBind getBindInfoByUserId(long userId);
}
