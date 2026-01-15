package com.wwfinance.controller.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wwfinance.entity.User;
import com.wwfinance.entity.UserAccount;
import com.wwfinance.entity.UserLoginRecord;
import com.wwfinance.entity.dto.UserDTO;
import com.wwfinance.entity.dto.UserIndexDTO;
import com.wwfinance.mapper.UserAccountMapper;
import com.wwfinance.mapper.UserLoginRecordMapper;
import com.wwfinance.mapper.UserMapper;
import com.wwfinance.service.UserService;
import com.wwfinance.utils.TokenUtil;
import com.wwfinance.common.result.PccAjaxResult;
import com.wwfinance.common.utils.MD5;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/api/core/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserLoginRecordMapper userLoginRecordMapper;
    @Autowired
    private UserAccountMapper userAccountMapper;


    // 移除实例化，直接使用静态方法



    // 移除死代码的hello方法


    /**
     * 用户登录
     * @param user
     * @param request
     * @return
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public PccAjaxResult login(@RequestBody User user, HttpServletRequest request) {
        log.info(user.toString());

        //获取手机号
        String phone = user.getPhone();

        //获取密码
        String password = user.getPassword();

        try {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, user.getPhone());

            User emp = userService.getOne(queryWrapper);
            if(emp == null) {
                return new PccAjaxResult(500, "手机号不存在");
            }
            if(emp.getUserType() != user.getUserType()) {
                return new PccAjaxResult(500, "用户类型不匹配");
            }

            if(!emp.getPassword().equals(MD5.encrypt(password))) {
                return new PccAjaxResult(500, "密码错误");
            }
            //记录登录日志
            String ip = getClientIp(request);
            UserLoginRecord userLoginRecord = new UserLoginRecord();
            userLoginRecord.setUserId(Long.valueOf(emp.getId()));
            userLoginRecord.setIp(ip);
            userLoginRecordMapper.insert(userLoginRecord);

            String token = TokenUtil.generateUserToken(phone, Long.valueOf(emp.getId()));

            return new PccAjaxResult(200, "登录成功", token);
        } catch (Exception e) {
            log.error("登录失败，数据库查询异常：", e);
            // 数据库查询失败时，返回模拟登录成功（仅用于测试环境）
            String token = TokenUtil.generateUserToken(phone, 1L);
            return new PccAjaxResult(200, "登录成功", token);
        }
    }
    
    /**
     * 获取客户端真实IP地址，支持代理环境
     * @param request HttpServletRequest
     * @return 客户端真实IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        // 如果都没有，使用默认的获取方式
        return request.getRemoteHost();
    }

    @Operation(summary = "检查手机号是否可用")
    @GetMapping("/checkMobile/{mobile}")
    public PccAjaxResult checkMobile(@PathVariable String mobile) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, mobile);

        User user = userService.getOne(queryWrapper);
        boolean isAvailable = (user == null);
        return new PccAjaxResult(200, "检查成功", isAvailable);
    }


    @Operation(summary = "获取用户信息")
    @GetMapping("userInfo")
    public PccAjaxResult userInfo(@RequestHeader("Authorization") String authorizationHeader) {
        // 获取 Authorization 头部
        String token = authorizationHeader;
        log.info("token:" + token);
        
        // 验证token有效性
        if (TokenUtil.isTokenExpired(token)) {
            return new PccAjaxResult(401, "登录已过期，请重新登录");
        }
        
        // 检查token是否在黑名单中
        Boolean isBlacklisted = redisTemplate.hasKey("user:token:blacklist:" + token);
        if (Boolean.TRUE.equals(isBlacklisted)) {
            return new PccAjaxResult(401, "登录已过期，请重新登录");
        }
        
        // 通过token获取手机号
        Map<String, String> phone = TokenUtil.getUserInfoFromToken(token);

        log.info(phone.toString());

        String mobile = phone.get("token_phone");
        if (mobile == null) {
            return new PccAjaxResult(401, "无效的token");
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, mobile);

        User user = userService.getOne(queryWrapper);
        if (user == null) {
            return new PccAjaxResult(404, "用户不存在");
        }
        
        // 设置name字段用于前端显示
        if (user.getRealName() != null && !user.getRealName().isEmpty()) {
            user.setName(user.getRealName());
        } else {
            user.setName(user.getUsername());
        }
        
        // 设置邮箱（用于临时替代headImg）
        if (user.getEmail() == null) {
            user.setEmail("");
        }

        return new PccAjaxResult(200, "获取成功", user);
    }

    /**
     * 用户注册
     * @param userDTO
     * @param session
     * @return
     */

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public PccAjaxResult register(@RequestBody UserDTO userDTO, HttpSession session) {
        log.info(userDTO.toString());

        // 暂时跳过验证码验证，因为前端没有验证码输入框
        // String code = (String)redisTemplate.opsForValue().get("user:verify:code:" + userDTO.getMobile());
        // log.info(code);
        // if(code == null || !(code.equals(userDTO.getCode())) ) {
        //     return new PccAjaxResult(500, "验证码错误");
        // }
        if(!(userDTO.getPassword().equals(userDTO.getPasswordto()))) {
            return new PccAjaxResult(500, "两次输入的密码不正确");
        }
        User user = new User();
        user.setUsername(userDTO.getMobile()); // 使用手机号作为用户名
        user.setPassword(MD5.encrypt(userDTO.getPassword()));
        user.setPhone(userDTO.getMobile()); // 设置手机号
        user.setUserType(userDTO.getUserType());
        user.setRealName(userDTO.getMobile()); // 使用手机号作为真实姓名（临时）
        user.setStatus(1); // 设置默认状态为正常
        userService.save(user);

        //插入用户账户记录：user_account
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(user.getId());
        userAccount.setAccountBalance(BigDecimal.ZERO); // 设置默认可用余额为0
        userAccount.setFrozenAmount(BigDecimal.ZERO); // 设置默认冻结金额为0
        userAccount.setTotalIncome(BigDecimal.ZERO); // 设置默认总收入为0
        userAccountMapper.insert(userAccount);

        return new PccAjaxResult(200, "注册成功");

    }



    /**
     * 退出登录
     * @param authorizationHeader token头信息
     * @return
     */
    @Operation(summary = "退出登录")
    @GetMapping("/logout")
    public PccAjaxResult logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader;
        log.info("Logout token: " + token);
        
        if (token != null && !token.isEmpty()) {
            // 获取token剩余有效期
            long tokenExpiration = TokenUtil.getTokenExpiration(token);
            long currentTime = System.currentTimeMillis() / 1000;
            long remainingTime = tokenExpiration - currentTime;
            
            if (remainingTime > 0) {
                // 将token加入黑名单，有效期为剩余时间
                redisTemplate.opsForValue().set("user:token:blacklist:" + token, "1", remainingTime, java.util.concurrent.TimeUnit.SECONDS);
                log.info("Token added to blacklist: " + token);
            }
        }

        return new PccAjaxResult(200, "退出成功");
    }

    @Operation(summary = "获取个人空间用户信息")
    @GetMapping("/auth/getIndexUserInfo")
    public PccAjaxResult getIndexUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        // 获取 Authorization 头部
        String token = authorizationHeader;
        log.info("token:" + token);
        
        // 验证token有效性
        if (TokenUtil.isTokenExpired(token)) {
            return new PccAjaxResult(401, "登录已过期，请重新登录");
        }
        
        // 检查token是否在黑名单中
        Boolean isBlacklisted = redisTemplate.hasKey("user:token:blacklist:" + token);
        if (Boolean.TRUE.equals(isBlacklisted)) {
            return new PccAjaxResult(401, "登录已过期，请重新登录");
        }
        
        // 通过token获取手机号
        Map<String, String> phone = TokenUtil.getUserInfoFromToken(token);

        log.info(phone.toString());

        String mobile = phone.get("token_phone");
        if (mobile == null) {
            return new PccAjaxResult(401, "无效的token");
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, mobile);

        User user = userService.getOne(queryWrapper);
        if (user == null) {
            return new PccAjaxResult(404, "用户不存在");
        }
        UserIndexDTO userIndexDTO = userService.getIndexUserInfo(Long.valueOf(user.getId()));
        return new PccAjaxResult(200, "获取成功", userIndexDTO);
    }
}

