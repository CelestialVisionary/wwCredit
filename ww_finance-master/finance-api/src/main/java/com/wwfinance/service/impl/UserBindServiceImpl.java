package com.wwfinance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wwfinance.entity.User;
import com.wwfinance.entity.UserBind;
import com.wwfinance.entity.dto.UserBindDTO;
import com.wwfinance.enums.UserBindEnum;
import com.wwfinance.xxBank.FormHelper;
import com.wwfinance.xxBank.HfbConst;
import com.wwfinance.xxBank.RequestHelper;
import com.wwfinance.mapper.UserBindMapper;
import com.wwfinance.mapper.UserMapper;
import com.wwfinance.service.UserBindService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务实现类
 * </p>
 *
 */
@Service
public class UserBindServiceImpl extends ServiceImpl<UserBindMapper, UserBind> implements UserBindService {

    @Resource
    private UserMapper userMapper;

    @Override
    public String commitBindUser(UserBindDTO userBindDTO, Long userId) {
        //查询身份证号码是否绑定
        QueryWrapper<UserBind> userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper
                .eq("id_card", userBindDTO.getIdCard())
                .ne("user_id", userId);
        UserBind userBind = baseMapper.selectOne(userBindQueryWrapper);

        if (userBind != null) {
            return "身份证号码已绑定";
        }
        //查询用户绑定信息
        userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper.eq("user_id", userId);
        userBind = baseMapper.selectOne(userBindQueryWrapper);
        //判断是否有绑定记录
        if (userBind == null) {
            //如果未创建绑定记录，则创建一条记录
            userBind = new UserBind();
            BeanUtils.copyProperties(userBindDTO, userBind);
            userBind.setUserId(userId);
            userBind.setStatus(UserBindEnum.NO_BIND.getStatus());
            baseMapper.insert(userBind);
        } else {
            //曾经跳转到托管平台，但是未操作完成，此时将用户最新填写的数据同步到userBind对象
            BeanUtils.copyProperties(userBindDTO, userBind);
            baseMapper.updateById(userBind);
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentUserId", userId);
        paramMap.put("idCard", userBindDTO.getIdCard());
        paramMap.put("personalName", userBindDTO.getName());
        paramMap.put("bankType", userBindDTO.getBankType());
        paramMap.put("bankNo", userBindDTO.getBankNo());
        paramMap.put("mobile", userBindDTO.getMobile());
        paramMap.put("returnUrl", HfbConst.USERBIND_RETURN_URL);
        paramMap.put("notifyUrl", HfbConst.USERBIND_NOTIFY_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        paramMap.put("sign", RequestHelper.getSign(paramMap));
        //构建充值自动提交表单
        String formStr = FormHelper.buildForm(HfbConst.USERBIND_URL, paramMap);
        return formStr;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notify(Map<String, Object> paramMap) {

        String bindCode = (String)paramMap.get("bindCode");
        //会员id
        String agentUserId = (String)paramMap.get("agentUserId");

        //根据user_id查询user_bind记录
        QueryWrapper<UserBind> userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper.eq("user_id", agentUserId);

        //更新用户绑定表
        UserBind userBind = baseMapper.selectOne(userBindQueryWrapper);
        userBind.setBindCode(bindCode);
        userBind.setStatus(UserBindEnum.BIND_OK.getStatus());
        baseMapper.updateById(userBind);

        //更新用户表
        User user = userMapper.selectById(agentUserId);
        // user.setBindCode(bindCode); // User实体类中没有该字段
        user.setName(userBind.getName());
        user.setIdCard(userBind.getIdCard());
        // user.setBindStatus(UserBindEnum.BIND_OK.getStatus()); // User实体类中没有该字段
        userMapper.updateById(user);
    }

    @Override
    public String getBindCodeByUserId(Long userId){
        QueryWrapper<UserBind> userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper.eq("user_id", userId);
        UserBind userBind = baseMapper.selectOne(userBindQueryWrapper);
        String bindCode = userBind.getBindCode();
        return bindCode;
    }
}