package com.wwfinance.utils;

import java.sql.*;
import java.util.Properties;

public class DatabaseChecker {
    public static void main(String[] args) {
        try {
            // 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 连接信息
            String url = "jdbc:mysql://localhost:3306/ww_finance?useSSL=false&serverTimezone=UTC&characterEncoding=utf-8";
            String username = "root";
            String password = "123456";
            
            // 连接数据库
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("数据库连接成功！");
            
            // 查看user表结构
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, "user", null);
            
            System.out.println("\nuser表结构：");
            System.out.println("字段名\t数据类型\t长度\t是否可为空");
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String columnType = rs.getString("TYPE_NAME");
                int columnSize = rs.getInt("COLUMN_SIZE");
                String isNullable = rs.getString("IS_NULLABLE");
                System.out.println(columnName + "\t" + columnType + "\t" + columnSize + "\t" + isNullable);
            }
            
            // 查看user_account表结构
            rs = metaData.getColumns(null, null, "user_account", null);
            
            System.out.println("\nuser_account表结构：");
            System.out.println("字段名\t数据类型\t长度\t是否可为空");
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String columnType = rs.getString("TYPE_NAME");
                int columnSize = rs.getInt("COLUMN_SIZE");
                String isNullable = rs.getString("IS_NULLABLE");
                System.out.println(columnName + "\t" + columnType + "\t" + columnSize + "\t" + isNullable);
            }
            
            // 关闭连接
            rs.close();
            conn.close();
            System.out.println("\n数据库连接已关闭！");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}