package com.mispec.luomashu.tools

import androidx.compose.runtime.Composable
import com.mispec.luomashu.ui.components.*

// 神经反应 — 6 tools, each with its authentic test mechanic

@Composable fun ApmScreen() = ClickToolScreen("APM 测试", "10秒内尽可能多点", 10, toolId = "apm") {
    val apm = it * 6; when { apm > 400 -> "S级 ${apm} APM"; apm > 300 -> "A级 ${apm} APM"; apm > 200 -> "B级 ${apm} APM"; else -> "C级 ${apm} APM" }
}
@Composable fun CpsScreen() = ClickToolScreen("CPS 连点测试", "10秒狂点区域", 10, toolId = "cps") {
    val cps = "%.1f".format(it / 10.0); when { it > 80 -> "$cps CPS ⚡ 快手"; it > 50 -> "$cps CPS 👍"; else -> "$cps CPS" }
}
@Composable fun TimingChallengeScreen2() = TimingChallengeScreen(5, toolId = "timing-challenge")
@Composable fun ChoiceReactionScreen2() = ChoiceReactionScreen(toolId = "choice-reaction")
@Composable fun GamingTalentScreen() = ClickToolScreen("游戏天赋测试", "10秒内疯狂输出", 10, toolId = "gaming-talent") {
    val apm = it * 6
    when { apm > 400 -> "S级 职业水准 (${apm} APM)"; apm > 300 -> "A级 高端玩家 (${apm} APM)"; apm > 200 -> "B级 熟练玩家 (${apm} APM)"; else -> "探索者 (${apm} APM)" }
}
@Composable fun ReactionTimeScreen2() = ReactionTimeScreen(toolId = "reaction-time")
