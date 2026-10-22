<div align="center">

# 跃动 YueDong

### 大学生体育用品商城

面向高校学生的体育用品电商平台，包含前台商城、用户个人中心、管理员后台，以及接入 DeepSeek/OpenAI 兼容大模型的商城 Agent。项目采用校园运动视觉风格，注重商品展示、选品效率和移动端使用体验。

[![Java](https://img.shields.io/badge/Java-8-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.11-6DB33F)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-5.7%2B-4479A1)](https://www.mysql.com/)
[![Vue](https://img.shields.io/badge/Vue-2.x-42B883)](https://v2.vuejs.org/)

[项目介绍](#项目介绍) · [功能模块](#功能模块) · [技术栈](#技术栈) · [本地运行](#本地运行) · [常见问题](#常见问题)

</div>

## 项目介绍

跃动 YueDong 是一个基于 Spring Boot 的大学生体育用品购物网站，围绕球类、训练服饰、运动配件等校园运动场景提供完整的购物流程。

项目已完成前台、用户个人中心和管理员后台的校园运动风视觉统一，使用运动跑道、球场线条、醒目比分牌配色和轻量动效强化体育氛围，并针对桌面端和手机端做了响应式适配。

商城 Agent 已从关键词规则助手升级为大模型规划器。模型负责理解自然语言、补全多轮上下文、选择已注册工具和组织回答，服务端负责身份校验、实时数据读取、写入确认与审计。没有配置模型或模型服务不可用时，系统会自动降级为规则助手，不影响原有购物和管理功能。

> 这是一个适合课程设计、毕业设计学习和 Java Web 入门实践的全栈示例项目，不是可直接用于生产环境的商业系统。正式部署前请修改默认密码、加强认证授权并完善支付、日志与数据安全设计。

## 功能模块

### 前台商城

- 体育用品分类浏览、关键词搜索和商品详情展示
- 商品加入购物车、修改数量、删除和批量清空
- 收货地址选择、订单结算、订单生成和订单明细
- 商品收藏、商品评价和留言板
- 校园体育新闻、网站公告浏览
- 用户注册、登录、积分查看和礼品兑换
- 桌面端与移动端响应式布局

### 用户个人中心

- 个人资料查看与修改
- 收货地址新增、编辑和删除
- 我的订单及订单明细
- 我的购物车、收藏和评价
- 积分记录与礼品兑换记录

### 管理员后台

- 管理员登录和密码修改
- 用户、商品分类和商品管理
- 订单受理、订单明细和商品销量统计
- 新闻、公告和留言管理
- 礼品兑换信息审核
- 商品评价管理
- ECharts 销售数据可视化

### 智能体 Agent

- 前台体育用品导购，支持预算、运动类别、商品编号和自然语言组合条件
- 支持“那 120 元以内呢”“换成羽毛球”等多轮上下文表达
- 查询实时商品、价格、库存、本人订单和商品详情
- 加购先生成确认草案，用户确认后才写入购物车
- 管理员后台侧边栏和经营工作台均提供“智能经营助手”入口，访问地址为 `/admin/agent/agent.html`
- 管理员可对话查询经营概览、低库存商品、销量排行，并生成待发货订单草案
- 发货等高风险操作必须二次确认，并支持幂等与审计记录
- DeepSeek、OpenAI 兼容接口及本地 Ollama 均可接入
- 模型未配置、超时或返回异常时自动降级到规则规划器

## 技术栈

| 分类 | 技术 |
| --- | --- |
| 后端语言 | Java 8 |
| 后端框架 | Spring Boot 2.6.11、Spring MVC |
| 持久层 | MyBatis 2.2.2、XML Mapper |
| 数据库 | MySQL 5.7 / 8.0、Druid 1.1.14 |
| 前端框架 | Vue 2、Axios |
| UI 与组件 | Element UI、Layui、Bootstrap、Bootstrap Icons |
| 交互与可视化 | jQuery、ECharts、CKEditor |
| 大模型 | DeepSeek Chat、OpenAI 兼容 Chat Completions |
| Agent 能力 | Function Calling、JSON Schema 工具注册、多轮上下文 |
| 安全与审计 | Session 身份校验、权限白名单、确认令牌、幂等、操作审计 |
| 构建工具 | Maven |
| 开发工具 | IntelliJ IDEA（推荐）或 Eclipse |

前端代码为 Spring Boot 项目内的静态 HTML、CSS 和 JavaScript，不需要单独安装 Node.js，也不需要执行 `npm install`。

## 项目结构

```text
YueDong/
├─ database/
│  └─ db_typs.sql                  # 数据库表结构与演示数据
├─ src/
│  ├─ main/
│  │  ├─ java/com/xiangmu/
│  │  │  ├─ Runboot.java           # Spring Boot 启动类
│  │  │  ├─ controler/             # 请求控制器
│  │  │  ├─ mappers/               # MyBatis Mapper 接口
│  │  │  ├─ pojo/                  # 实体类
│  │  │  ├─ service/               # 业务服务
│  │  │  ├─ agent/                 # 大模型规划器、工具、确认与审计
│  │  │  └─ util/                  # 工具类
│  │  └─ resources/
│  │     ├─ application.properties # 端口和数据库配置
│  │     ├─ mappers/               # MyBatis SQL 映射
│  │     └─ static/                # 页面、样式、脚本和图片
│  └─ test/                        # 测试代码
├─ pom.xml
├─ .gitignore
└─ README.md
```

## 本地运行

### 1. 准备环境

建议安装以下软件：

- JDK 8
- MySQL 5.7 或 8.0
- IntelliJ IDEA 2021 或更高版本
- Maven 3.6+（IntelliJ IDEA 自带的 Maven 也可以）
- Chrome、Edge 等现代浏览器

检查 Java 版本：

```bash
java -version
```

输出中应包含 `1.8`。

### 2. 下载项目

```bash
git clone https://github.com/billgeo1/YueDong.git
cd YueDong
```

也可以在 GitHub 页面点击 **Code → Download ZIP**，解压后进入 `YueDong` 目录。该目录中的 `pom.xml` 必须位于最外层，不要把它的上一级目录当作项目根目录打开。

### 3. 创建并导入数据库

使用 Navicat、DataGrip、MySQL Workbench 或命令行创建数据库：

```sql
CREATE DATABASE db_typs
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;
```

然后导入仓库内的 [`database/db_typs.sql`](database/db_typs.sql)。使用 MySQL 命令行时，可以在项目根目录执行：

```bash
mysql --default-character-set=utf8mb4 -u root -p db_typs -e "source database/db_typs.sql"
```

导入完成后应能看到 `t_user`、`t_admin`、`t_product`、`t_order`、`t_cart` 等数据表。

### 4. 修改数据库配置

打开 [`src/main/resources/application.properties`](src/main/resources/application.properties)，按本机环境修改账号和密码：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_typs?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=123456
```

如果 MySQL 使用其他端口，请同步把 `3306` 改成实际端口。示例配置中的密码仅用于本地演示，提交自己的代码前请勿写入真实生产密码。

### 5. 使用 IntelliJ IDEA 启动

1. 打开 IntelliJ IDEA，选择 **Open**。
2. 选择解压后的 `YueDong` 目录，也就是包含 `pom.xml` 的目录。
3. 等待 IDEA 自动加载 Maven 依赖。如果没有自动加载，右键 `pom.xml`，选择 **Maven → Reload project**。
4. 打开 **File → Project Structure → Project**，将 Project SDK 设置为 JDK 8。
5. 确认 IntelliJ IDEA 的 Maven 使用同一个 JDK 8。
6. 找到 `src/main/java/com/xiangmu/Runboot.java`。
7. 点击 `Runboot` 左侧的绿色运行按钮，选择 **Run 'Runboot'**。

当控制台显示 Tomcat 已在 `8888` 端口启动且没有数据库连接异常时，说明项目启动成功。

也可以在 IDEA 的终端中执行：

```bash
mvn spring-boot:run
```

### 6. 配置商城 Agent 大模型

Agent 配置位于 [`src/main/resources/application.properties`](src/main/resources/application.properties)，默认使用 DeepSeek：

```properties
agent.llm.enabled=true
agent.llm.base-url=https://api.deepseek.com
agent.llm.api-key=${DEEPSEEK_API_KEY:}
agent.llm.model=deepseek-chat
agent.llm.timeout-ms=12000
agent.llm.max-tokens=700
```

API Key 不应写入 `application.properties`、JavaScript、README 或提交到 Git。先在系统中设置环境变量。

Windows PowerShell：

```powershell
[Environment]::SetEnvironmentVariable(
  "DEEPSEEK_API_KEY",
  "你的DeepSeek-API-Key",
  "User"
)
```

设置后请关闭并重新打开 IntelliJ IDEA，使新的环境变量生效。也可以在 PowerShell 中临时设置后启动：

```powershell
$env:DEEPSEEK_API_KEY="你的DeepSeek-API-Key"
mvn spring-boot:run
```

使用 IntelliJ IDEA 时，也可以在 **Run → Edit Configurations → Runboot → Environment variables** 中添加 `DEEPSEEK_API_KEY`。

如果没有 API Key，可以保持 `agent.llm.enabled=false`，系统会使用规则助手；也可以接入本地 Ollama：

```properties
agent.llm.enabled=true
agent.llm.base-url=http://localhost:11434/v1
agent.llm.api-key=
agent.llm.model=qwen2.5:7b
```

Agent 响应中的 `planner` 字段可用于判断实际模式：

```json
{
  "planner": "llm",
  "provider": "DeepSeek",
  "model": "deepseek-chat",
  "answerPlanner": "llm"
}
```

如果显示 `planner: "rules"`，说明当前使用的是本地规则降级模式，请检查 Key、网络、模型名称和启动环境变量。

### 7. 访问系统

- 前台商城：[http://localhost:8888](http://localhost:8888)
- 管理员登录：[http://localhost:8888/login.html](http://localhost:8888/login.html)
- 管理员经营助手：[http://localhost:8888/admin/agent/agent.html](http://localhost:8888/admin/agent/agent.html)

演示账号：

| 角色 | 账号 | 密码 |
| --- | --- | --- |
| 管理员 | `a` | `a` |
| 普通用户 | `wanggang` | `000000` |

普通用户也可以在前台注册新账号。管理员和普通用户使用不同的登录入口。

> 不要直接双击 `index.html` 或 `login.html` 打开页面。页面通过 `/` 开头的地址调用后端接口，必须启动 Spring Boot 后通过 `http://localhost:8888` 访问。

## 配置说明

| 配置项 | 当前值 | 说明 |
| --- | --- | --- |
| 服务端口 | `8888` | 修改 `server.port` 可更换端口 |
| 数据库名 | `db_typs` | SQL 脚本默认导入该数据库 |
| MySQL 地址 | `localhost:3306` | 按本机 MySQL 地址和端口修改 |
| 上传大小限制 | `500MB` | 对应 Spring Multipart 配置 |
| Mapper 位置 | `classpath:mappers/*.xml` | MyBatis SQL 映射目录 |
| Agent 开关 | `agent.llm.enabled` | `true` 优先调用大模型，异常时自动降级 |
| Agent 接口 | `agent.llm.base-url` | DeepSeek 或 OpenAI 兼容接口地址 |
| Agent 模型 | `agent.llm.model` | 默认 `deepseek-chat` |
| Agent Key | `DEEPSEEK_API_KEY` | 只从环境变量读取，禁止提交到仓库 |

## Agent 调用链

```text
浏览器问题 + 最近对话
        ↓
Spring Boot /agent/chat
        ↓
DeepSeek 大模型理解需求并选择工具
        ↓
服务端校验 Session、角色、工具白名单和参数
        ↓
MyBatis 读取商品、订单、库存或经营数据
        ↓
大模型根据真实工具结果组织自然语言
        ↓
读操作直接返回；写操作返回确认草案，确认后才执行
```

模型只能从服务端注册的工具中选择能力，不能直接访问数据库，也不能绕过管理员权限。加购和发货工具即使被模型选中，也只会生成确认草案；最终业务结果始终以服务端数据库操作结果为准。

图片、附件等上传文件会在项目运行时生成，并保存在运行目录的 `upload` 文件夹中。这些运行时文件默认不会提交到 Git。

## 常见问题

### 启动时报 Unknown database 'db_typs'

数据库尚未创建或导入失败。先创建 `db_typs`，再导入 `database/db_typs.sql`。

### 启动时报 Access denied for user

`application.properties` 中的 MySQL 用户名或密码与本机不一致。修改后重新启动项目。

### 端口 8888 已被占用

关闭占用端口的程序，或把 `server.port=8888` 改为其他未被占用的端口。修改后访问地址也要使用新端口。

### 页面能打开但商品、订单等数据加载失败

确认 Spring Boot 控制台没有数据库异常，并确认访问地址是 `http://localhost:8888`，而不是直接用浏览器打开本地 HTML 文件。

### 中文出现乱码

数据库建议使用 `utf8mb4`，导入 SQL 时指定字符集：

```bash
mysql --default-character-set=utf8mb4 -u root -p db_typs -e "source database/db_typs.sql"
```

### Maven 依赖下载慢或失败

检查网络和 Maven 镜像配置，然后在 IntelliJ IDEA 中重新加载 Maven 项目。国内环境可按需将 Maven 设置为阿里云镜像。

## 开发说明

- 公共运动主题样式位于 `src/main/resources/static/css/sports-theme.css`。
- 管理后台样式位于 `src/main/resources/static/css/sports-console.css`。
- 前台页面主要位于 `src/main/resources/static/view/`。
- 用户个人中心页面主要位于 `src/main/resources/static/auser/`。
- 管理员后台页面主要位于 `src/main/resources/static/admin/`。
- 接口控制器位于 `src/main/java/com/xiangmu/controler/`。
- 数据库映射位于 `src/main/resources/mappers/`。

修改静态页面后刷新浏览器即可看到变化；修改 Java 代码后需要重新启动项目。若浏览器缓存了旧样式，可使用 `Ctrl + F5` 强制刷新。

## 后续改进方向

- 将明文密码升级为 BCrypt 等安全哈希方案
- 使用环境变量或配置中心管理数据库密码
- 引入 Spring Security 完善角色权限和接口鉴权
- 增加库存并发控制、订单超时关闭和支付回调
- 补充单元测试、接口测试和持续集成
- 将上传文件迁移到对象存储

## License

本项目用于学习和技术交流。第三方前端库、字体、图片及编辑器资源的使用请遵循各自原始许可证。
