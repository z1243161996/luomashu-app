# 路码数 (LuomaShu)

> 芬兰语数字学习应用 - 快速掌握 0-10 的芬兰语发音和拼写

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Android-8.0%2B-brightgreen.svg)](https://developer.android.com/about/versions/oreo)

## ✨ 功能特性

- 📚 **系统学习**: 按难度递进, 从 1 位数到 3 位数
- 🎯 **快速测试**: 随机抽取 5 题, 即时检验学习成果
- 🏆 **挑战模式**: 连续答对 20 题才算通过
- 📊 **成绩追踪**: 每次测试自动记录正确率

## 📱 支持设备

- Android 8.0 (API 26) 及以上
- 支持手机和平板

## 🛠️ 技术栈

- **语言**: Kotlin 2.1.0
- **UI**: Jetpack Compose + Material Design 3
- **数据存储**: DataStore Preferences
- **音频**: AudioTrack

## 📦 安装

### 从 GitHub Release 下载

1. 访问 [Releases](https://github.com/z1243161996/luomashu-app/releases)
2. 下载最新的 APK 文件
3. 在 Android 设备上安装

### 从源码构建

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

## 📁 项目结构

```
luomashu-app/
├── android-app/          # Android 应用源代码
├── fastlane/             # F-Droid 元数据
├── metadata/             # F-Droid 配置
├── F-DROID-LAUNCH-GUIDE.md
├── README.md
└── .gitignore
```

## 📄 许可证

MIT License

## 🔗 相关链接

- [F-Droid 上架指南](F-DROID-LAUNCH-GUIDE.md)
- [GitHub Releases](https://github.com/z1243161996/luomashu-app/releases)
