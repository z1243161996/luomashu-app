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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalContext
import com.mispec.luomashu.util.DisclaimerBanner
import com.mispec.luomashu.ui.theme.AppColors
import android.view.HapticFeedbackConstants
import android.view.View
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


// ═══ Hearing Test ═══
@Composable fun HearingTestScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var score by remember { mutableIntStateOf(0) }; val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current; var round by remember { mutableIntStateOf(0) }; var beepStartTime by remember { mutableLongStateOf(0L) }; var missed by remember { mutableIntStateOf(0) }; val total = 10; val scope = rememberCoroutineScope(); val roundJobHolder = remember { object { var job: kotlinx.coroutines.Job? = null } }
    fun nextRound() { if (round < total) { round++; roundJobHolder.job?.cancel(); roundJobHolder.job = scope.launch { delay(Random.nextLong(500L, 2500L)); beepStartTime = System.currentTimeMillis(); AudioEngine.playTone(1000.0, 300); delay(1500L); if (beepStartTime > 0) { missed++; beepStartTime = 0 } } } }
    LaunchedEffect(state) { if (state == ToolScreenState.Playing) { round = 0; nextRound() } else { roundJobHolder.job?.cancel() } }
    Column(Modifier.fillMaxSize().padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(28.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u542c\u529b\u6d4b\u8bd5", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u542c\u5230\u63d0\u793a\u540e\u7acb\u5373\u70b9\u51fb\u5c4f\u5e55", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Text("$round / $total | \u6b63\u786e: $score | \u9057\u6f0f: $missed", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.weight(1f))
        when (state) {
            ToolScreenState.Idle -> { DisclaimerBanner(); Button(onClick = { state = ToolScreenState.Playing; score = 0; missed = 0 }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) } }
            ToolScreenState.Playing -> { Box(Modifier.fillMaxWidth().weight(1f).clip(RoundedCornerShape(12.dp)).background(if (beepStartTime > 0) AppColors.successMuted else MaterialTheme.colorScheme.surfaceVariant).clickable { if (beepStartTime > 0) { score++; beepStartTime = 0 }; if (round >= total) { if (score > best.value) best.value = score; state = ToolScreenState.Result } else nextRound() }.then(sem("interact-area")), contentAlignment = Alignment.Center) { Text(if (beepStartTime > 0) "\u542c\u5230\u4e86\uff01" else "\u5b89\u9759...", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) } }
            ToolScreenState.Result -> { Spacer(Modifier.height(24.dp)); ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) { Text("\u542c\u529b\u6d4b\u8bd5\u5b8c\u6210", style = MaterialTheme.typography.titleMedium); Spacer(Modifier.height(8.dp)); Text("$score / $total", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary); Text("\u9057\u6f0f $missed", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) } }; Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                    Spacer(Modifier.height(8.dp))
                    val enc = remember { randomEncouragement() }
                    Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("听力测试", "$score", enc); shareResult(context, bitmap, "听力测试", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun PitchTestScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }
    var score by remember { mutableIntStateOf(0) }
    var round by remember { mutableIntStateOf(0) }
    var highFirst by remember { mutableStateOf(false) }
    val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current
    val total = 10
    val scope = rememberCoroutineScope()

    fun nextRound() {
        highFirst = Random.nextBoolean()
        scope.launch {
            AudioEngine.playPitchPair(lowFirst = highFirst)
        }
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(28.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) {
            Text("\u97f3\u9ad8\u8fa8\u522b\u6d4b\u8bd5", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("\u542c\u4e24\u4e2a\u97f3, \u70b9\u51fb\u54ea\u4e2a\u97f3\u8c03\u66f4\u9ad8", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text("$round / $total | \u6b63\u786e: $score", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value} / $total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
        Spacer(Modifier.weight(1f))

        when (state) {
            ToolScreenState.Idle -> {
                Text("\u672c\u6d4b\u8bd5\u4ec5\u4f9b\u5a31\u4e50\u53c2\u8003", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                Spacer(Modifier.height(16.dp))
                Button(onClick = { state = ToolScreenState.Playing; score = 0; round = 0; nextRound() },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }
            }
            ToolScreenState.Playing -> {
                Text("\u7b2c $round \u8f6e", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
                Text("\u54ea\u4e2a\u97f3\u8c03\u66f4\u9ad8?", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = {
                            val correct = highFirst
                            if (correct) score++
                            round++
                            if (round >= total) {
                                if (score > best.value) best.value = score
                                state = ToolScreenState.Result
                            } else nextRound()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(64.dp).padding(end = 8.dp).sem("quiz-option-0"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("\u7b2c\u4e00\u4e2a", fontWeight = FontWeight.Bold, color = Color.White) }
                    Button(onClick = {
                            val correct = !highFirst
                            if (correct) score++
                            round++
                            if (round >= total) {
                                if (score > best.value) best.value = score
                                state = ToolScreenState.Result
                            } else nextRound()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(64.dp).padding(start = 8.dp).sem("quiz-option-1"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("\u7b2c\u4e8c\u4e2a", fontWeight = FontWeight.Bold, color = Color.White) }
                }
                Spacer(Modifier.height(16.dp))
                Text("\u70b9\u51fb\u4e0b\u65b9\u6309\u94ae\u64ad\u653e", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                FilledTonalButton(onClick = { performHaptic(view); nextRound() },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp).sem("btn-secondary", "再来一次按钮")
                ) { Icon(Icons.Filled.PlayArrow, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u91cd\u65b0\u64ad\u653e") }
            }
            ToolScreenState.Result -> {
                ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(28.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("\u97f3\u9ad8\u8fa8\u522b\u6d4b\u8bd5", style = MaterialTheme.typography.titleMedium)
                        if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value} / $total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                        Spacer(Modifier.height(8.dp))
                        Text("$score / $total", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        val grade = when { score >= 9 -> "\u542c\u529b\u654f\u9510!"; score >= 7 -> "\u826f\u597d"; score >= 5 -> "\u4e00\u822c"; else -> "\u7ee7\u7eed\u52a0\u6cb9" }
                        Spacer(Modifier.height(4.dp))
                        Text(grade, style = MaterialTheme.typography.titleSmall)
                    }
                }
                Spacer(Modifier.height(20.dp))
                FilledTonalButton(onClick = { state = ToolScreenState.Idle },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")
                ) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                    Spacer(Modifier.height(8.dp))
                    val enc = remember { randomEncouragement() }
                    Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("音高辨别", "$score", enc); shareResult(context, bitmap, "音高辨别", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun RhythmTestScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }
    var score by remember { mutableIntStateOf(0) }
    var round by remember { mutableIntStateOf(0) }
    var taps by remember { mutableIntStateOf(0) }
    var patternLength by remember { mutableIntStateOf(3) }
    val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current
    val total = 8
    val scope = rememberCoroutineScope()
    var isPlaying by remember { mutableStateOf(false) }

    fun nextRound() {
        patternLength = 3 + round / 2
        taps = 0
        isPlaying = true
        scope.launch {
            val steps = (0 until patternLength).toList()
            AudioEngine.playRhythmPattern(steps)
            isPlaying = false
        }
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(28.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) {
            Text("\u8282\u594f\u611f\u77e5\u6d4b\u8bd5", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("\u542c\u8282\u594f, \u7136\u540e\u70b9\u51fb\u76f8\u540c\u6b21\u6570", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text("$round / $total | \u6b63\u786e: $score", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value} / $total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
        Spacer(Modifier.weight(1f))

        when (state) {
            ToolScreenState.Idle -> {
                Text("\u672c\u6d4b\u8bd5\u4ec5\u4f9b\u5a31\u4e50\u53c2\u8003", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                Spacer(Modifier.height(16.dp))
                Button(onClick = { state = ToolScreenState.Playing; score = 0; round = 0; nextRound() },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }
            }
            ToolScreenState.Playing -> {
                if (isPlaying) {
                    Text("\u8046\u542c\u8282\u594f\u4e2d...", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)))
                } else {
                    Text("\u7b2c $round \u8f6e", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(8.dp))
                    Text("\u4f60\u542c\u5230\u4e86 $patternLength \u4e2a\u8282\u62cd, \u70b9\u51fb\u76f8\u540c\u6b21\u6570", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Text("\u5df2\u70b9\u51fb: $taps / $patternLength", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(24.dp))
                    Box(Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable {
                            if (taps < patternLength) {
                                taps++
                                if (taps >= patternLength) {
                                    if (taps == patternLength) score++
                                    round++
                                    if (round >= total) {
                                        if (score > best.value) best.value = score
                                        state = ToolScreenState.Result
                                    } else {
                                        nextRound()
                                    }
                                }
                            }
                        }.sem("interact-area"),
                        contentAlignment = Alignment.Center
                    ) { Text("\u70b9\u51fb!", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimaryContainer) }
                    Spacer(Modifier.height(16.dp))
                    FilledTonalButton(onClick = { nextRound() },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp).sem("btn-secondary", "再来一次按钮")
                    ) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u91cd\u542c\u8282\u594f") }
                }
            }
            ToolScreenState.Result -> {
                ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(28.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("\u8282\u594f\u611f\u77e5\u6d4b\u8bd5", style = MaterialTheme.typography.titleMedium)
                        if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value} / $total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                        Spacer(Modifier.height(8.dp))
                        Text("$score / $total", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        val grade = when { score >= 7 -> "\u8282\u594f\u611f\u6781\u4f73!"; score >= 5 -> "\u8282\u594f\u611f\u826f\u597d"; score >= 3 -> "\u8fd8\u9700\u7ec3\u4e60"; else -> "\u7ee7\u7eed\u52a0\u6cb9" }
                        Spacer(Modifier.height(4.dp))
                        Text(grade, style = MaterialTheme.typography.titleSmall)
                    }
                }
                Spacer(Modifier.height(20.dp))
                FilledTonalButton(onClick = { state = ToolScreenState.Idle },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")
                ) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                    Spacer(Modifier.height(8.dp))
                    val enc = remember { randomEncouragement() }
                    Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("节奏感测试", "$score", enc); shareResult(context, bitmap, "节奏感测试", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun AngleTestScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }
    var score by remember { mutableIntStateOf(0) }
    var round by remember { mutableIntStateOf(0) }
    var targetAngle by remember { mutableFloatStateOf(0f) }
    var options by remember { mutableStateOf(listOf<Float>()) }
    val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current
    val total = 10
    val primaryColor = MaterialTheme.colorScheme.primary

    fun nextRound() {
        targetAngle = Random.nextFloat() * 150f + 15f
        val distractors = mutableSetOf<Float>()
        while (distractors.size < 3) {
            val d = targetAngle + Random.nextFloat() * 60f - 30f
            if (d > 0f && d < 180f && abs(d - targetAngle) > 8f) distractors.add(d)
        }
        options = (distractors + targetAngle).shuffled()
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) {
            Text("\u89d2\u5ea6\u8fa8\u522b\u6d4b\u8bd5", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("\u89c2\u5bdf\u7ebf\u6761\u89d2\u5ea6, \u4ece\u9009\u9879\u4e2d\u9009\u51fa\u6b63\u786e\u7684\u89d2\u5ea6", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text("$round / $total | \u6b63\u786e: $score", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value} / $total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
        Spacer(Modifier.weight(1f))

        when (state) {
            ToolScreenState.Idle -> {
                Text("\u672c\u6d4b\u8bd5\u4ec5\u4f9b\u5a31\u4e50\u53c2\u8003", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                Spacer(Modifier.height(16.dp))
                Button(onClick = { state = ToolScreenState.Playing; score = 0; round = 0; nextRound() },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }
            }
            ToolScreenState.Playing -> {
                Text("\u8fd9\u4e2a\u89d2\u5ea6\u662f\u591a\u5c11?", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                Canvas(Modifier.size(180.dp).sem("interact-area")) {
                    val cx = size.width / 2
                    val cy = size.height / 2
                    val radius = size.minDimension * 0.38f
                    drawLine(Color.LightGray, Offset(cx, cy), Offset(cx + radius, cy), strokeWidth = 2f)
                    val rad = Math.toRadians(targetAngle.toDouble()).toFloat()
                    drawLine(primaryColor, Offset(cx, cy),
                        Offset(cx + radius * cos(rad), cy - radius * sin(rad)),
                        strokeWidth = 4f)
                }
                Spacer(Modifier.height(20.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    options.take(2).forEachIndexed { i, angle ->
                        Button(onClick = {
                                if (abs(angle - targetAngle) < 0.5f) score++
                                round++
                                if (round >= total) {
                                    if (score > best.value) best.value = score
                                    state = ToolScreenState.Result
                                } else nextRound()
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(48.dp).padding(horizontal = 4.dp).sem("quiz-option-$i")
                        ) { Text("%.0f\u00b0".format(angle), fontSize = 16.sp) }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    options.drop(2).forEachIndexed { i, angle ->
                        Button(onClick = {
                                if (abs(angle - targetAngle) < 0.5f) score++
                                round++
                                if (round >= total) {
                                    if (score > best.value) best.value = score
                                    state = ToolScreenState.Result
                                } else nextRound()
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(48.dp).padding(horizontal = 4.dp).sem("quiz-option-${i + 2}")
                        ) { Text("%.0f\u00b0".format(angle), fontSize = 16.sp) }
                    }
                }
            }
            ToolScreenState.Result -> {
                ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(28.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("\u89d2\u5ea6\u8fa8\u522b\u6d4b\u8bd5", style = MaterialTheme.typography.titleMedium)
                        if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value} / $total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                        Spacer(Modifier.height(8.dp))
                        Text("$score / $total", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        val grade = when { score >= 9 -> "\u89d2\u5ea6\u5929\u624d!"; score >= 7 -> "\u89c2\u5bdf\u529b\u5f3a"; score >= 5 -> "\u4e00\u822c\u6c34\u5e73"; else -> "\u7ee7\u7eed\u7ec3\u4e60" }
                        Spacer(Modifier.height(4.dp))
                        Text(grade, style = MaterialTheme.typography.titleSmall)
                    }
                }
                Spacer(Modifier.height(20.dp))
                FilledTonalButton(onClick = { state = ToolScreenState.Idle },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")
                ) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                    Spacer(Modifier.height(8.dp))
                    val enc = remember { randomEncouragement() }
                    Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("角度辨别", "$score", enc); shareResult(context, bitmap, "角度辨别", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}
