# F-Droid 上架完整流程

> **项目**: luomashu-app
> **包名**: com.mispec.luomashu
> **当前版本**: 0.1.0 (versionCode: 1)

---

## 一、上架前检查清单

### 1. 代码准备 ✅

- [x] 项目是开源的 (MIT License)
- [x] 无 Google Play Services 依赖
- [x] 无专有追踪代码
- [x] 无隐藏网络请求
- [ ] LICENSE 文件存在 (需要确认)

### 2. 元数据准备

- [ ] `metadata/com.mispec.luomashu.yml` 完整
- [ ] 中英文描述文件
- [ ] 至少 2 张截图
- [ ] 更新日志文件

### 3. 构建验证

- [ ] 本地 Release 构建成功
- [ ] APK 签名正确
- [ ] ProGuard/R8 配置正常

---

## 二、完整上架步骤

### Step 1: 完善元数据文件

#### 1.1 元数据配置文件

文件位置: `metadata/com.mispec.luomashu.yml`

```yaml
# F-Droid 元数据 - 路码数 (LuomaShu)
# 芬兰语数字学习应用

Categories:
  - Education
  - Learning

License: MIT

# 使用 FastLane 格式的描述
Name: 路码数

Summary: >
  芬兰语数字学习应用 - 快速掌握 0-10 的芬兰语发音和拼写

Description: |
  路码数是一款专为语言学习者设计的芬兰语数字学习应用。

  核心功能:
  - 📚 系统学习: 按难度递进, 从 1 位数到 3 位数
  - 🎯 快速测试: 随机抽取 5 题, 即时检验学习成果
  - 🏆 挑战模式: 连续答对 20 题才算通过
  - 📊 成绩追踪: 每次测试自动记录正确率

  应用特点:
  - 完全离线运行, 无需网络
  - 简洁直观的界面设计
  - 支持中英双语
  - 符合 Material Design 3 规范

  适用人群:
  - 准备去芬兰旅行的游客
  - 芬兰语初学者
  - 对北欧语言感兴趣的学习者

WebSite: https://github.com/linfee/luomashu-app

# 构建方式
AutoUpdateMode: Version
UpdateCheckMode: Tags

# 翻译信息
TranslateName: false
TranslateSummary: false
TranslateDescription: false

# F-Droid 构建配置
Builds:
  - versionName: 0.1.0
    versionCode: 1
    commit: v0.1.0

    # 构建配置
    gradle:
      - yes

    # 源代码
    srcname: luomashu-app
    subdir: android-app
```

#### 1.2 中文描述

文件位置: `fastlane/metadata/android/zh-CN/full_description.txt`

```markdown
路码数 - 芬兰语数字学习应用

快速掌握芬兰语 0-10 的发音和拼写, 专为旅行者和语言学习者设计。

核心功能:

📚 系统学习
按难度递进, 从 1 位数到 3 位数, 循序渐进掌握芬兰语数字。

🎯 快速测试
随机抽取 5 题, 即时检验学习成果, 每次测试都是新体验。

🏆 挑战模式
连续答对 20 题才算通过, 真正掌握才能通关。

📊 成绩追踪
每次测试自动记录正确率, 追踪学习进度。

应用特点:

✓ 完全离线运行, 无需网络连接
✓ 简洁直观的 Material Design 3 界面
✓ 支持中英双语显示
✓ 轻量级应用, 体积小巧
✓ 无需注册, 即装即用

适用人群:

• 准备去芬兰旅行的游客
• 芬兰语初学者
• 对北欧语言感兴趣的学习者
• 语言学习爱好者

为什么选择路码数?

芬兰语数字系统独特, 与英语等语言差异较大。路码数通过:
- 发音展示: 显示芬兰语数字的发音
- 拼写练习: 掌握正确的拼写方式
- 反复测试: 强化记忆, 确保持久掌握

学习芬兰语数字, 从路码数开始!
```

#### 1.3 英文描述

文件位置: `fastlane/metadata/android/en-US/full_description.txt`

```markdown
LuomaShu - Finnish Number Learning App

Master Finnish numbers 0-10 quickly with pronunciation and spelling practice. Designed for travelers and language learners.

Core Features:

📚 Systematic Learning
Progress from 1-digit to 3-digit numbers, building your skills step by step.

🎯 Quick Test
Randomly select 5 questions for instant assessment. Every test is a new experience.

🏆 Challenge Mode
Answer 20 consecutive questions correctly to pass. True mastery required.

📊 Progress Tracking
Automatic accuracy recording after each test to track your learning progress.

App Features:

✓ Fully offline - no internet connection required
✓ Clean Material Design 3 interface
✓ Bilingual support (Chinese & English)
✓ Lightweight and fast
✓ No registration needed

Perfect For:

• Travelers visiting Finland
• Finnish language beginners
• Nordic language enthusiasts
• Language learning hobbyers

Why LuomaShu?

Finnish number system is unique and differs significantly from English. LuomaShu helps you:
- Master pronunciation of Finnish numbers
- Learn correct spelling
- Reinforce memory through repeated practice

Start your Finnish number journey today!
```

