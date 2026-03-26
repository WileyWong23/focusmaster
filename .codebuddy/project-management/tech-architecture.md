# 专注大师 - 技术架构文档

**版本**: v1.0  
**日期**: 2026-03-26  
**状态**: 待用户确认  
**作者**: 架构师  

---

## 一、整体架构

### 1.1 架构图

```
┌─────────────────────────────────────────────┐
│                   浏览器 / WebView           │
│  ┌───────────────────────────────────────┐  │
│  │           前端 (SPA)                  │  │
│  │   HTML + CSS + JavaScript            │  │
│  │   Tailwind CSS + 原生JS              │  │
│  └──────────────┬────────────────────────┘  │
│                 │ HTTP REST API             │
└─────────────────┼───────────────────────────┘
                  │
┌─────────────────┼───────────────────────────┐
│           Spring Boot (8080)                │
│  ┌──────────────┴────────────────────────┐  │
│  │          Controller 层                │  │
│  │  GoalController  SessionController   │  │
│  │  StatsController                     │  │
│  └──────────────┬────────────────────────┘  │
│  ┌──────────────┴────────────────────────┐  │
│  │          Service 层                   │  │
│  │  GoalService  SessionService         │  │
│  │  StatsService  RankService           │  │
│  └──────────────┬────────────────────────┘  │
│  ┌──────────────┴────────────────────────┐  │
│  │          Repository 层 (JPA)          │  │
│  │  GoalRepository  SessionRepository   │  │
│  └──────────────┬────────────────────────┘  │
│  ┌──────────────┴────────────────────────┐  │
│  │          H2 嵌入式数据库              │  │
│  │         (文件持久化存储)              │  │
│  └──────────────────────────────────────┘  │
└─────────────────────────────────────────────┘
```

### 1.2 设计原则
- **前后端一体部署**：前端静态资源打包在 Spring Boot JAR 中，无需单独部署前端服务
- **零外部依赖**：H2 嵌入式数据库 + 文件存储，开箱即用
- **RESTful API**：后端提供标准 REST 接口，前端通过 fetch 调用
- **SPA 路由**：前端使用 Hash 路由实现页面切换

---

## 二、技术栈

### 2.1 后端

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 8 | 开发语言 |
| Spring Boot | 2.7.14 | 应用框架 |
| Spring Data JPA | (随Boot) | ORM / 数据访问 |
| H2 Database | (随Boot) | 嵌入式数据库 |
| Lombok | 1.18.x | 简化POJO代码 |

### 2.2 前端

| 技术 | 版本 | 用途 |
|------|------|------|
| HTML5 | - | 页面结构 |
| CSS3 | - | 样式（Tailwind CSS） |
| JavaScript | ES6+ | 交互逻辑 |
| Tailwind CSS | CDN | UI框架 |
| Material Symbols | CDN | 图标库 |
| Google Fonts | CDN | Manrope + Inter 字体 |

### 2.3 构建 & 运行

| 工具 | 用途 |
|------|------|
| Maven | 依赖管理 & 打包 |
| Spring Boot Maven Plugin | 可执行JAR打包 |

---

## 三、后端项目结构

```
src/main/java/com/focusmaster/
├── Application.java                    // 启动类
├── config/
│   └── WebConfig.java                  // CORS配置、静态资源映射
├── controller/
│   ├── GoalController.java             // 目标管理API
│   └── SessionController.java          // 专注会话API
├── dto/
│   ├── GoalCreateRequest.java          // 创建目标请求
│   ├── GoalResponse.java               // 目标响应
│   ├── SessionStartRequest.java        // 开始专注请求
│   ├── SessionEndRequest.java          // 结束专注请求
│   └── StatsResponse.java              // 统计数据响应
├── entity/
│   ├── AppUser.java                    // 用户实体
│   ├── Goal.java                       // 目标实体
│   └── FocusSession.java              // 专注记录实体
├── enums/
│   ├── GoalStatus.java                 // ACTIVE / COMPLETED / ABANDONED
│   └── Rank.java                       // BRONZE ~ KING
├── repository/
│   ├── AppUserRepository.java          // 用户数据访问
│   ├── GoalRepository.java             // 目标数据访问
│   └── SessionRepository.java          // 专注记录数据访问
├── service/
│   ├── GoalService.java                // 目标业务逻辑
│   ├── SessionService.java             // 专注会话业务逻辑
│   ├── RankService.java                // 段位计算逻辑
│   └── AppUserService.java             // 默认用户初始化
└── exception/
    ├── GlobalExceptionHandler.java     // 全局异常处理
    └── BusinessException.java          // 业务异常

src/main/resources/
├── application.properties              // 应用配置
├── data.sql                            // 初始化数据（可选）
└── static/
    └── index.html                      // 前端入口页面（SPA）
```

