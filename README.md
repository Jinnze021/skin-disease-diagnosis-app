# 皮肤病智诊影析小程序（安全公开版）

这是面向校招作品展示的全栈项目：用户上传皮肤图片后，由 Java 后端完成身份校验、图片安全验证和 EasyDL 模型代理调用，并提供科普、医院信息、在线咨询与个人检测记录等功能。

> 本仓库仅用于软件工程学习与作品展示。AI 结果不构成医疗诊断或治疗建议；出现症状请咨询具备资质的医疗专业人员。

## 项目亮点

- Spring Boot + MyBatis-Plus 构建 RESTful 后端，按公开、登录用户、管理员三级权限保护接口，并对个人数据实施服务端归属校验。
- BCrypt 存储密码；登录令牌使用 256 位安全随机数，Redis 仅保存 SHA-256 摘要并设置 2 小时过期时间。
- 查询参数通过字段白名单转换为 MyBatis-Plus Wrapper，拒绝 `sqlwhere`、未知字段、非法排序和超大分页。
- 上传图片限制为可解码的 PNG/JPEG，最大 5 MiB、单边不超过 8192 像素、总像素不超过 2500 万；文件名由服务端生成 UUID。
- EasyDL 密钥和 OAuth 令牌只存在于后端；令牌缓存、超时控制、单次失效重试与响应裁剪均在服务端完成。
- 密码重置采用一次性验证码摘要、10 分钟有效期、5 次尝试限制、60 秒发送冷却和来源限流。

## 技术栈

- 后端：Java 8、Spring Boot 2.7、MyBatis-Plus、MySQL、Redis、BCrypt、Maven
- 前端：Vue 2、uni-app、Vuex
- 工程质量：JUnit 5、Mockito、GitHub Actions、Dependabot

## 目录结构

```text
.
├── project/                 # Spring Boot 后端及后端测试
├── client_home/             # uni-app 用户端
├── project.sql              # 仅包含表结构，不含真实用户或默认口令
├── docs/architecture.md     # 安全架构与请求流程
├── docs/security-verification.md  # 最终验证记录（完成后生成）
├── SECURITY.md              # 安全策略与报告方式
└── .env.example             # 环境变量模板（仅占位值）
```

核心实现可从以下位置开始阅读：

- [认证与会话](project/src/main/java/com/project/demo/security/TokenService.java)
- [接口权限策略](project/src/main/java/com/project/demo/security/EndpointAccessPolicy.java)
- [安全查询解析](project/src/main/java/com/project/demo/query/QuerySpecParser.java)
- [图片安全边界](project/src/main/java/com/project/demo/storage/ImageStorageService.java)
- [EasyDL 服务端代理](project/src/main/java/com/project/demo/easydl/EasyDlClient.java)
- [AI 诊断接口](project/src/main/java/com/project/demo/controller/SkinDiagnosisController.java)

## 本地运行

### 1. 准备环境

- JDK 8
- Maven 3.8+
- MySQL 8
- Redis 6.2+
- Node.js 24.14.0（与 CI 一致）

创建数据库并导入结构：

```sql
CREATE DATABASE project_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

```bash
mysql -u root -p project_db < project.sql
```

复制 `.env.example` 的变量到本机环境中。最低限度需要配置：

```text
DB_USERNAME=你的本地数据库用户
DB_PASSWORD=你的本地数据库密码
```

仓库不会自动读取 `.env` 文件；请通过 IDE、终端或本机安全配置注入环境变量，不要提交真实值。

### 2. 启动后端

```bash
mvn -f project/pom.xml test
mvn -f project/pom.xml spring-boot:run
```

后端默认监听 `http://127.0.0.1:5000/api`。数据库脚本不含真实数据、演示账号或默认管理员口令；可先通过注册接口创建普通用户。管理员数据应只在本地测试库中创建，不得提交数据库导出。

### 3. 启动或构建前端

```bash
cd client_home
npm ci --legacy-peer-deps --ignore-scripts
$env:NODE_OPTIONS="--openssl-legacy-provider"  # Windows PowerShell
npm run serve
```

生产构建：

```bash
$env:NODE_OPTIONS="--openssl-legacy-provider"  # Windows PowerShell
npm run build
```

无需提交前端 `.env` 文件：开发服务器默认把 `/api` 转发到 `http://127.0.0.1:5000`，生产构建默认使用同源 `/api`；如需改变地址，可在本机设置 `VUE_APP_BASE_HOST` 和 `VUE_APP_BASE_API`。
这套 uni-app/Vue 2 项目仍使用 Webpack 4，因此在 Node.js 24 下构建时需要临时设置 `NODE_OPTIONS=--openssl-legacy-provider`；不要把该变量配置为系统级永久变量。Linux/macOS 可在命令前使用 `NODE_OPTIONS=--openssl-legacy-provider`，CI 已在构建步骤中局部设置。

> **依赖风险提示：** 该前端来自较早的 uni-app/Vue 2 工程，锁定依赖仍包含 npm 已知漏洞。它仅用于本地面试演示和代码审阅，请勿连接真实用户数据或直接部署到公网生产环境。完整审计结果见 `docs/security-verification.md`；若要生产化，应单独规划 Vue 3/新版 uni-app 迁移。

## 可选功能

默认关闭 EasyDL 和邮件密码重置，因此没有外部凭据也能安全启动核心后端。启用前请在本机设置：

```text
EASYDL_ENABLED=true
EASYDL_API_KEY=你的新 API Key
EASYDL_SECRET_KEY=你的新 Secret Key
EASYDL_MODEL_URL=https://aip.baidubce.com/rpc/2.0/ai_custom/v1/classification/你的模型

PASSWORD_RESET_ENABLED=true
PASSWORD_RESET_HMAC_KEY=至少32字节的随机字符串
MAIL_USERNAME=发件账号
MAIL_PASSWORD=邮件授权码
```

服务只接受 `aip.baidubce.com` 下的 HTTPS EasyDL 令牌与模型地址。请勿使用曾经公开过的旧密钥。

## 测试与安全边界

```bash
mvn -f project/pom.xml clean test
mvn -f project/pom.xml -DskipTests package
```

测试中的 `FAKE_*` 字符串和模拟 HTTP 响应均为无效占位数据。真实 EasyDL 联调需由使用者自行提供已轮换的环境变量；仓库不包含真实病历、用户信息、图片或模型凭据。更完整的设计见 [架构说明](docs/architecture.md) 和 [安全策略](SECURITY.md)。
