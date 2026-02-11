# 威武信贷金融管理平台

## 项目地址

`https://github.com/CelestialVisionary/wwCredit`

## 技术栈

### 后端技术栈
- **Java 21**、Spring Boot 3、MyBatis-Plus、MySQL、Redis、Nacos、Maven、RESTful API

### 前端技术栈
- **Vue 3**、TypeScript 5.2.2、Vite 5.2.0、Vue Router 4.3.0、Pinia 2.1.7、Axios 1.13.2、原生CSS

### AI 技术栈
- **Python 3.10**、LangChain 0.1.20、LangGraph 0.2.0、OpenAI 1.30.0、FastAPI 0.109.2、Uvicorn 0.27.1

## 项目描述

参与开发的威武信贷金融管理平台，覆盖信贷业务全流程，包含前端用户端 / 管理后台、后端业务接口服务，支撑借款配置、用户积分、登录记录、公告管理等核心功能。

**新增AI功能**：集成大语言模型(LLM)能力，通过Python微服务实现模型微调与推理，为用户提供智能金融建议、风险评估和投资推荐，提升平台智能化水平。

## 主要工作

### 后端开发
- 基于 Spring Boot 3+MyBatis-Plus 实现用户积分记录表、登录记录表的 CRUD 操作
- 开发用户登录记录 TOP50 查询接口，通过 QueryWrapper 封装查询条件
- 手动注册 BorrowInfoMapper Bean，优化 MyBatis 映射配置，编写借款信息关联查询 SQL
- 封装 RequestHelper 工具类实现请求参数 Map 格式转换
- 新增 BadSqlGrammarException 全局异常处理，提升接口容错性

### AI 功能开发
- **Python 微服务**：创建 `ww_finance-ai` 目录，实现大模型微调与推理服务
- **LangChain 集成**：使用 LangChain 0.1.20 和 LangGraph 0.2.0 构建智能金融助手
- **模型微调**：实现基于金融领域数据的模型微调流程
- **API 集成**：Java 后端通过 WebClient 调用 Python 微调服务
- **回退机制**：当 Python 服务不可用时，自动回退到原始大模型

### 接口与安全
- 基于 JWT 实现接口无状态认证
- 配合前端完成路由权限控制、页面联调，保障借款配置、积分查询等核心功能正常可用
- 适配 Java 21 版本特性，通过 Maven 完成项目构建打包，保障后端服务稳定运行

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
├── ww_finance-ai/          # AI 微服务（Python）
│   ├── data/               # 训练和验证数据
│   ├── api_service.py      # FastAPI 服务
│   ├── data_processor.py   # 数据处理工具
│   ├── fine_tuner.py       # 模型微调实现
│   ├── mock_api_service.py # 模拟API服务（用于测试）
│   ├── requirements.txt    # Python 依赖配置
│   ├── start_service.py    # 服务启动脚本
│   └── .env                # 环境变量配置
├── ww_finance-master/      # 后端微服务（Java）
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
- **AI 助手**: 智能金融建议、风险评估、投资推荐

#### finance-message
- **短信服务**: 短信发送、验证码生成

#### finance-oss
- **文件存储**: 文件上传、下载

### AI 功能模块

#### ww_finance-ai
- **模型微调**: 基于金融领域数据的大模型微调
- **模型评估**: 评估微调后模型的性能
- **智能推理**: 提供金融相关问题的智能回答
- **API 服务**: 通过 FastAPI 提供 RESTful API 接口

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
7. **安装Python**：Python 3.10+（推荐使用虚拟环境）

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

### AI 服务运行

1. **配置环境变量**
   - 复制 `ww_finance-ai/.env` 文件并配置以下参数：
     ```
     # OpenAI API配置
     OPENAI_API_KEY=your-openai-api-key-here
     
     # 模型配置
     MODEL_NAME=gpt-3.5-turbo
     BASE_MODEL=gpt-3.5-turbo
     FINE_TUNED_MODEL_NAME=finance-assistant
     
     # API服务配置
     API_HOST=0.0.0.0
     API_PORT=8000
     
     # 数据配置
     DATA_DIR=data
     TRAIN_DATA_FILE=train_data.jsonl
     VAL_DATA_FILE=val_data.jsonl
     
     # 微调配置
     EPOCHS=3
     BATCH_SIZE=4
     LEARNING_RATE=2e-5
     ```

2. **安装Python依赖**
   ```bash
   cd ww_finance-ai
   python -m venv venv
   venv\Scripts\activate  # Windows
   # 或 source venv/bin/activate  # Linux/Mac
   pip install -r requirements.txt
   ```

3. **启动AI服务**
   ```bash
   python start_service.py
   ```
   - 服务将运行在 `http://localhost:8000`
   - 健康检查：`http://localhost:8000/`
   - API文档：`http://localhost:8000/docs`（当依赖项安装完成时）

4. **测试AI服务**
   - 健康检查：
     ```bash
     curl http://localhost:8000/
     ```
   - 模型推理：
     ```bash
     curl -X POST http://localhost:8000/api/inference \
     -H "Content-Type: application/json" \
     -d '{"user_question": "我想了解如何理财", "account_info": "账户余额：10000元"}'
     ```

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

### AI 开发规范

#### Python 代码规范
- 遵循PEP 8代码风格指南
- 使用类型提示增强代码可读性
- 模块化设计，职责分离
- 错误处理：使用try-except捕获异常
- 日志记录：使用标准logging模块
- 配置管理：使用环境变量或配置文件

#### AI 模型开发规范
- 数据准备：确保训练数据质量和多样性
- 模型选择：根据任务需求选择合适的基础模型
- 微调策略：合理设置微调参数（学习率、批量大小、epoch数）
- 评估指标：使用适当的指标评估模型性能
- 模型部署：通过API服务提供模型推理能力
- 监控与维护：定期评估模型性能，及时更新

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

7. **AI 服务注意事项**
   - **API密钥管理**：OpenAI API密钥应妥善保管，避免硬编码在代码中
   - **依赖项安装**：在MSYS2环境中安装依赖项可能会遇到限制，建议使用支持的Python环境
   - **服务部署**：生产环境中建议使用Docker容器化部署Python服务
   - **性能优化**：对于大规模微调，建议使用GPU加速
   - **监控告警**：建议为AI服务配置监控，及时发现服务异常
   - **数据安全**：确保训练数据符合隐私保护要求，避免使用敏感信息

8. **模拟服务**
   - 当Python依赖项无法安装时，服务会自动启动模拟API服务
   - 模拟服务提供基本的健康检查和模型推理功能，用于测试系统集成

## API文档

后端API文档使用Swagger/OpenAPI 3.0，可通过以下地址访问：
- 核心API服务：`http://localhost:8110/index.html`
- 文档包含所有API接口的详细说明、请求参数、响应格式等

## 许可证

(待确认)

## 联系方式

(待确认)