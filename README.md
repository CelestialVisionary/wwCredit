# 信贷管理系统

## 项目简介

这是一个完整的金融信贷管理系统，采用前后端分离架构，包含用户前台和管理后台。

## 技术栈

### 后端技术栈
- **框架**: Spring Boot
- **ORM**: MyBatis
- **数据库**: MySQL
- **微服务架构**: Spring Cloud
- **API文档**: Swagger/OpenAPI
- **消息队列**: (待确认)
- **缓存**: Redis
- **构建工具**: Maven

### 前端技术栈
- **框架**: Vue 3
- **语言**: TypeScript
- **构建工具**: Vite
- **路由**: Vue Router
- **状态管理**: (待确认)
- **UI组件库**: (待确认)

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

### 后端运行

1. 克隆项目
   ```bash
   git clone <项目地址>
   cd credit-proj/ww_finance-master
   ```

2. 配置数据库
   - 修改各服务下的 `application.yml` 文件
   - 配置数据库连接信息

3. 构建项目
   ```bash
   mvn clean install
   ```

4. 启动服务
   - 依次启动各微服务模块
   - 建议启动顺序: gateway → base → common → message → oss → api

### 前端运行

#### 用户前台

```bash
cd ww_front
npm install
npm run dev
```

#### 管理后台

```bash
cd ww_admin
npm install
npm run dev
```

## 开发规范

### 后端开发规范
- 遵循RESTful API设计规范
- 统一异常处理
- 统一响应格式
- 代码分层清晰
- 合理使用设计模式

### 前端开发规范
- 组件化开发
- TypeScript类型安全
- 代码风格统一
- 响应式设计
- 性能优化

## 注意事项

1. 确保JDK版本为1.8或以上
2. 确保Node.js版本为14或以上
3. 开发环境需要安装MySQL、Redis等中间件
4. 微服务之间通过Feign进行通信
5. 前端使用Axios进行API调用

## 许可证

(待确认)

## 联系方式

(待确认)