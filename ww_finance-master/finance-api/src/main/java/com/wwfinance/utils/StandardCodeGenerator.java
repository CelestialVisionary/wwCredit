package com.wwfinance.utils;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Collections;

/**
 * 标准MyBatis-Plus代码生成器配置
 * 适配Spring Boot 3.x + MyBatis-Plus 3.5.x
 * 确保不使用错误的factoryBeanObjectType字符串参数
 */
public class StandardCodeGenerator {

    public static void main(String[] args) {
        // 修改为你需要生成的表名
        String[] tables = {"borrower_attach"};
        generateCode("ww_finance", tables);
    }

    /**
     * 生成代码
     * @param databaseName 数据库名
     * @param tableNames 表名数组
     */
    public static void generateCode(String databaseName, String... tableNames) {
        FastAutoGenerator.create(
                "jdbc:mysql://localhost:3306/" + databaseName + "?serverTimezone=GMT%2B8&characterEncoding=utf-8", 
                "root", 
                "1234"
        )
        // 全局配置
        .globalConfig(builder -> {
            builder.author("finance-api")
                    .outputDir(System.getProperty("user.dir") + "/src/main/java")
                    .dateType(DateType.TIME_PACK)
                    .commentDate("yyyy-MM-dd");
        })
        // 包配置
        .packageConfig(builder -> {
            builder.parent("com.wwfinance")
                    .entity("entity")
                    .mapper("mapper")
                    .service("service")
                    .serviceImpl("service.impl")
                    .controller("controller")
                    .pathInfo(Collections.singletonMap(OutputFile.xml, 
                            System.getProperty("user.dir") + "/src/main/resources/com/wwfinance/mapper"));
        })
        // 策略配置 - 关键部分：正确配置Mapper注解
        .strategyConfig(builder -> {
            builder.addInclude(tableNames)
                    // 实体类配置
                    .entityBuilder()
                    .enableLombok()
                    .enableChainModel()
                    .naming(NamingStrategy.underline_to_camel)
                    .columnNaming(NamingStrategy.underline_to_camel)
                    .idType(IdType.AUTO)
                    .formatFileName("%s")
                    // Mapper配置 - 重要：仅启用基础@Mapper注解，不添加任何参数
                    .mapperBuilder()
                    // 不使用enableMapperAnnotation()方法，避免生成带有错误参数的@Mapper注解
                    .enableBaseResultMap()
                    .enableBaseColumnList()
                    .formatMapperFileName("%sMapper")
                    .formatXmlFileName("%sMapper")
                    // Service配置
                    .serviceBuilder()
                    .formatServiceFileName("%sService")
                    .formatServiceImplFileName("%sServiceImpl")
                    // Controller配置
                    .controllerBuilder()
                    .enableRestStyle();
        })
        // 执行生成
        .execute();
        
        System.out.println("代码生成完成！已确保使用正确的@Mapper注解配置（无错误的factoryBeanObjectType参数）");
    }
}