### 3.1 包说明

| 包 | 职责 |
|---|---|
| `config` | 全局配置：CORS、静态资源映射 |
| `controller` | REST API 接口层，参数校验、调用Service |
| `dto` | 数据传输对象，接口请求/响应结构定义 |
| `entity` | JPA 实体类，映射数据库表 |
| `enums` | 枚举：目标状态、段位等级 |
| `repository` | Spring Data JPA 接口，数据访问层 |
| `service` | 核心业务逻辑层 |
| `exception` | 统一异常处理 |

---

## 四、数据库设计

### 4.1 表结构

#### app_user（用户表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| device_uuid | VARCHAR(64) | UNIQUE | 设备唯一标识（客户端生成UUID） |
| nickname | VARCHAR(50) | NOT NULL, DEFAULT '专注大师' | 昵称 |
| avatar | VARCHAR(500) | DEFAULT NULL | 头像URL |
| phone | VARCHAR(20) | DEFAULT NULL | 手机号（V2登录用） |
| password | VARCHAR(100) | DEFAULT NULL | 密码（V2登录用，加密存储） |
| wechat_openid | VARCHAR(64) | DEFAULT NULL, UNIQUE | 微信OpenID（V2微信登录用） |
| wechat_unionid | VARCHAR(64) | DEFAULT NULL | 微信UnionID（跨应用关联用） |
| total_focus_minutes | BIGINT | NOT NULL, DEFAULT 0 | 累计专注总时长（冗余字段，提升查询性能） |
| completed_goal_count | INT | NOT NULL, DEFAULT 0 | 已完成目标数（冗余字段） |
| last_active_at | TIMESTAMP | DEFAULT NULL | 最后活跃时间 |
| created_at | TIMESTAMP | NOT NULL | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL | 更新时间 |

**索引**：`uk_device_uuid` (device_uuid) UNIQUE，`uk_wechat_openid` (wechat_openid) UNIQUE

> **V1策略**：前端首次访问时生成 UUID 存入 localStorage，请求后端自动创建/关联用户。微信相关字段预留为 NULL。
> **V2扩展**：新增手机号登录和微信登录，表结构零变更。

**V2登录扩展说明**：
```
V1: 设备UUID → 自动创建app_user → 所有数据绑定该user

V2 手机号登录:
  phone + password → 查找app_user → 若已存在则关联device_uuid
  → 若新用户则创建app_user并绑定device_uuid

V2 微信登录:
  前端跳转微信授权 → 用户同意 → 获取code
  → 后端用code换取openid → 用openid查找/创建app_user
  → 绑定device_uuid → 返回token
    → 若新用户则创建app_user并绑定device_uuid
    → 登录态下所有API通过JWT中的userId鉴权
    → device_uuid不再作为主查询条件，降级为"记住登录"
```

#### goal（目标表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| user_id | BIGINT | FK → app_user.id, NOT NULL | 所属用户 |
| name | VARCHAR(50) | NOT NULL | 目标名称 |
| total_minutes | BIGINT | NOT NULL | 目标总时长（分钟） |
| accumulated_minutes | BIGINT | NOT NULL, DEFAULT 0 | 已累计时长（分钟） |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'ACTIVE' | 状态：ACTIVE/COMPLETED/ABANDONED |
| current_rank | VARCHAR(20) | NOT NULL, DEFAULT 'BRONZE' | 当前段位 |
| created_at | TIMESTAMP | NOT NULL | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL | 更新时间 |

**索引**：`idx_user_status` (user_id, status)

#### focus_session（专注记录表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| user_id | BIGINT | FK → app_user.id, NOT NULL | 所属用户 |
| goal_id | BIGINT | FK → goal.id, NOT NULL | 关联目标 |
| planned_minutes | INT | NOT NULL | 计划时长（分钟） |
| actual_minutes | INT | NOT NULL | 实际专注时长（分钟） |
| is_valid | BOOLEAN | NOT NULL | 是否有效（≥5分钟） |
| started_at | TIMESTAMP | NOT NULL | 开始时间 |
| ended_at | TIMESTAMP | NOT NULL | 结束时间 |