#### 1.4 简短描述

**中文** (`zh-CN/short_description.txt`):
```
芬兰语数字学习应用 - 快速掌握 0-10 的芬兰语发音和拼写
```

**英文** (`en-US/short_description.txt`):
```
Finnish number learning app - master 0-10 pronunciation and spelling
```

---

### Step 2: 准备截图

#### 2.1 截图要求

| 类型 | 尺寸 | 数量 | 说明 |
|------|------|------|------|
| 手机截图 | 1080x1920 (9:16) | 至少 2 张 | 必需 |
| 7 英寸平板 | 1200x1920 | 可选 | 推荐 |
| 10 英寸平板 | 1600x2560 | 可选 | 可选 |

#### 2.2 截图内容建议

```
截图 1: 主界面 - 展示 6 大测试类别
截图 2: 测试界面 - 展示芬兰语数字和选项
截图 3: 结果界面 - 展示测试成绩和分享功能
截图 4: 学习界面 - 展示数字详情
```

#### 2.3 截图文件命名

```
fastlane/metadata/android/zh-CN/images/phoneScreenshots/
├── 1.png    # 主界面
├── 2.png    # 测试界面
├── 3.png    # 结果界面
└── 4.png    # 学习界面

fastlane/metadata/android/en-US/images/phoneScreenshots/
├── 1.png
├── 2.png
├── 3.png
└── 4.png
```

---

### Step 3: 本地构建验证

#### 3.1 清理并构建 Release

```bash
cd android-app

# 清理项目
./gradlew clean

# 构建 Release APK
./gradlew assembleRelease

# 检查输出
ls -lh app/build/outputs/apk/release/
```

#### 3.2 验证 APK 签名

```bash
# 查看签名信息
keytool -printcert -jarfile app/build/outputs/apk/release/app-release-unsigned.apk
```

#### 3.3 测试 Release 版本

```bash
# 安装到设备测试
adb install -r app/build/outputs/apk/release/app-release.apk

# 验证功能
# - 启动应用
# - 运行所有测试类别
# - 检查分享功能
```

---

### Step 4: 创建 GitHub Release

#### 4.1 推送代码

```bash
# 确保所有更改已提交
git add .
git commit -m "chore: prepare F-Droid metadata v0.1.0"
git push origin main
```

#### 4.2 创建 Tag

```bash
# 确保 tag 与 build.gradle.kts 中的版本一致
git tag -a v0.1.0 -m "Release v0.1.0 - F-Droid first release"
git push origin v0.1.0
```

#### 4.3 在 GitHub 创建 Release

1. 访问: https://github.com/linfee/luomashu-app/releases
2. 点击 "Create a new release"
3. 选择 tag: `v0.1.0`
4. 填写 Release 信息:

**Title**: `v0.1.0 - F-Droid First Release`

**Description**:
```markdown
## 🎉 首次发布

路码数 v0.1.0 - 芬兰语数字学习应用

### ✨ 新功能

- **系统学习**: 6 大测试类别, 从简单到复杂
- **快速测试**: 随机 5 题, 即时检验
- **挑战模式**: 连续答对 20 题通关
- **成绩追踪**: 自动记录测试成绩

### 📱 支持设备

- Android 8.0 (API 26) 及以上
- 支持手机和平板

### 🚀 安装方式

- **F-Droid**: 等待审核后可从 F-Droid 安装
- **直接下载**: 从本 Release 页面下载 APK

### 📝 更新日志

- 初始版本发布
- 支持芬兰语 0-10 数字学习
- 中英双语界面
- Material Design 3 设计
```

---

### Step 5: 提交到 F-Droid

#### 方式 A: Request for Packaging (推荐)

1. 访问 F-Droiddata Issues: https://gitlab.com/fdroid/fdroiddata/-/issues
2. 点击 "New issue"
3. 填写以下内容:

**Title**: `Request for Packaging: com.mispec.luomashu`

