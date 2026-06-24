# 脑洞测试 (NaoDong CeShi)

A Jetpack Compose Android app with 58 interactive cognitive, personality, sensory, and knowledge tests.

## Architecture

- **UI**: Jetpack Compose + Material 3, single-activity
- **Language**: Kotlin 2.1.0
- **Build**: Gradle 8.7.3, Android Gradle Plugin 8.7.3
- **Target**: Android 14 (API 35), min SDK 26
- **State**: `rememberSaveable` + `DataStore` for best scores
- **Audio**: `AudioTrack` sine-wave synthesis (no resource files)

## Project Structure

```
app/src/main/java/com/mispec/luomashu/
  MainActivity.kt          — Entry point, CompositionLocal provider
  audio/AudioEngine.kt     — Pure sine-wave tone generator
  data/
    ToolDef.kt             — Tool definition model + 6 category lists (58 tools)
    Category.kt            — Category enum with labels, icons, colors
  tools/                   — Tool composable wrappers (delegate to UI components)
  ui/
    home/HomeScreen.kt     — Navigation, home grid, category list, tool router
    components/
      SharedToolScreens.kt — ClickToolScreen, QuizToolScreen, ScaleToolScreen
      CustomToolScreens.kt — 24 custom interactive tool composables
    theme/Theme.kt         — Light/dark Material 3 color schemes
  util/
    ScreenState.kt         — ToolScreenState sealed class + sem() utility
    BestScoreStore.kt      — DataStore-backed best score persistence
```

## Build

```bash
cd android-app
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

## Test

Appium WebDriverIO tests in `tests/appium/`.
```bash
cd tests/appium
npm install
npx wdio run wdio.conf.js
```

## Permissions

No internet or dangerous permissions required. Offline-first design.