**索引**：`idx_user_goal` (user_id, goal_id)，`idx_user_ended` (user_id, ended_at)

### 4.2 DDL

```sql
CREATE TABLE app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_uuid VARCHAR(64) NOT NULL,
    nickname VARCHAR(50) NOT NULL DEFAULT '专注大师',
    avatar VARCHAR(500),
    phone VARCHAR(20),
    password VARCHAR(100),
    wechat_openid VARCHAR(64),
    wechat_unionid VARCHAR(64),
    total_focus_minutes BIGINT NOT NULL DEFAULT 0,
    completed_goal_count INT NOT NULL DEFAULT 0,
    last_active_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_device_uuid UNIQUE (device_uuid),
    CONSTRAINT uk_wechat_openid UNIQUE (wechat_openid)
);

CREATE TABLE goal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    total_minutes BIGINT NOT NULL,
    accumulated_minutes BIGINT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    current_rank VARCHAR(20) NOT NULL DEFAULT 'BRONZE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);

CREATE TABLE focus_session (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    goal_id BIGINT NOT NULL,
    planned_minutes INT NOT NULL,
    actual_minutes INT NOT NULL,
    is_valid BOOLEAN NOT NULL,
    started_at TIMESTAMP NOT NULL,
    ended_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_user(id),
    FOREIGN KEY (goal_id) REFERENCES goal(id)
);

CREATE INDEX idx_goal_user_status ON goal(user_id, status);
CREATE INDEX idx_session_user_goal ON focus_session(user_id, goal_id);
CREATE INDEX idx_session_user_ended ON focus_session(user_id, ended_at);
```

### 4.3 H2 配置

```properties
# 使用文件存储，数据持久化
spring.datasource.url=jdbc:h2:file:./data/focusmaster;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
# 自动建表
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.h2.console.enabled=false
```

---

## 五、API 接口设计

### 5.1 统一响应格式

```json
{
    "code": 200,
    "message": "success",
    "data": { ... }
}
```

| code | 含义 |
|------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 404 | 资源不存在 |
| 500 | 服务端错误 |

### 5.2 目标管理 API

#### GET /api/goals
获取所有目标列表

**Response**:
```json
{
    "code": 200,
    "data": {
        "activeGoal": {
            "id": 1,
            "name": "考研",
            "totalMinutes": 6000,
            "accumulatedMinutes": 4320,
            "status": "ACTIVE",
            "currentRank": "GOLD",
            "progressPercent": 72,
            "createdAt": "2026-03-20T10:00:00"
        },
        "completedGoals": [
            {
                "id": 2,
                "name": "教师资格证",
                "totalMinutes": 12000,
                "accumulatedMinutes": 12000,
                "status": "COMPLETED",
                "currentRank": "KING",
                "progressPercent": 100,
                "createdAt": "2026-01-15T08:00:00"
            }
        ],
        "abandonedGoals": []
    }
}
```

#### POST /api/goals
创建新目标

**Request**:
```json
{
    "name": "考研",
    "totalMinutes": 6000
}
```

**Response**:
```json
{
    "code": 200,
    "data": { "id": 1, "name": "考研", ... }
}
```

**校验规则**：
- name: 非空，最长20字符
- totalMinutes: ≥ 60（最少1小时），≤ 59940（999小时）
- 若已有 ACTIVE 目标，返回 400 + 提示信息

#### POST /api/goals/{id}/end
结束目标

**Response**:
```json
{
    "code": 200,
    "data": {
        "id": 1,
        "status": "ABANDONED",
        "accumulatedMinutes": 4320
    }
}
```

#### POST /api/goals/{id}/restart
重新启动已结束目标

**校验规则**：
- 目标必须存在且状态非 ACTIVE
- 若已有其他 ACTIVE 目标，返回 400

#### GET /api/goals/{id}
获取目标详情（含段位阶梯信息）

**Response**:
```json
{
    "code": 200,
    "data": {
        "id": 1,
        "name": "考研",
        "totalMinutes": 6000,
        "accumulatedMinutes": 4320,
        "status": "ACTIVE",
        "currentRank": "GOLD",
        "rankThresholds": [
            { "rank": "BRONZE", "minutesRequired": 0 },
            { "rank": "SILVER", "minutesRequired": 300 },
            { "rank": "GOLD", "minutesRequired": 900 },
            { "rank": "PLATINUM", "minutesRequired": 1800 },
            { "rank": "DIAMOND", "minutesRequired": 3000 },
            { "rank": "STARRY", "minutesRequired": 4500 },
            { "rank": "KING", "minutesRequired": 6000 }
        ]
    }
}
```