**Description**:
```markdown
## App Information

- **App Name**: 路码数 (LuomaShu)
- **Package Name**: com.mispec.luomashu
- **Source Code**: https://github.com/linfee/luomashu-app
- **License**: MIT
- **Categories**: Education, Learning

## Description

LuomaShu is a Finnish number learning app designed for travelers and language learners. It helps users master Finnish numbers 0-10 through:

- Systematic learning (1-digit to 3-digit numbers)
- Quick tests (random 5 questions)
- Challenge mode (20 consecutive correct answers)
- Progress tracking

Key features:
- Fully offline - no internet required
- Clean Material Design 3 interface
- Bilingual support (Chinese & English)
- Lightweight and fast

## Build Instructions

The Android app is located in the `android-app` subdirectory.

```yaml
AutoUpdateMode: Version
UpdateCheckMode: Tags
Builds:
  - versionName: 0.1.0
    versionCode: 1
    commit: v0.1.0
    gradle:
      - yes
    srcname: luomashu-app
    subdir: android-app
```

## Additional Notes

- No Google Play Services dependencies
- No proprietary tracking or analytics
- Uses only open-source libraries
- Build is reproducible

## Screenshots

Screenshots are available in `fastlane/metadata/android/` directory.

Thank you for considering this app for F-Droid!
```

#### 方式 B: 直接提交 Merge Request

1. Fork https://gitlab.com/fdroid/fdroiddata
2. 在 `metadata/` 目录下创建 `com.mispec.luomashu.yml`
3. 复制上面的元数据配置
4. 提交 Merge Request

---

### Step 6: 等待审核

#### 审核流程

1. **自动构建**: F-Droid 服务器自动克隆仓库并构建
2. **代码扫描**: 检查是否有专有依赖或追踪代码
3. **签名验证**: 验证 APK 签名是否正确
4. **人工审核**: 维护者进行最终审核
5. **发布上线**: 审核通过后发布到 F-Droid

#### 时间预估

- 首次提交: 1-2 周
- 后续更新: 1-3 天

#### 审核状态查询

访问 F-Droiddata 的 Merge Request 页面查看审核进度。

---

## 三、更新版本流程

当新版本发布时：

### 1. 更新版本号

文件: `android-app/app/build.gradle.kts`

```kotlin
defaultConfig {
    versionCode = 2  // 递增
    versionName = "0.2.0"  // 新版本号
}
```

### 2. 添加更新日志

文件: `fastlane/metadata/android/changelogs/2.txt`

```markdown
## v0.2.0

### 新功能
- 新增 XX 功能
- 优化 XX 体验

### 修复
- 修复 XX 问题
```

### 3. 更新元数据

文件: `metadata/com.mispec.luomashu.yml`

```yaml
Builds:
  - versionName: 0.2.0
    versionCode: 2
    commit: v0.2.0
    gradle:
      - yes
    srcname: luomashu-app
    subdir: android-app
```

### 4. 推送并创建 Release

```bash
git add .
git commit -m "chore: prepare v0.2.0"
git push origin main

git tag -a v0.2.0 -m "Release v0.2.0"
git push origin v0.2.0
```

F-Droid 会自动检测新版本并构建。

---

## 四、常见问题

### Q1: 构建失败怎么办?

**A**: 检查以下几点：

1. **Gradle 版本**: 确保使用兼容的 Gradle 版本
2. **依赖问题**: 检查是否有专有依赖
3. **签名配置**: 确保签名配置正确

### Q2: 如何验证构建是否可重现?

**A**: 
```bash
# 清理并重新构建
./gradlew clean assembleRelease

# 对比 APK
sha256sum app/build/outputs/apk/release/app-release.apk
```

### Q3: 可以使用 Google Play Services 吗?

**A**: 不可以。F-Droid 禁止使用任何 Google 专有服务。

### Q4: 需要提供源代码吗?

**A**: 是的。F-Droid 要求完全开源，包括所有依赖库。

### Q5: 审核被拒怎么办?

**A**: 根据拒绝原因修改后重新提交。常见问题：
- 专有依赖 → 替换为开源库
- 追踪代码 → 移除所有追踪
- 签名问题 → 检查签名配置

---

## 五、自动化脚本

### 构建脚本

```bash
#!/bin/bash
# build-for-fdroid.sh

set -e

echo "🔨 开始构建 F-Droid 版本..."

cd android-app

# 清理
./gradlew clean

# 构建 Release
./gradlew assembleRelease

# 检查输出
APK_PATH="app/build/outputs/apk/release/app-release.apk"
if [ -f "$APK_PATH" ]; then
    echo "✅ 构建成功!"
    echo "📦 APK 路径: $APK_PATH"
    echo "📊 文件大小: $(ls -lh $APK_PATH | awk '{print $5}')"
else
    echo "❌ 构建失败!"
    exit 1
fi
```

