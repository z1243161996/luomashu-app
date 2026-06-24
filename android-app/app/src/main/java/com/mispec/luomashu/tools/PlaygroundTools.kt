package com.mispec.luomashu.tools

import androidx.compose.runtime.Composable
import com.mispec.luomashu.ui.components.*

@Composable fun MinesweeperScreen() = MinesweeperGameScreen(toolId = "minesweeper")
@Composable fun GuessNumberScreen() = GuessNumberGameScreen(toolId = "guess-number")
@Composable fun ChaseButtonScreen() = ChaseButtonGameScreen(toolId = "chase-button")
@Composable fun DontPressScreen() = DontPressGameScreen(toolId = "dont-press")
@Composable fun CircleChallengeScreen() = CircleChallengeGameScreen(toolId = "circle-challenge")
@Composable fun DndScreen() = QuizToolScreen("D&D 阵营测试", listOf(
    QuizQuestion("遇到不公你会？", listOf("挺身而出","遵守规则","随机应变","视情况"), 0),
    QuizQuestion("你更看重？", listOf("个人自由","社会责任","两者兼顾","都不看重"), 2),
    QuizQuestion("面对危险你？", listOf("迎难而上","谨慎评估","随机而动","退缩"), 1),
    QuizQuestion("你相信？", listOf("因果报应","优胜劣汰","中庸之道","命运天定"), 3),
    QuizQuestion("你的性格倾向？", listOf("善良","守序","混乱","中立"), 1),
    QuizQuestion("最好的决策来自？", listOf("内心直觉","逻辑分析","权衡利弊","他人建议"), 0),
    QuizQuestion("如何对待弱者？", listOf("保护他们","顺其自然","利用他们","不管"), 0),
    QuizQuestion("理想的社会是？", listOf("人人平等","精英统治","自由竞争","无为而治"), 2),
    QuizQuestion("你更接近？", listOf("正义斗士","秩序维护者","自由灵魂","观察者"), 3),
    QuizQuestion("最大的美德是？", listOf("诚实","忠诚","自由","智慧"), 1),
    QuizQuestion("权力的意义是？", listOf("服务他人","维护秩序","追求自由","自我保护"), 2),
    QuizQuestion("对未来的态度？", listOf("积极改变","守住传统","随波逐流","冷眼旁观"), 0),
), toolId = "dnd-alignment")
@Composable fun EmojiQuizScreen() = QuizToolScreen("Emoji 猜谜", listOf(
    QuizQuestion("🐝🐝🐝🐝 → 成语", listOf("蜂拥而至","一窝蜂","蜂拥而上","蜂群"), 0),
    QuizQuestion("🌧️ → 🐱 → 🐶 → 成语", listOf("倾盆大雨","狂风暴雨","牛毛细雨","和风细雨"), 2),
    QuizQuestion("🍎 → 👨‍⚕️ → 🏥 → 俗语", listOf("一天一苹果医生远离我","苹果好吃","医院很贵","多吃水果"), 0),
    QuizQuestion("📖 → 📖 → 📖 → 成语", listOf("书山有路","学富五车","博览群书","汗牛充栋"), 1),
    QuizQuestion("⚡⚡⚡ → 成语", listOf("雷厉风行","电闪雷鸣","闪电般","迅雷不及"), 3),
    QuizQuestion("🦁🦁🦁 → 成语", listOf("狮虎相争","河东狮吼","如虎添翼","龙腾虎跃"), 1),
    QuizQuestion("🐎🐎🐎 → 成语", listOf("马到成功","万马齐喑","马不停蹄","老马识途"), 2),
    QuizQuestion("🎭 → 🎬 → 表示什么", listOf("戏剧","搞笑","严肃","平淡"), 0),
    QuizQuestion("🔥 -> 🚒 -> 👨‍🚒 → 什么事", listOf("救火","做饭","烧烤","取暖"), 3),
    QuizQuestion("🐢 + 🐇 → 比赛 → 寓意", listOf("持之以恒","速度","龟兔","动物"), 0),
    QuizQuestion("💡 → 💭 → 成语", listOf("灵光一现","灯亮","灵感","突发"), 1),
    QuizQuestion("🌙 → 🌟 → 💤 → 成语", listOf("披星戴月","月明星稀","数星星","熬夜"), 2),
), toolId = "emoji-quiz")
