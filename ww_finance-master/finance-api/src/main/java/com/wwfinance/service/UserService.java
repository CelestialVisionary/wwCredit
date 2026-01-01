package com.wwfinance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwfinance.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwfinance.entity.dto.AdminUserQuery;
import com.wwfinance.entity.dto.UserIndexDTO;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 */
public interface UserService extends IService<User> {
    IPage<User> listPage(Page<User> pageParam, AdminUserQuery adminUserQuery);

    void lock(Long id, Integer status);

    UserIndexDTO getIndexUserInfo(Long userId);
}