---

### 5.3 专注会话 API

#### POST /api/sessions/start
开始专注

**Request**:
```json
{
    "goalId": 1,
    "plannedMinutes": 25
}
```

**Response**:
```json
{
    "code": 200,
    "data": {
        "sessionId": 101,
        "startedAt": "2026-03-26T10:00:00",
        "plannedMinutes": 25
    }
}
```

**校验规则**：
- goalId 必须存在且为 ACTIVE 状态
- plannedMinutes: 5 ~ 180
- 同一目标不能有未结束的会话（前端控制即可）

#### POST /api/sessions/{id}/end
结束专注

**Request**:
```json
{
    "actualMinutes": 55
}
```

**Response**:
```json
{
    "code": 200,
    "data": {
        "sessionId": 101,
        "isValid": true,
        "actualMinutes": 55,
        "accumulatedMinutes": 4375,
        "currentRank": "GOLD",
        "rankChanged": false,
        "previousRank": "GOLD"
    }
}
```

**业务逻辑**：
1. 记录本次专注时长
2. 判断 isValid（≥5分钟）
3. 若有效，累加到 goal.accumulatedMinutes
4. 重新计算段位
5. 返回段位是否变化（用于前端决定是否展示升级庆祝页）

---

### 5.4 统计 API

#### GET /api/stats/overview
获取整体统计数据

**Response**:
```json
{
    "code": 200,
    "data": {
        "totalFocusMinutes": 28920,
        "completedGoalCount": 3,
        "dailyStats": [
            { "date": "2026-03-20", "totalMinutes": 180 },
            { "date": "2026-03-21", "totalMinutes": 120 },
            { "date": "2026-03-22", "totalMinutes": 240 },
            { "date": "2026-03-23", "totalMinutes": 0 },
            { "date": "2026-03-24", "totalMinutes": 90 },
            { "date": "2026-03-25", "totalMinutes": 150 },
            { "date": "2026-03-26", "totalMinutes": 55 }
        ]
    }
}
```

**说明**：
- `totalFocusMinutes`: 所有目标所有有效专注时长总和
- `completedGoalCount`: status=COMPLETED 的目标数
- `dailyStats`: 近7天每天的有效专注时长（含当天），按日期倒序

### 5.5 用户管理 API

#### POST /api/user/init
初始化用户（首次打开时调用，根据 device_uuid 查找或创建用户）

**Request**:
```json
{
    "deviceUuid": "3b12f1df-5232-4618-a6b2-7c3d5b2a1e9f"
}
```

**Response**:
```json
{
    "code": 200,
    "data": {
        "id": 1,
        "deviceUuid": "3b12f1df-5232-4618-a6b2-7c3d5b2a1e9f",
        "nickname": "专注大师",
        "avatar": null,
        "totalFocusMinutes": 28920,
        "completedGoalCount": 3,
        "createdAt": "2026-03-01 10:00:00"
    }
}
```

#### GET /api/user/profile
获取当前用户信息

**Headers**: `X-User-Id: 1`

**Response**:
```json
{
    "code": 200,
    "data": {
        "id": 1,
        "nickname": "专注大师",
        "avatar": null,
        "totalFocusMinutes": 28920,
        "completedGoalCount": 3,
        "createdAt": "2026-03-01 10:00:00"
    }
}
```

#### PUT /api/user/profile
修改用户昵称

**Headers**: `X-User-Id: 1`

**Request**:
```json
{
    "nickname": "考研人"
}
```

**Response**:
```json
{
    "code": 200,
    "message": "修改成功"
}
```

**校验规则**：
- `nickname` 长度：1~50字符
- 不可为空字符串或纯空白

---

## 六、前端架构

### 6.1 技术方案

- **单文件 SPA**：所有前端代码集成在 `src/main/resources/static/index.html` 一个文件中
- **Hash 路由**：使用 `location.hash` 实现页面切换（#home, #goals, #my）
- **Tailwind CSS CDN**：通过 CDN 引入，无需构建工具
- **Material Symbols CDN**：图标库
- **Google Fonts CDN**：Manrope + Inter 字体
- **原生 JavaScript**：不使用任何前端框架，用模块化的 JS 对象组织代码

### 6.2 前端代码结构（单文件内）

