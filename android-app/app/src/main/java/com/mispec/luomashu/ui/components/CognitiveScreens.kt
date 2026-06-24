package com.mispec.luomashu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size as CSize
import androidx.compose.ui.graphics.drawscope.Stroke as DrawStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.mispec.luomashu.audio.AudioEngine
import com.mispec.luomashu.util.sem
import com.mispec.luomashu.util.ToolScreenState
import com.mispec.luomashu.util.LocalBestScore
import com.mispec.luomashu.util.rememberBestScore
import com.mispec.luomashu.util.performHaptic
import com.mispec.luomashu.util.randomEncouragement
import com.mispec.luomashu.util.createResultBitmap
import com.mispec.luomashu.util.shareResult
import com.mispec.luomashu.util.BestScoreStore
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalContext
import com.mispec.luomashu.util.DisclaimerBanner
import com.mispec.luomashu.ui.theme.AppColors
import android.view.HapticFeedbackConstants
import android.view.View
import kotlin.math.abs
import kotlin.random.Random

private val stroopColors = listOf("\u7ea2" to AppColors.danger, "\u7eff" to AppColors.success, "\u84dd" to AppColors.accent, "\u9ec4" to AppColors.catKnowledge)

@Composable
fun SchulteGridScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }
    var grid by remember { mutableStateOf(listOf<Int>()) }
    var nextNum by remember { mutableIntStateOf(1) }
    var errors by remember { mutableIntStateOf(0) }
    var startTime by remember { mutableLongStateOf(0L) }
    var elapsedStr by remember { mutableStateOf("") }
    val bestTimeMs = rememberBestScore(toolId, Int.MAX_VALUE)
    val view = LocalView.current
    val context = LocalContext.current

    fun startGame() { grid = (1..25).shuffled(); nextNum = 1; errors = 0; startTime = System.nanoTime(); state = ToolScreenState.Playing }

    LaunchedEffect(state) {
        if (state == ToolScreenState.Playing) {
            delay(180_000L)
            if (state == ToolScreenState.Playing) {
                elapsedStr = "%.3f".format((System.nanoTime() - startTime) / 1_000_000_000.0)
                state = ToolScreenState.Result
            }
        }
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) {
            Text("舒尔特方格", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("按顺序点击 1 → 25", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (bestTimeMs.value < Int.MAX_VALUE) Text("最佳: %4.2fs".format(bestTimeMs.value / 1000f), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(12.dp))
        when (state) {
            ToolScreenState.Idle -> {
                Spacer(Modifier.weight(1f))
                DisclaimerBanner()
                Button(onClick = { startGame() }, shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("开始测试", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }
                Spacer(Modifier.weight(1f))
            }
            ToolScreenState.Playing -> {
                Text("下一个: $nextNum", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text("错误: $errors", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
                Column(Modifier.aspectRatio(1f).padding(4.dp).sem("interact-area")) {
                    for (row in 0..4) {
                        Row(Modifier.fillMaxWidth().weight(1f)) {
                            for (col in 0..4) {
                                val idx = row * 5 + col; val num = grid[idx]
                                val isNext = num == nextNum; val isDone = num < nextNum
                                Surface(
                                    modifier = Modifier.weight(1f).fillMaxHeight().padding(2.dp)
                                        .clip(RoundedCornerShape(8.dp)).clickable {
                                            if (isNext) { nextNum++; if (nextNum > 25) {
                                                val elapsedMs = ((System.nanoTime() - startTime) / 1_000_000).toLong()
                                                elapsedStr = "%.2fs".format(elapsedMs / 1000f)
                                                if (elapsedMs < bestTimeMs.value) { bestTimeMs.value = elapsedMs.toInt() }
                                                state = ToolScreenState.Result
                                            } } else if (num > nextNum) errors++
                                        },
                                    color = when { isDone -> MaterialTheme.colorScheme.surfaceVariant; isNext -> MaterialTheme.colorScheme.primaryContainer; else -> MaterialTheme.colorScheme.surface },
                                    tonalElevation = if (isNext) 4.dp else 0.dp
                                ) {
                                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text("$num", fontSize = if (isDone) 12.sp else 16.sp,
                                            fontWeight = if (isNext) FontWeight.ExtraBold else FontWeight.Medium,
                                            color = when { isDone -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f); isNext -> MaterialTheme.colorScheme.primary; else -> MaterialTheme.colorScheme.onSurface }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ToolScreenState.Result -> {
                Spacer(Modifier.weight(0.5f))
                ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(28.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("舒尔特方格", style = MaterialTheme.typography.titleMedium)
                        if (bestTimeMs.value < Int.MAX_VALUE) Text("最佳: %4.2fs".format(bestTimeMs.value/1000f), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                        Spacer(Modifier.height(8.dp))
                        Text(if (elapsedStr.isEmpty()) "超时" else "耗时 $elapsedStr", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        Text("偏差 ${errors} 次", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.height(20.dp))
                FilledTonalButton(onClick = { state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")
                ) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("再来一次") }
                Spacer(Modifier.height(8.dp))
                val enc = remember { randomEncouragement() }
                Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("舒尔特方格", elapsedStr, enc); shareResult(context, bitmap, "舒尔特方格", elapsedStr) }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "分享结果", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("分享成绩", fontSize = 13.sp) }
            }
            else -> {}
        }
    }
}


// ═══ Stroop Test ═══
@Composable fun StroopTestScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var score by remember { mutableIntStateOf(0) }; val best = rememberBestScore(toolId); val view = LocalView.current; val context = LocalContext.current
    var round by remember { mutableIntStateOf(0) }; var match by remember { mutableStateOf(true) }; var textIdx by remember { mutableIntStateOf(0) }; var colorIdx by remember { mutableIntStateOf(0) }; val total = 20
    fun next() { textIdx = Random.nextInt(4); colorIdx = Random.nextInt(4); match = textIdx == colorIdx }
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(24.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u65af\u7279\u9c81\u666e\u6d4b\u8bd5", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u5b57\u7684\u989c\u8272\u548c\u5b57\u7684\u610f\u601d\u4e00\u6837\u5417\uff1f", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value} / $total", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.weight(1f))
        when (state) {
            ToolScreenState.Idle -> { DisclaimerBanner(); Button(onClick = { state = ToolScreenState.Playing; score = 0; round = 0; next() }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) } }
            ToolScreenState.Playing -> { Text("$round / $total | \u6b63\u786e\u7387 ${if (round > 0) score * 100 / round else 0}%", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary); Spacer(Modifier.height(16.dp)); Text(stroopColors[textIdx].first, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = stroopColors[colorIdx].second); Spacer(Modifier.height(24.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { Button(onClick = { if (match) score++; round++; if (round >= total) { if (score > best.value) best.value = score; state = ToolScreenState.Result } else next() }, shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f).height(52.dp).padding(end = 8.dp), colors = ButtonDefaults.buttonColors(containerColor = AppColors.success)) { Text("\u76f8\u540c", fontWeight = FontWeight.Bold, color = Color.White) }; Button(onClick = { if (!match) score++; round++; if (round >= total) { if (score > best.value) best.value = score; state = ToolScreenState.Result } else next() }, shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f).height(52.dp).padding(start = 8.dp), colors = ButtonDefaults.buttonColors(containerColor = AppColors.danger)) { Text("\u4e0d\u540c", fontWeight = FontWeight.Bold, color = Color.White) } } }
            ToolScreenState.Result -> { Spacer(Modifier.height(24.dp)); ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) { Text("\u65af\u7279\u9c81\u666e\u6d4b\u8bd5", style = MaterialTheme.typography.titleMedium); if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value} / $total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary); Spacer(Modifier.height(8.dp)); Text("$score / $total", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) } }; Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                Spacer(Modifier.height(8.dp))
                val enc = remember { randomEncouragement() }
                Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("斯特鲁普测试", "$score", enc); shareResult(context, bitmap, "斯特鲁普测试", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}


// ═══ Chimp Test ═══
@Composable fun ChimpTestScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var level by remember { mutableIntStateOf(1) }; var positions by remember { mutableStateOf(listOf<Offset>()) }; var visible by remember { mutableStateOf(true) }; var nextClick by remember { mutableIntStateOf(1) }; val bestLvl = rememberBestScore(toolId); var errorFlash by remember { mutableStateOf(false) }; val view = LocalView.current; val context = LocalContext.current
    fun nextLevel() { level++; positions = (0 until minOf(level + 2, 12)).map { Offset(Random.nextFloat() * 0.7f + 0.15f, Random.nextFloat() * 0.6f + 0.15f) }; visible = true; nextClick = 1 }
    LaunchedEffect(visible) { if (visible) { delay(if (level <= 3) 3000L else if (level <= 6) 2500L else 2000L); visible = false } }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u9ed1\u7329\u7329\u6d4b\u8bd5", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u8bb0\u4f4f\u6570\u5b57\u4f4d\u7f6e\uff0c\u9690\u85cf\u540e\u6309\u987a\u5e8f\u70b9\u51fb", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Text("\u7b49\u7ea7: $level", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        if (bestLvl.value > 0) Text("\u5386\u53f2\u6700\u4f73\u7b49\u7ea7: ${bestLvl.value}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
        Spacer(Modifier.weight(1f))
        when (state) {
            ToolScreenState.Idle -> { Text("\u9ed1\u7329\u7329\u53ef\u4ee5\u8bb0\u4f4f9\u4e2a\u6570\u5b57\u7684\u4f4d\u7f6e\n\u4f60\u80fd\u505a\u5230\u7b2c\u51e0\u7ea7\uff1f", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(Modifier.height(16.dp)); Button(onClick = { state = ToolScreenState.Playing; level = 1; nextLevel() }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) } }
            ToolScreenState.Playing -> { BoxWithConstraints(Modifier.aspectRatio(1.3f).clip(RoundedCornerShape(12.dp)).background(if (errorFlash) AppColors.dangerMuted else MaterialTheme.colorScheme.surfaceVariant)) { val w = this@BoxWithConstraints.maxWidth; val h = this@BoxWithConstraints.maxHeight; positions.forEachIndexed { i, pos -> Box(Modifier.fillMaxSize()) { if (visible) Box(Modifier.offset(x = (pos.x * w.value).dp, y = (pos.y * h.value).dp).size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) { Text("${i+1}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp) } else Box(Modifier.offset(x = (pos.x * w.value).dp, y = (pos.y * h.value).dp).size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant).clickable { if (i + 1 == nextClick) { nextClick++; if (nextClick > positions.size) { if (level > bestLvl.value) bestLvl.value = level; nextLevel() } } else { errorFlash = true; state = ToolScreenState.Result } }.then(sem("interact-area")), contentAlignment = Alignment.Center) { Text(if (i + 1 >= nextClick) "?" else "${i+1}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium) } } } }; Text("\u70b9\u51fb\u6570\u5b57\u4f4d\u7f6e: $nextClick", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp)) }
            ToolScreenState.Result -> { Text("\u8fbe\u5230\u7b49\u7ea7 ${bestLvl.value}", fontSize = 56.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary); Text("\u8d85\u8d8a\u4e86\u9ed1\u7329\u7329\u7684\u5de5\u4f5c\u8bb0\u5fc6", style = MaterialTheme.typography.bodyMedium); Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Text("\u518d\u6765\u4e00\u6b21") }
                Spacer(Modifier.height(8.dp))
                val enc = remember { randomEncouragement() }
                Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("黑猩猩测试", "等级 ${bestLvl.value}", enc); shareResult(context, bitmap, "黑猩猩测试", "等级 ${bestLvl.value}") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}


// ═══ Choice Reaction ═══
@Composable fun ChoiceReactionScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var score by remember { mutableIntStateOf(0) }; var miss by remember { mutableIntStateOf(0) }; var round by remember { mutableIntStateOf(0) }; var targetIdx by remember { mutableIntStateOf(0) }; val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current; var clicked by remember { mutableStateOf(false) }; val total = 15; val reactionColors = listOf(AppColors.danger, AppColors.accent, AppColors.success, AppColors.catKnowledge)
    fun next() { targetIdx = Random.nextInt(4); clicked = false }
    LaunchedEffect(state, round) { if (state == ToolScreenState.Playing && !clicked) { delay(2000L); if (!clicked) { miss++; round++; if (round >= total) { if (score > best.value) best.value = score; state = ToolScreenState.Result } else next() } } }
    Column(Modifier.fillMaxSize().padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(28.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u9009\u62e9\u53cd\u5e94\u6d4b\u8bd5", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u70b9\u51fb\u4e0e\u4e0a\u65b9\u8272\u5757\u5339\u914d\u7684\u989c\u8272", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Text("$round / $total | \u51c6\u786e\u7387 ${if (round > 0) score * 100 / round else 0}%", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.weight(1f))
        when (state) {
            ToolScreenState.Idle -> { Button(onClick = { state = ToolScreenState.Playing; score = 0; miss = 0; round = 0; next() }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) } }
            ToolScreenState.Playing -> { Box(Modifier.size(100.dp).clip(CircleShape).background(reactionColors[targetIdx])); Spacer(Modifier.height(20.dp)); Text("\u70b9\u51fb\u4e0b\u65b9\u5339\u914d\u7684\u989c\u8272", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(Modifier.height(12.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { reactionColors.forEachIndexed { i, col -> Box(Modifier.size(56.dp).clip(CircleShape).background(col).clickable { if (!clicked) { clicked = true; if (i == targetIdx) score++; round++; if (round >= total) { if (score > best.value) best.value = score; state = ToolScreenState.Result } else next() } }.then(sem("quiz-option-$i"))) } } }
            ToolScreenState.Result -> { Spacer(Modifier.height(24.dp)); ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) { Text("\u9009\u62e9\u53cd\u5e94\u6d4b\u8bd5", style = MaterialTheme.typography.titleMedium); if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value} / $total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary); Spacer(Modifier.height(8.dp)); Text("$score / $total", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) } }; Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                Spacer(Modifier.height(8.dp))
                val enc = remember { randomEncouragement() }
                Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("选择反应", "$score", enc); shareResult(context, bitmap, "选择反应", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}


// ═══ Aim Trainer ═══
@Composable fun AimTrainerScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var hits by remember { mutableIntStateOf(0) }; var misses by remember { mutableIntStateOf(0) }; var timer by remember { mutableIntStateOf(10) }; val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current; var tx by remember { mutableFloatStateOf(100f) }; var ty by remember { mutableFloatStateOf(150f) }; var size by remember { mutableFloatStateOf(36f) }
    LaunchedEffect(state) { if (state == ToolScreenState.Playing) { val start = System.currentTimeMillis(); while (System.currentTimeMillis() - start < 10_000) { delay(100L); timer = ((10_000 - (System.currentTimeMillis() - start)) / 1000).toInt().coerceAtLeast(0) }; state = ToolScreenState.Result } }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u7784\u51c6\u8bad\u7ec3", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u5feb\u901f\u70b9\u51fb\u51fa\u73b0\u7684\u76ee\u6807", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { Text("\u547d\u4e2d: $hits", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold); Text("\u672a\u4e2d: $misses", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold); Text("${timer}s", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Spacer(Modifier.height(8.dp))
        when (state) {
            ToolScreenState.Idle -> { Spacer(Modifier.weight(1f)); Button(onClick = { state = ToolScreenState.Playing; hits = 0; misses = 0; timer = 10; tx = Random.nextFloat() * 200f + 20f; ty = Random.nextFloat() * 300f + 20f }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }; Spacer(Modifier.weight(1f)) }
            ToolScreenState.Playing -> { BoxWithConstraints(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp)).clip(RoundedCornerShape(12.dp)).then(sem("interact-area")).pointerInput(Unit) { detectTapGestures { misses++ } }) { val w = this@BoxWithConstraints.maxWidth; val h = this@BoxWithConstraints.maxHeight; Box(Modifier.offset(x = ((tx - 40f).coerceIn(0f, w.value - 80f)).dp, y = ((ty - 40f).coerceIn(0f, h.value - 80f)).dp).size(48.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary).clickable { hits++; tx = Random.nextFloat() * w.value * 0.8f + 20f; ty = Random.nextFloat() * h.value * 0.8f + 20f }.then(sem("interact-area"))) } }
            ToolScreenState.Result -> { Spacer(Modifier.height(24.dp)); ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) { Text("\u7784\u51c6\u8bad\u7ec3\u7ed3\u679c", style = MaterialTheme.typography.titleMedium); Spacer(Modifier.height(8.dp)); Text("$hits \u547d\u4e2d", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary); Text("\u6f0f\u6389 $misses", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) } }; Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                Spacer(Modifier.height(8.dp))
                val enc = remember { randomEncouragement() }
                Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("瞄准训练", "$hits 命中", enc); shareResult(context, bitmap, "瞄准训练", "$hits 命中") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
    }
}


// ═══ Focus Training ═══
@Composable fun FocusTrainingScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var score by remember { mutableIntStateOf(0) }; val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current; var timeLeft by remember { mutableIntStateOf(15) }; var tx by remember { mutableFloatStateOf(100f) }; var ty by remember { mutableFloatStateOf(150f) }; var onTarget by remember { mutableStateOf(false) }; var targetStartTime by remember { mutableLongStateOf(0L) }
    // Track time on target independently to avoid frame-rate dependency
    LaunchedEffect(onTarget, state) {
        if (state == ToolScreenState.Playing) {
            if (onTarget && targetStartTime == 0L) {
                targetStartTime = System.currentTimeMillis()
            } else if (!onTarget && targetStartTime > 0L) {
                val elapsed = System.currentTimeMillis() - targetStartTime
                score += (elapsed / 16).toInt().coerceAtLeast(1) // ~60 points per second
                targetStartTime = 0L
            }
        }
    }
    LaunchedEffect(state) { if (state == ToolScreenState.Playing) { val start = System.currentTimeMillis(); while (System.currentTimeMillis() - start < 15_000) { delay(100L); timeLeft = ((15_000 - (System.currentTimeMillis() - start)) / 1000).toInt().coerceAtLeast(0) }; state = ToolScreenState.Result } }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u4e13\u6ce8\u529b\u8bad\u7ec3", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u6309\u4f4f\u79fb\u52a8\u76ee\u6807\u4e0d\u653e", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Text("${timeLeft}s | \u5f97\u5206: $score", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(12.dp))
        when (state) {
            ToolScreenState.Idle -> { Spacer(Modifier.weight(1f)); Button(onClick = { state = ToolScreenState.Playing; score = 0; timeLeft = 15; tx = 150f; ty = 200f }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }; Spacer(Modifier.weight(1f)) }
            ToolScreenState.Playing -> { LaunchedEffect(Unit) { while (true) { delay(100L); tx = (tx + (Random.nextFloat() - 0.5f) * 10f).coerceIn(20f, 280f); ty = (ty + (Random.nextFloat() - 0.5f) * 10f).coerceIn(20f, 380f) } }; BoxWithConstraints(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).then(sem("interact-area"))) { Box(Modifier.offset(x = (tx - 24f).dp, y = (ty - 24f).dp).size(48.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary).pointerInput(Unit) { detectTapGestures(onPress = { onTarget = true; tryAwaitRelease(); onTarget = false }) }) } }
            ToolScreenState.Result -> { Spacer(Modifier.height(24.dp)); ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) { Text("\u4e13\u6ce8\u5f97\u5206", style = MaterialTheme.typography.titleMedium); if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary); Spacer(Modifier.height(8.dp)); Text("$score", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) } }; Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                Spacer(Modifier.height(8.dp))
                val enc = remember { randomEncouragement() }
                Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("专注力训练", "$score", enc); shareResult(context, bitmap, "专注力训练", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
    }
}


// ═══ Inhibition Test ═══
@Composable fun InhibitionTestScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var score by remember { mutableIntStateOf(0) }; val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current; var miss by remember { mutableIntStateOf(0) }; var round by remember { mutableIntStateOf(0) }; var isGo by remember { mutableStateOf(false) }; var showTs by remember { mutableLongStateOf(0L) }; val total = 20; val scope = rememberCoroutineScope(); var roundJob by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }
    fun next() { isGo = Random.nextBoolean(); showTs = System.currentTimeMillis(); roundJob?.cancel(); roundJob = scope.launch { delay(800L); if (state == ToolScreenState.Playing && showTs > 0) { miss++; showTs = 0; round++; if (round >= total) { if (score > best.value) best.value = score; state = ToolScreenState.Result } else next() } } }
    Column(Modifier.fillMaxSize().padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(28.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u6291\u5236\u63a7\u5236\u6d4b\u8bd5", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u770b\u5230 \u25cf \u70b9\u51fb\uff0c\u770b\u5230 \u2298 \u4e0d\u70b9", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value} / $total", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.weight(1f))
        when (state) {
            ToolScreenState.Idle -> { Button(onClick = { state = ToolScreenState.Playing; score = 0; miss = 0; round = 0; next() }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) } }
            ToolScreenState.Playing -> { Text(if (isGo) "●" else "⊘", fontSize = 72.sp, fontWeight = FontWeight.ExtraBold, color = if (isGo) AppColors.success else AppColors.danger); Spacer(Modifier.height(24.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { Button(onClick = { if (isGo) score++; round++; if (round >= total) { if (score > best.value) best.value = score; state = ToolScreenState.Result } else next() }, shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f).height(52.dp).padding(end = 8.dp), colors = ButtonDefaults.buttonColors(containerColor = AppColors.success)) { Text("\u70b9\u51fb", fontWeight = FontWeight.Bold, color = Color.White) }; Text("\u6b63\u786e\u70b9\u51fb: $score", style = MaterialTheme.typography.bodyMedium) } }
            ToolScreenState.Result -> { ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) { Text("\u6291\u5236\u63a7\u5236\u6d4b\u8bd5", style = MaterialTheme.typography.titleMedium); Spacer(Modifier.height(8.dp)); Text("$score / $total", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary); Text("\u9519\u8bef\u70b9\u51fb: $miss", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error) } }; Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                Spacer(Modifier.height(8.dp))
                val enc = remember { randomEncouragement() }
                Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("抑制控制", "$score", enc); shareResult(context, bitmap, "抑制控制", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}


// ═══ MultiTasking ═══
@Composable fun MultiTaskingTestScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var score by remember { mutableIntStateOf(0) }; val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current; var round by remember { mutableIntStateOf(0) }; var leftChar by remember { mutableStateOf('A') }; var rightNum by remember { mutableIntStateOf(0) }; var answerPhase by remember { mutableStateOf(false) }; val total = 10
    fun next() { leftChar = ('A'..'Z').random(); rightNum = Random.nextInt(100); answerPhase = false }
    LaunchedEffect(state, round) { if (state == ToolScreenState.Playing && !answerPhase) { delay(1500L); if (!answerPhase) { round++; if (round >= total) { if (score > best.value) best.value = score; state = ToolScreenState.Result } else next() } } }
    Column(Modifier.fillMaxSize().padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(24.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u591a\u4efb\u52a1\u6d4b\u8bd5", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u8bb0\u4f4f\u5b57\u6bcd\u548c\u6570\u5b57\uff0c\u56de\u7b54\u5173\u4e8e\u5b83\u4eec\u7684\u95ee\u9898", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Text("$round / $total | \u6b63\u786e: $score", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.weight(1f))
        when (state) {
            ToolScreenState.Idle -> { DisclaimerBanner(); Button(onClick = { state = ToolScreenState.Playing; score = 0; round = 0; next() }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) } }
            ToolScreenState.Playing -> { if (!answerPhase) { Text("$leftChar | $rightNum", fontSize = 56.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) } else { Text("\u5b57\u6bcd\u662f \u5143\u97f3 \u8fd8\u662f \u8f85\u97f3\uff1f", style = MaterialTheme.typography.titleMedium); Spacer(Modifier.height(16.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { Button(onClick = { val correct = "AEIOU".contains(leftChar); if (correct) score++; round++; if (round >= total) { if (score > best.value) best.value = score; state = ToolScreenState.Result } else next() }, shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f).height(52.dp).padding(end = 8.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5143\u97f3") }; Button(onClick = { val correct = !"AEIOU".contains(leftChar); if (correct) score++; round++; if (round >= total) { if (score > best.value) best.value = score; state = ToolScreenState.Result } else next() }, shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f).height(52.dp).padding(start = 8.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u8f85\u97f3") } } } }
            ToolScreenState.Result -> { Spacer(Modifier.height(24.dp)); ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) { Text("\u591a\u4efb\u52a1\u6d4b\u8bd5", style = MaterialTheme.typography.titleMedium); Spacer(Modifier.height(8.dp)); Text("$score / $total", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) } }; Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                Spacer(Modifier.height(8.dp))
                val enc = remember { randomEncouragement() }
                Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("多任务测试", "$score", enc); shareResult(context, bitmap, "多任务测试", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}


// ═══ Memory Test ═══
@Composable fun MemoryTestScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var score by remember { mutableIntStateOf(0) }; val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current; var level by remember { mutableIntStateOf(1) }; var sequence by remember { mutableStateOf(listOf<Int>()) }; var showing by remember { mutableStateOf(true) }; var step by remember { mutableIntStateOf(0) }; val totalLevels = 10
    fun nextLevel() { level++; sequence = (0 until level + 2).map { Random.nextInt(4) }; showing = true; step = 0 }
    LaunchedEffect(showing) { if (showing && state == ToolScreenState.Playing) { for (s in sequence) { delay(500L); delay(300L) }; showing = false } }
    LaunchedEffect(state) { if (state == ToolScreenState.Playing) { level = 1; showing = false; nextLevel() } }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u8bb0\u5fc6\u529b\u6d4b\u8bd5", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u8bb0\u4f4f\u95ea\u70c1\u987a\u5e8f\uff0c\u7136\u540e\u91cd\u590d", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Text("\u7b49\u7ea7 $level | \u5f97\u5206 $score", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73\u7b49\u7ea7: ${best.value}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
        Spacer(Modifier.weight(1f))
        when (state) {
            ToolScreenState.Idle -> { Button(onClick = { state = ToolScreenState.Playing; score = 0 }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) } }
            ToolScreenState.Playing -> { BoxWithConstraints(Modifier.aspectRatio(1f).sem("interact-area")) { val w = this@BoxWithConstraints.maxWidth; val h = this@BoxWithConstraints.maxHeight; if (showing) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("\u8bb0\u4f4f!", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) } } else { for (i in 0..3) { val x = listOf(0.2f, 0.7f, 0.2f, 0.7f)[i]; val y = listOf(0.2f, 0.2f, 0.7f, 0.7f)[i]; Box(Modifier.offset(x = (x * w.value - 30f).dp, y = (y * h.value - 30f).dp).size(60.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceVariant).clickable { if (step < sequence.size && i == sequence[step]) { step++; if (step >= sequence.size) { score += level * 10; if (score > best.value) best.value = score; nextLevel() } } else { if (score > best.value) best.value = score; state = ToolScreenState.Result } }.then(sem("quiz-option-$i")), contentAlignment = Alignment.Center) { Text("${i+1}", fontSize = 24.sp, fontWeight = FontWeight.Bold) } } } } }
            ToolScreenState.Result -> { Spacer(Modifier.height(24.dp)); ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) { Text("\u8bb0\u5fc6\u529b\u6d4b\u8bd5", style = MaterialTheme.typography.titleMedium); Spacer(Modifier.height(8.dp)); Text("$score \u5206", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) } }; Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                Spacer(Modifier.height(8.dp))
                val enc = remember { randomEncouragement() }
                Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("记忆力测试", "$score", enc); shareResult(context, bitmap, "记忆力测试", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}


