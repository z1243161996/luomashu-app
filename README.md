# 脑洞测试 (LuomaShu)

> 发现你不为人知的一面！58 种趣味测试等你来挑战 🎮

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Android-8.0%2B-brightgreen.svg)](https://developer.android.com/about/versions/oreo)

## ✨ 你是什么样的人？

- 🧠 你的反应速度能打败多少人？
- 💭 你的 IQ 真的很高吗？
- 🪞 你是 MBTI 里的哪个类型？
- 👁️ 你的视力和听力还正常吗？
- 🎮 你能连续答对 20 题吗？

**下载脑洞测试，找到答案！**

---

## 🎮 测试分类

### 🧠 神经反应 (6 个)
APM 连点、CPS 测试、反应速度、选择反应、游戏天赋、滚动速度

### 💭 思维认知 (13 个)
记忆力、舒尔特方格、IQ 测试、逻辑推理、数学速算、多任务处理、专注力训练...

### 🪞 性格镜像 (12 个)
MBTI、大五人格、九型人格、情商测试、霍兰德职业、性格色彩、心理年龄...

### 📚 知识百科 (12 个)
成语接龙、诗词填空、化学元素、地理知识、历史问答、英语词汇...

### 👁️ 感官挑战 (8 个)
视力测试、听力测试、音高辨别、色觉测试、动态视力、节奏感...

### 🎮 趣味游乐 (7 个)
扫雷、猜数字、追按钮、别按红色、emoji 猜谜、D&D 阵营...

---

## 📱 下载安装

### GitHub Release

👉 [点击下载最新版本](https://github.com/z1243161996/luomashu-app/releases)

---

## 🛠️ 从源码构建

```bash
# 前置条件
# - Android Studio Hedgehog+
# - JDK 17
# - Android SDK (API 35)

cd android-app
./gradlew assembleDebug

# 安装到设备
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 📁 项目结构

```
android-app/
├── app/src/main/java/com/mispec/luomashu/
│   ├── MainActivity.kt           # 入口
│   ├── audio/AudioEngine.kt      # 音频引擎
│   ├── data/                     # 数据模型
│   ├── tools/                    # 58 种测试工具
│   ├── ui/                       # UI 界面
│   └── util/                     # 工具类
```

---

## 📄 许可证

MIT License