```html
<!DOCTYPE html>
<html>
<head>
    <!-- CDN引入: Tailwind, Material Symbols, Google Fonts -->
    <!-- Tailwind自定义主题配置 -->
    <!-- 自定义CSS样式 -->
</head>
<body>
    <!-- 底部导航栏（固定） -->
    <nav id="tab-bar">首页 | 目标 | 我的</nav>
    
    <!-- 页面容器 -->
    <div id="app">
        <section id="page-home">...</section>
        <section id="page-goals">...</section>
        <section id="page-my">...</section>
        <!-- 子页面（覆盖式） -->
        <section id="page-create-goal">...</section>
        <section id="page-focus-timer">...</section>
        <section id="page-focus-done">...</section>
        <section id="page-rank-up">...</section>
    </div>

    <script>
        // ===== 路由模块 =====
        const Router = { ... };
        
        // ===== API模块 =====
        const Api = { ... };
        
        // ===== 状态管理 =====
        const Store = { ... };
        
        // ===== 页面渲染模块 =====
        const HomePage = { render(), bindEvents() };
        const GoalsPage = { render(), bindEvents() };
        const MyPage = { render(), bindEvents() };
        const TimerPage = { render(), start(), pause(), resume(), end() };
        
        // ===== 组件模块 =====
        const TimePicker = { init(), getValue() };      // 横向时间选择器
        const RankBar = { render(ranks, current) };     // 段位进度条
        const Chart = { renderBar(dailyStats) };         // 柱状图（CSS实现）
        const Toast = { show(message) };                 // 提示消息
        
        // ===== 初始化 =====
        Router.init();
    </script>
</body>
</html>
```

### 6.3 核心组件设计

#### 横向时间选择器 (TimePicker)
- 纯 CSS + JS 实现横向滚动
- 5~180分钟，间隔1分钟
- 触摸滑动 + 点击选中
- 选中项放大 + 主色调高亮

#### 柱状图 (Chart)
- 纯 CSS 实现（div + flexbox）
- 近7天每天一根柱子
- 高度按比例计算，带动画
- 触摸显示具体数值（tooltip）

#### Toast 提示
- 页面底部弹出，2秒后自动消失
- 用于："即将上线"、操作成功/失败等提示

### 6.4 计时器方案
- 前端使用 `setInterval` 每秒倒计时
- 记录开始时间戳，每次 tick 计算已过时间（防止 setInterval 不精确）
- 结束时将实际时长提交后端
- 切换 Tab/页面不中断（浏览器标签页可见性变化时仍继续计时）

---

## 七、段位计算逻辑

### 7.1 RankService

```java
// 段位阈值（占总时长百分比）
BRONZE:  0%
SILVER:  5%
GOLD:   15%
PLATINUM: 30%
DIAMOND: 50%
STARRY:  75%
KING:  100%

// 计算方法
public Rank calculateRank(long accumulatedMinutes, long totalMinutes) {
    if (totalMinutes <= 0) return BRONZE;
    double percent = (double) accumulatedMinutes / totalMinutes;
    if (percent >= 1.0) return KING;
    if (percent >= 0.75) return STARRY;
    if (percent >= 0.50) return DIAMOND;
    if (percent >= 0.30) return PLATINUM;
    if (percent >= 0.15) return GOLD;
    if (percent >= 0.05) return SILVER;
    return BRONZE;
}
```

---

## 八、pom.xml 依赖变更

需在现有基础上新增：

```xml
<!-- JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- H2 -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

---

## 九、运行与部署

### 9.1 本地开发
```bash
mvn spring-boot:run
# 访问 http://localhost:8080
```

### 9.2 打包
```bash
mvn clean package
# 生成 target/focusmaster-1.0-SNAPSHOT.jar
```

### 9.3 H5 封装 APK（后续）
```bash
# 使用 Capacitor
npm install @capacitor/cli @capacitor/core
npx cap init
npx cap add android
# 将打包后的静态资源放入 Capacitor web 目录
npx cap sync
npx cap open android
# 在 Android Studio 中构建 APK
```

---

## 十、目录变更清单

| 操作 | 路径 | 说明 |
|------|------|------|
| 删除 | `com/example/` 整个包 | 移除旧脚手架代码 |
| 新建 | `com/focusmaster/` | 新项目包结构 |
| 新建 | `src/main/resources/static/` | 前端资源目录 |
| 修改 | `pom.xml` | 新增依赖 |
| 修改 | `application.properties` | H2 配置 |
