# 专注大师 (FocusMaster)

一款基于游戏化段位机制的专注力管理应用，帮助你通过设定目标、记录专注时长，逐步从「青铜」晋升到「王者」。

## 功能特性

- **目标管理** — 创建专注目标，设定总时长，追踪完成进度
- **专注计时** — 支持 5~180 分钟自由设定，支持暂停/继续
- **段位系统** — 累计专注时长自动晋升段位（青铜 → 白银 → 黄金 → 白金 → 钻石 → 星耀 → 王者）
- **数据统计** — 近期专注时长柱状图、累计专注时长、已完成目标数
- **PWA 支持** — 支持添加到手机主屏幕，类原生应用体验

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Spring Boot 2.7 + Spring Data JPA |
| 数据库 | H2（文件持久化） |
| 前端 | 原生 HTML/CSS/JS（单页面应用） |
| 部署 | Docker + Railway |

## 段位体系

| 段位 | 累计进度 |
|------|---------|
| 青铜 | 0% |
| 白银 | 5% |
| 黄金 | 15% |
| 白金 | 30% |
| 钻石 | 50% |
| 星耀 | 75% |
| 王者 | 100% |

## 本地开发

```bash
# 确保已安装 JDK 17 和 Maven
git clone https://github.com/WileyWong23/focusmaster.git
cd focusmaster
mvn spring-boot:run
```

访问 http://localhost:8080 即可使用。

## 在线访问

https://focusmaster-production.up.railway.app

## API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/user/init` | 用户初始化 |
| GET | `/api/user/profile` | 获取用户资料 |
| PUT | `/api/user/profile` | 修改昵称 |
| POST | `/api/goals` | 创建目标 |
| GET | `/api/goals` | 获取所有目标 |
| POST | `/api/goals/{id}/end` | 结束目标 |
| POST | `/api/goals/{id}/restart` | 重新启动目标 |
| POST | `/api/sessions/start` | 开始专注 |
| POST | `/api/sessions/{id}/end` | 结束专注 |
| GET | `/api/stats/overview` | 统计概览 |

## License

MIT
