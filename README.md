# 信贷管理系统

## 项目简介

这是一个完整的金融信贷管理系统，采用前后端分离架构，包含用户前台和管理后台。系统提供了用户管理、积分等级、借款管理、出借管理、还款管理等核心功能，旨在为用户提供安全、便捷的金融服务。

## 技术栈

### 后端技术栈
- **框架**: Spring Boot 3.2.10
- **ORM**: MyBatis-Plus 3.5.6
- **数据库**: MySQL 8.0+
- **微服务架构**: Spring Cloud Alibaba
- **注册中心/配置中心**: Nacos 2.2.2
- **API文档**: Swagger/OpenAPI 3.0.1
- **缓存**: Redis 7.0+
- **构建工具**: Maven 3.6.3+

### 前端技术栈
- **框架**: Vue 3
- **语言**: TypeScript 5.2.2
- **构建工具**: Vite 5.2.0
- **路由**: Vue Router 4.3.0
- **状态管理**: Pinia 2.1.7
- **HTTP客户端**: Axios 1.13.2
- **UI组件库**: 原生CSS（无第三方UI库）

## 项目结构

```
credit-proj/
├── notes/                  # 项目笔记
├── ww_admin/               # 管理后台前端
│   ├── src/                # 源代码
│   └── package.json        # 依赖配置
├── ww_finance-master/      # 后端微服务
│   ├── finance-api/        # 核心API服务
│   ├── finance-base/       # 基础配置
│   ├── finance-common/     # 公共工具类
│   ├── finance-gateway/    # 网关服务
│   ├── finance-message/    # 消息服务
│   ├── finance-oss/        # 对象存储服务
│   └── pom.xml             # Maven父工程
├── ww_front/               # 用户前台前端
│   ├── src/                # 源代码
│   └── package.json        # 依赖配置
└── package.json            # 项目根依赖配置
```

## 功能模块

### 后端核心模块

#### finance-api
- **用户管理**: 用户注册、登录、信息管理
- **积分等级**: 积分等级规则管理
- **借款管理**: 借款信息、借款审批
- **出借管理**: 投资列表、出借记录
- **还款管理**: 还款计划、还款记录
- **交易流水**: 资金交易记录
- **数据字典**: 系统数据字典管理

#### finance-message
- **短信服务**: 短信发送、验证码生成

#### finance-oss
- **文件存储**: 文件上传、下载

### 前端模块

#### ww_front (用户前台)
- **首页**: 平台介绍、统计数据
- **投资**: 投资列表、投资详情
- **公告**: 平台公告
- **用户中心**: 个人信息、借款记录、投资记录
- **安全中心**: 账户安全设置

#### ww_admin (管理后台)
- **仪表盘**: 数据统计、系统概览
- **用户管理**: 用户列表、登录记录
- **积分等级**: 积分等级配置
- **借款管理**: 借款列表、审批流程
- **出借管理**: 投资记录、资金流水
- **系统设置**: 系统配置、字典管理
- **公告管理**: 公告发布、编辑

## 快速开始

### 环境准备

1. **安装JDK**：JDK 21+（推荐Oracle JDK或OpenJDK）
2. **安装Node.js**：Node.js 18+（推荐使用nvm进行版本管理）
3. **安装Maven**：Maven 3.6.3+
4. **安装MySQL**：MySQL 8.0+，创建数据库`ww_finance`
5. **安装Redis**：Redis 7.0+，默认配置即可
6. **安装Nacos**：Nacos 2.2.2，默认配置即可

### 后端运行

1. **启动依赖服务**
   - 启动Redis：`redis-server.exe redis.conf`（Windows）或`redis-server redis.conf`（Linux）
   - 启动Nacos：`startup.cmd -m standalone`（Windows）或`startup.sh -m standalone`（Linux）

2. **配置数据库**
   - 修改`ww_finance-master/finance-api/src/main/resources/application.yml`
   - 配置数据库连接信息：
     ```yaml
     spring:
       datasource:
         url: jdbc:mysql://localhost:3306/ww_finance?serverTimezone=GMT%2B8&characterEncoding=utf-8
         username: root
         password: your_password
     ```

3. **构建项目**
   ```bash
   cd ww_finance-master
   mvn clean install
   ```

4. **启动核心API服务**
   ```bash
   cd finance-api
   mvn spring-boot:run
   ```
   - 服务将运行在`http://localhost:8110`
   - API文档地址：`http://localhost:8110/index.html`

### 前端运行

#### 用户前台

1. **安装依赖**
   ```bash
   cd ww_front
   npm install
   ```

2. **启动开发服务器**
   ```bash
   npm run dev
   ```
   - 访问地址：`http://localhost:5173`

#### 管理后台

1. **安装依赖**
   ```bash
   cd ww_admin
   npm install
   ```

2. **启动开发服务器**
   ```bash
   npm run dev
   ```
   - 访问地址：`http://localhost:5174`（或自动分配的其他端口）

## 开发规范

### 后端开发规范
- 遵循RESTful API设计规范
- 统一异常处理，返回格式为`PccAjaxResult`
- 代码分层清晰：Controller → Service → Mapper
- 使用Lombok简化实体类开发
- 合理使用设计模式
- 编写详细的Swagger文档

### 前端开发规范
- 组件化开发，遵循单一职责原则
- TypeScript类型安全，避免使用`any`类型
- 代码风格统一，使用ESLint进行代码检查
- 响应式设计，适配不同屏幕尺寸
- 性能优化：懒加载、代码分割、缓存策略
- 状态管理：使用Pinia，合理划分store

## 注意事项

1. **环境配置**
   - 确保所有依赖服务（MySQL、Redis、Nacos）都已正确启动
   - 检查防火墙设置，确保服务端口可访问

2. **数据库配置**
   - 首次运行需要创建数据库并初始化表结构
   - 建议使用数据库迁移工具管理表结构变更

3. **微服务通信**
   - 微服务之间通过Feign进行通信
   - 服务注册与发现通过Nacos实现

4. **前端代理配置**
   - 前端已配置代理，将`/api/core`路径代理到后端服务
   - 开发环境下无需额外配置CORS

5. **日志管理**
   - 后端使用SLF4J + Logback进行日志管理
   - 日志文件位于各服务的`logs`目录下

6. **安全注意事项**
   - 生产环境需配置HTTPS
   - 敏感信息（如数据库密码）应通过环境变量或配置中心管理
   - 定期更新依赖库，修复安全漏洞

## API文档

后端API文档使用Swagger/OpenAPI 3.0，可通过以下地址访问：
- 核心API服务：`http://localhost:8110/index.html`
- 文档包含所有API接口的详细说明、请求参数、响应格式等

## 许可证

(待确认)

## 联系方式

(待确认)