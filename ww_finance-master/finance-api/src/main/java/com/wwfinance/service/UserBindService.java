package com.wwfinance.service;

import com.wwfinance.entity.UserBind;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwfinance.entity.dto.UserBindDTO;

import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 */
public interface UserBindService extends IService<UserBind> {

    /**
     * 账户绑定提交到托管平台的数据
     */
    String commitBindUser(UserBindDTO userBindDTO, Long userId);

    void notify(Map<String, Object> paramMap);

    String getBindCodeByUserId(Long userId);
}
