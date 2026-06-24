package com.mispec.luomashu.ui.components

import com.mispec.luomashu.util.sem
import com.mispec.luomashu.util.ToolScreenState
import com.mispec.luomashu.util.DisclaimerBanner
import com.mispec.luomashu.util.rememberBestScore
import com.mispec.luomashu.util.randomEncouragement
import com.mispec.luomashu.util.performHaptic
import com.mispec.luomashu.util.createResultBitmap
import com.mispec.luomashu.util.shareResult

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Quiz Tool Screen ──

data class QuizQuestion(val question: String, val options: List<String>, val answer: Int) {
    init {
        require(options.size in 2..8) { "QuizQuestion must have 2-8 options, got ${options.size}" }
        require(answer in options.indices) { "answer $answer out of bounds [0, ${options.lastIndex}] for '$question'" }
    }
}

@Composable
fun QuizToolScreen(title: String, questions: List<QuizQuestion>, toolId: String = "") {
    if (questions.isEmpty()) return
    var shuffled by remember { mutableStateOf(questions.shuffled()) }
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }
    var qIdx by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    val best = rememberBestScore(toolId)
    val context = LocalContext.current
    val view = LocalView.current

    // Update best score only when state changes to Result (not on every recomposition)
    LaunchedEffect(state) {
        if (state == ToolScreenState.Result && score > best.value) {
            best.value = score
        }
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Spacer(Modifier.height(20.dp))

        Surface(
            color = MaterialTheme.colorScheme.tertiaryContainer,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text(
                "本测试仅供娱乐参考, 不构成专业评估",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp).fillMaxWidth(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                textAlign = TextAlign.Center
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) {
                Text(
                    title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        when {
            state == ToolScreenState.Idle -> {
                DisclaimerBanner()
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = { state = ToolScreenState.Playing; qIdx = 0; score = 0; shuffled = questions.shuffled() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .then(sem("btn-primary", "开始测试按钮"))
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("开始测试", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(32.dp))
            }
            state == ToolScreenState.Playing && qIdx < shuffled.size -> {
                val q = shuffled[qIdx]

                // Progress
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    LinearProgressIndicator(
                        progress = { qIdx.toFloat() / shuffled.size },
                        modifier = Modifier.weight(1f).height(4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "${qIdx + 1}/${shuffled.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Question card
                ElevatedCard(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Column(modifier = Modifier.semantics { testTag = "quiz-question"; contentDescription = "quiz-question" }) {
                            Text(
                                q.question,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Option buttons
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    q.options.forEachIndexed { i, opt ->
                        OutlinedButton(
                            onClick = {
                                performHaptic(view)
                                try {
                                    if (i == q.answer) score++
                                    qIdx++
                                    if (qIdx >= shuffled.size) state = ToolScreenState.Result
                                } catch (e: Exception) {
                                    android.util.Log.e("QuizToolScreen", "Option click error", e)
                                    state = ToolScreenState.Result
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(sem("quiz-option-$i")),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(
                                opt,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.weight(1f))
            }
            state == ToolScreenState.Result -> {
                Spacer(Modifier.weight(1f))

                val quizEncouragement = remember { randomEncouragement() }
                ElevatedCard(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.padding(24.dp).then(sem("result-screen", "测试结果")),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.EmojiEvents,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("score-value")) {
                            Text(
                                "$score/${shuffled.size}",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 52.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "正确率: ${score * 100 / shuffled.size}%",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (best.value > 0) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "历史最佳: ${best.value}/${shuffled.size}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }

                        Spacer(Modifier.height(12.dp))
                        Text(
                            quizEncouragement,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                FilledTonalButton(
                    onClick = { performHaptic(view); state = ToolScreenState.Idle },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .then(sem("btn-secondary", "再来一次按钮"))
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("再来一次", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        performHaptic(view)
                        val scoreText = "$score/${shuffled.size}"
                        val bitmap = createResultBitmap(title, scoreText, quizEncouragement)
                        shareResult(context, bitmap, title, scoreText)
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Icon(Icons.Filled.Share, contentDescription = "分享结果", modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("分享成绩", fontSize = 13.sp)
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
