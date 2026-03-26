# FocusMaster 项目概览

## 一、项目背景
开发一款专注类App，帮助用户设定专注目标、跟踪专注时长、通过游戏化段位升级机制激励用户持续专注。

## 二、核心功能需求

### 2.1 专注目标管理
- 用户可自定义专注目标名称（如：考研、考教师资格证等）
- 用户可设置目标专注总时长（预设选项：50h/100h/200h/自定义）
- 支持多个目标，但同一时间只有一个"进行中"的目标
- 可随时结束进行中的目标
- 已结束目标可重新启动，已积累时长继续累积

### 2.2 段位等级系统
- 7个大段位：青铜 → 白银 → 黄金 → 白金 → 钻石 → 星耀 → 王者
- 阶梯式升级，越往后升级所需时长越长
- 段位划分基于用户设置的目标总时长按比例计算

### 2.3 专注计时
- 选中目标后可开启单次专注
- 用户设置本次专注时长（最短5分钟）
- 后台运行计时
- 专注时长不满5分钟的不计入累计
- 满5分钟的部分按1小时为单位取整计入

### 2.4 计算规则
- 单次专注时长 < 5分钟：不计入累计时长
- 单次专注时长 >= 5分钟：计入累计时长（向上取整到小时）
  - 例：已累计100h5min，本次专注55min → 总累计101h

## 三、项目文档索引
| 文档 | 路径 | 状态 |
|------|------|------|
| 团队结构 | `.codebuddy/project-management/team-structure.md` | 已完成 |
| 项目概览 | `.codebuddy/project-management/project-overview.md` | 已完成 |
| 产品需求文档 | `.codebuddy/project-management/prd.md` | 待完成 |
| UI设计规范 | `.codebuddy/project-management/ui-design-spec.md` | 待完成 |
| 技术架构文档 | `.codebuddy/project-management/tech-architecture.md` | 待完成 |
| API接口文档 | `.codebuddy/project-management/api-docs.md` | 待完成 |
| 测试报告 | `.codebuddy/project-management/test-report.md` | 待完成 |
