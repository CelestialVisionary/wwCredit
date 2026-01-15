package com.wwfinance.config;

import com.wwfinance.mapper.*;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.annotation.DbType;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.Resource;

@Configuration
// 关键：移除@MapperScan，完全手动注册所有Mapper
public class MyBatisConfig {

    // 注入 MyBatis 的 SqlSessionTemplate
    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    // 配置MyBatis Plus分页插件
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    // 手动注册所有Mapper Bean，完全绕过注解扫描和解析
    @Bean
    public AnnouncementMapper announcementMapper() {
        return sqlSessionTemplate.getMapper(AnnouncementMapper.class);
    }
    
    @Bean
    public BorrowInfoMapper borrowInfoMapper() {
        return sqlSessionTemplate.getMapper(BorrowInfoMapper.class);
    }
    
    @Bean
    public BorrowerAttachMapper borrowerAttachMapper() {
        return sqlSessionTemplate.getMapper(BorrowerAttachMapper.class);
    }
    
    @Bean
    public BorrowerMapper borrowerMapper() {
        return sqlSessionTemplate.getMapper(BorrowerMapper.class);
    }
    
    @Bean
    public DictMapper dictMapper() {
        return sqlSessionTemplate.getMapper(DictMapper.class);
    }
    
    @Bean
    public IntegralGradeMapper integralGradeMapper() {
        return sqlSessionTemplate.getMapper(IntegralGradeMapper.class);
    }
    
    @Bean
    public InvestmentListMapper investmentListMapper() {
        return sqlSessionTemplate.getMapper(InvestmentListMapper.class);
    }
    
    @Bean
    public LendItemMapper lendItemMapper() {
        return sqlSessionTemplate.getMapper(LendItemMapper.class);
    }
    
    @Bean
    public LendItemReturnMapper lendItemReturnMapper() {
        return sqlSessionTemplate.getMapper(LendItemReturnMapper.class);
    }
    
    @Bean
    public LendMapper lendMapper() {
        return sqlSessionTemplate.getMapper(LendMapper.class);
    }
    
    @Bean
    public LendReturnMapper lendReturnMapper() {
        return sqlSessionTemplate.getMapper(LendReturnMapper.class);
    }
    
    @Bean
    public TransFlowMapper transFlowMapper() {
        return sqlSessionTemplate.getMapper(TransFlowMapper.class);
    }
    
    @Bean
    public UserAccountMapper userAccountMapper() {
        return sqlSessionTemplate.getMapper(UserAccountMapper.class);
    }
    
    @Bean
    public UserBindMapper userBindMapper() {
        return sqlSessionTemplate.getMapper(UserBindMapper.class);
    }
    
    @Bean
    public UserIntegralMapper userIntegralMapper() {
        return sqlSessionTemplate.getMapper(UserIntegralMapper.class);
    }
    
    @Bean
    public UserLoginRecordMapper userLoginRecordMapper() {
        return sqlSessionTemplate.getMapper(UserLoginRecordMapper.class);
    }
    
    @Bean
    public UserMapper userMapper() {
        return sqlSessionTemplate.getMapper(UserMapper.class);
    }
}