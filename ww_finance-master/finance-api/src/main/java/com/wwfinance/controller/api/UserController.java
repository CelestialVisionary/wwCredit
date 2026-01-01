package com.wwfinance.controller.api;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wwfinance.entity.User;
import com.wwfinance.entity.UserAccount;
import com.wwfinance.entity.UserLoginRecord;
import com.wwfinance.entity.dto.UserDTO;
import com.wwfinance.entity.dto.UserIndexDTO;
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

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserLoginRecordMapper userLoginRecordMapper;


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
        String mobile = user.getMobile();

        //获取密码
        String password = user.getPassword();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getMobile, user.getMobile());

        User emp = userService.getOne(queryWrapper);
        if(emp == null) {
            return new PccAjaxResult(500, "用户名错误");
        }
        if(emp.getUserType() != user.getUserType()) {
            return new PccAjaxResult(500, "用户不存在");
        }

        if(!emp.getPassword().equals(MD5.encrypt(password))) {
            return new PccAjaxResult(500, "密码错误");
        }
        //记录登录日志
        String ip = request.getRemoteHost();
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(emp.getId());
        userLoginRecord.setIp(ip);
        userLoginRecordMapper.insert(userLoginRecord);

        String token = TokenUtil.generateMerchantToken(mobile);

        return new PccAjaxResult(200, "登录成功", token);

    }

    @Operation(summary = "检查手机号是否可用")
    @GetMapping("/checkMobile/{mobile}")
    public boolean checkMobile(@PathVariable String mobile) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getMobile, mobile);

        User user = userService.getOne(queryWrapper);
        if(user != null) {
            return false;
        }
        return true;
    }


    @Operation(summary = "获取用户信息")
    @GetMapping("userInfo")
    public PccAjaxResult userInfo(@RequestHeader("Authorization") String authorizationHeader) {
        // 获取 Authorization 头部
        String token = authorizationHeader;
        log.info("token:" + token);
        // 通过token获取手机号
        Map<String, String> phone = TokenUtil.getMapInfoFromToken(token);

        log.info(phone.toString());

        String mobile = (String) phone.get("token_phone");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getMobile, mobile);

        User user = userService.getOne(queryWrapper);

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
    public PccAjaxResult register(@RequestBody UserDTO userDTO, HttpSession session) {
        log.info(userDTO.toString());

        String code = (String)redisTemplate.opsForValue().get("xx:code:" + userDTO.getMobile());
        log.info(code);
        /*if(!(code.equals(userDTO.getCode())) ) {
            return new PccAjaxResult(500, "验证码错误");
        }*/
        if(!(userDTO.getPassword().equals(userDTO.getPasswordto()))) {
            return new PccAjaxResult(500, "两次输入的密码不正确");
        }
        User user = new User();
        user.setMobile(userDTO.getMobile());
        user.setUserType(userDTO.getUserType());
        user.setName(userDTO.getMobile());
        user.setNickName(userDTO.getMobile());
        user.setPassword(MD5.encrypt(userDTO.getPassword()));
        user.setStatus(1);
        userService.save(user);

        //插入用户账户记录：user_account
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(user.getId());
       // userAccountService.save(userAccount);

        return new PccAjaxResult(200, "注册成功");

    }



    /**
     * 退出登录
     * @param
     * @return
     */
    @Operation(summary = "退出登录")
    @GetMapping("/logout")
    public PccAjaxResult logout() {

        return new PccAjaxResult(200, "退出成功");
    }

    @Operation(summary = "获取个人空间用户信息")
    @GetMapping("/auth/getIndexUserInfo")
    public PccAjaxResult getIndexUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        // 获取 Authorization 头部
        String token = authorizationHeader;
        log.info("token:" + token);
        // 通过token获取手机号
        Map<String, String> phone = TokenUtil.getMapInfoFromToken(token);

        log.info(phone.toString());

        String mobile = (String) phone.get("token_phone");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getMobile, mobile);

        User user = userService.getOne(queryWrapper);
        UserIndexDTO userIndexDTO = userService.getIndexUserInfo(user.getId());
        return new PccAjaxResult(200, "获取成功", userIndexDTO);
    }
}

