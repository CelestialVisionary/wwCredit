package com.wwfinance.service.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwfinance.entity.User;
import com.wwfinance.entity.UserAccount;
import com.wwfinance.entity.UserLoginRecord;
import com.wwfinance.entity.dto.AdminUserQuery;
import com.wwfinance.entity.dto.UserIndexDTO;
import com.wwfinance.mapper.UserAccountMapper;
import com.wwfinance.mapper.UserLoginRecordMapper;
import com.wwfinance.mapper.UserMapper;
import com.wwfinance.service.UserService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserAccountMapper userAccountMapper;

    @Resource
    private UserLoginRecordMapper userLoginRecordMapper;

    @Override
    public IPage<User> listPage(Page<User> pageParam, AdminUserQuery adminUserQuery) {
        String mobile = adminUserQuery.getMobile();
        Integer status = adminUserQuery.getStatus();
        Integer userType = adminUserQuery.getUserType();
        QueryWrapper<User> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper
                .eq(StringUtils.isNotBlank(mobile), "mobile", mobile)
                .eq(status != null, "status", adminUserQuery.getStatus())
                .eq(userType != null, "user_type", userType);
        return baseMapper.selectPage(pageParam, userInfoQueryWrapper);
    }

    @Override
    public void lock(Long id, Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        baseMapper.updateById(user);
    }

    @Override
    public UserIndexDTO getIndexUserInfo(Long userId) {

        //用户信息
        User user = baseMapper.selectById(userId);
        //账户信息
        QueryWrapper<UserAccount> userAccountQueryWrapper = new QueryWrapper<>();
        userAccountQueryWrapper.eq("user_id", userId);
        UserAccount userAccount = userAccountMapper.selectOne(userAccountQueryWrapper);
        //登录信息
        QueryWrapper<UserLoginRecord> userLoginRecordQueryWrapper = new QueryWrapper<>();
        userLoginRecordQueryWrapper
                .eq("user_id", userId)
                .orderByDesc("id")
                .last("limit 1");
        UserLoginRecord userLoginRecord = userLoginRecordMapper.selectOne(userLoginRecordQueryWrapper);

        //组装结果数据
        UserIndexDTO userIndexDTO = new UserIndexDTO();
        userIndexDTO.setUserId(user.getId());
        userIndexDTO.setUserType(user.getUserType());
        userIndexDTO.setName(user.getName());
        userIndexDTO.setNickName(user.getNickName());
        userIndexDTO.setHeadImg(user.getHeadImg());
        userIndexDTO.setBindStatus(user.getBindStatus());
        userIndexDTO.setAmount(userAccount.getAmount());
        userIndexDTO.setFreezeAmount(userAccount.getFreezeAmount());
        userIndexDTO.setLastLoginTime(userLoginRecord == null ? null :userLoginRecord.getCreateTime());
        return userIndexDTO;
    }
}