### 截图准备脚本

```bash
#!/bin/bash
# prepare-screenshots.sh

set -e

SCREENSHOT_DIR_ZH="fastlane/metadata/android/zh-CN/images/phoneScreenshots"
SCREENSHOT_DIR_EN="fastlane/metadata/android/en-US/images/phoneScreenshots"

echo "📸 准备截图目录..."

mkdir -p "$SCREENSHOT_DIR_ZH"
mkdir -p "$SCREENSHOT_DIR_EN"

echo "✅ 截图目录已创建"
echo ""
echo "请将截图放入以下目录:"
echo "  中文: $SCREENSHOT_DIR_ZH"
echo "  英文: $SCREENSHOT_DIR_EN"
echo ""
echo "截图要求:"
echo "  - 尺寸: 1080x1920 (9:16)"
echo "  - 数量: 至少 2 张"
echo "  - 命名: 1.png, 2.png, 3.png ..."
```

### 提交检查脚本

```bash
#!/bin/bash
# check-fdroid-ready.sh

set -e

echo "🔍 检查 F-Droid 上架准备情况..."
echo ""

ERRORS=0

# 检查 LICENSE 文件
if [ ! -f "LICENSE" ]; then
    echo "❌ 缺少 LICENSE 文件"
    ERRORS=$((ERRORS + 1))
else
    echo "✅ LICENSE 文件存在"
fi

# 检查元数据
if [ ! -f "metadata/com.mispec.luomashu.yml" ]; then
    echo "❌ 缺少元数据文件"
    ERRORS=$((ERRORS + 1))
else
    echo "✅ 元数据文件存在"
fi

# 检查中文描述
if [ ! -f "fastlane/metadata/android/zh-CN/full_description.txt" ]; then
    echo "❌ 缺少中文描述"
    ERRORS=$((ERRORS + 1))
else
    echo "✅ 中文描述存在"
fi

# 检查英文描述
if [ ! -f "fastlane/metadata/android/en-US/full_description.txt" ]; then
    echo "❌ 缺少英文描述"
    ERRORS=$((ERRORS + 1))
else
    echo "✅ 英文描述存在"
fi

# 检查截图
ZHS=$(ls fastlane/metadata/android/zh-CN/images/phoneScreenshots/*.png 2>/dev/null | wc -l)
ENS=$(ls fastlane/metadata/android/en-US/images/phoneScreenshots/*.png 2>/dev/null | wc -l)

if [ "$ZHS" -lt 2 ]; then
    echo "❌ 中文截图不足 (需要至少 2 张, 当前 $ZHS 张)"
    ERRORS=$((ERRORS + 1))
else
    echo "✅ 中文截图足够 ($ZHS 张)"
fi

if [ "$ENS" -lt 2 ]; then
    echo "❌ 英文截图不足 (需要至少 2 张, 当前 $ENS 张)"
    ERRORS=$((ERRORS + 1))
else
    echo "✅ 英文截图足够 ($ENS 张)"
fi

# 检查 Google Play Services 依赖
if grep -r "play-services" android-app/app/build.gradle.kts; then
    echo "❌ 发现 Google Play Services 依赖"
    ERRORS=$((ERRORS + 1))
else
    echo "✅ 无 Google Play Services 依赖"
fi

echo ""
if [ $ERRORS -eq 0 ]; then
    echo "🎉 所有检查通过, 可以提交到 F-Droid!"
else
    echo "⚠️  发现 $ERRORS 个问题, 请修复后重试"
    exit 1
fi
```

---

## 六、提交前最终检查

运行以下命令确认一切就绪：

```bash
# 1. 运行检查脚本
./check-fdroid-ready.sh

# 2. 构建 Release
./build-for-fdroid.sh

# 3. 准备截图
./prepare-screenshots.sh

# 4. 提交代码
git add .
git commit -m "chore: prepare F-Droid metadata"
git push origin main

# 5. 创建 Release
git tag -a v0.1.0 -m "F-Droid first release"
git push origin v0.1.0
```

---

## 七、相关链接

- **F-Droid 官网**: https://f-droid.org/
- **F-Droiddata 仓库**: https://gitlab.com/fdroid/fdroiddata
- **提交 Issue**: https://gitlab.com/fdroid/fdroiddata/-/issues
- **F-Droid 文档**: https://f-droid.org/wiki/
- **应用页面** (发布后): https://f-droid.org/packages/com.mispec.luomashu/

---

*最后更新: 2026-06-22*
