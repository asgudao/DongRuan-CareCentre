# DongRuan-CareCentre（东软养老中心管理系统）

> 东软实习 / 课程设计项目 —— 养老中心全栈管理系统（Web 后台 + Vue 管理端 + 贡献度热力图工具）

## 项目简介

本仓库是东软实习期间产出的养老中心管理系统，整体由三部分组成：

1. **yly后端**：基于 Spring Boot 3 + MyBatis-Plus 的服务端，提供登录鉴权、人员管理、床位管理、健康档案等 RESTful API。
2. **yly前端**：基于 Vue 3 + Vite + Element Plus 的运营 / 护工管理后台界面。
3. **generate_heatmap.py**：基于 Git log 的团队贡献度热力图生成脚本（matplotlib）。

适合作为：
- Vue 3 + Element Plus 中后台项目的完整范例
- Spring Boot 3 + MyBatis-Plus 实战参考
- 集成支付、文件存储、邮件等企业级中间件的实践案例

## 技术栈

### 后端
| 类别     | 技术 / 版本                                |
| -------- | ------------------------------------------ |
| 基础框架 | Spring Boot **3.1.5**                      |
| 持久层   | MyBatis-Plus 3.5.5                         |
| 数据库   | MySQL 8.0                                  |
| 安全     | Spring Security                            |
| 缓存     | Spring Data Redis                          |
| 邮件     | Spring Boot Starter Mail                   |
| 对象存储 | MinIO 8.5.12                               |
| 支付     | 支付宝 SDK 4.34.0                          |
| 鉴权     | JWT 4.4.0                                  |
| 模板引擎 | FreeMarker 2.3.34                          |
| JDK      | **17**                                     |

### 前端
| 类别       | 技术 / 版本                |
| ---------- | -------------------------- |
| 框架       | Vue **3.5**                |
| 构建工具   | Vite **7**                 |
| 路由       | Vue Router 4               |
| 状态管理   | Pinia 3（持久化插件）      |
| UI 组件库  | Element Plus 2.10          |
| HTTP       | Axios 1.11                 |
| 代码规范   | ESLint 9 + Prettier 3.6    |
| 样式       | Sass                       |
| Node       | ≥ 20.19 / 22.12            |

### 工具脚本
- Python 3.8+ / matplotlib / numpy

## 项目结构

```
DongRuan-CareCentre/
├── yly后端/
│   └── yly/
│       ├── pom.xml
│       └── src/main/
│           ├── java/com/neuedu/
│           │   ├── aop/        # 切面（日志、权限等）
│           │   ├── config/     # 配置类
│           │   ├── controller/ # REST 控制器
│           │   ├── entity/     # 数据库实体
│           │   ├── mapper/     # MyBatis-Plus Mapper
│           │   ├── service/ + impl/
│           │   ├── util/       # 工具类
│           │   └── vo/         # 视图对象
│           └── resources/
│               ├── application.yml
│               ├── mapper/     # XML 映射文件
│               └── templates/  # FreeMarker 模板
├── yly前端/
│   └── yly/
│       ├── package.json
│       ├── vite.config.js
│       └── src/                # Vue 组件 / 页面 / 路由 / 状态
└── generate_heatmap.py         # Git 贡献度热力图生成工具
```

## 核心功能

- ✅ 基于 JWT + Spring Security 的登录鉴权
- ✅ 老人档案管理（CRUD + 分页 + 条件查询）
- ✅ 护工 / 员工管理
- ✅ 床位 / 房间分配
- ✅ 健康档案与体征记录
- ✅ 支付宝在线缴费
- ✅ MinIO 对象存储（头像 / 体检报告）
- ✅ 邮件通知（账单、提醒）
- ✅ Redis 缓存热点数据
- ✅ 统一响应体 + 全局异常处理
- ✅ AOP 切面（日志 / 权限）

## 环境要求

| 工具   | 版本        |
| ------ | ----------- |
| JDK    | 17          |
| Maven  | 3.8+        |
| MySQL  | 8.0         |
| Redis  | 6+          |
| MinIO  | 8.x         |
| Node   | ≥ 20.19     |
| Python | 3.8+（脚本）|

## 快速开始

### 1. 启动后端
```bash
cd yly后端/yly
# 修改 application.yml 中的 MySQL / Redis / 阿里云支付 / MinIO 凭证
mvn clean install
mvn spring-boot:run
# 默认端口 8080
```

### 2. 启动前端
```bash
cd yly前端/yly
npm install      # 或 pnpm install
npm run dev
# 访问 http://localhost:5173
```

### 3. 生成贡献度热力图
```bash
cd yly后端/yly          # 进入 .git 所在目录
python ../../generate_heatmap.py
# 在当前目录生成 heatmap.png
```

## License

未指定 License。建议以团队 / 公司内部使用为主，若需对外发布请补充 [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)。
