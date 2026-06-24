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
import com.mispec.luomashu.audio.AudioEngine
import com.mispec.luomashu.util.sem
import com.mispec.luomashu.util.ToolScreenState
import com.mispec.luomashu.util.LocalBestScore
import com.mispec.luomashu.util.rememberBestScore
import com.mispec.luomashu.util.performHaptic
import com.mispec.luomashu.util.randomEncouragement
import com.mispec.luomashu.util.createResultBitmap
import com.mispec.luomashu.util.shareResult
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalContext
import com.mispec.luomashu.util.DisclaimerBanner
import com.mispec.luomashu.ui.theme.AppColors
import android.view.HapticFeedbackConstants
import android.view.View
import kotlin.math.abs
import kotlin.random.Random

// ═══ Guess Number ═══
@Composable fun GuessNumberGameScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var secret by remember { mutableIntStateOf(0) }; var guess by remember { mutableStateOf("") }; var attempts by remember { mutableIntStateOf(0) }; var hint by remember { mutableStateOf("") }; val view = LocalView.current; val context = LocalContext.current; val best = rememberBestScore(toolId)
    fun start() { secret = Random.nextInt(100) + 1; attempts = 0; hint = ""; state = ToolScreenState.Playing }
    fun check() { val g = guess.toIntOrNull() ?: return; attempts++; hint = when { g < secret -> "\u592a\u5c0f\u4e86\uff0c\u518d\u5927\u4e00\u70b9 \u25b2"; g > secret -> "\u592a\u5927\u4e86\uff0c\u518d\u5c0f\u4e00\u70b9 \u25bc"; else -> "\u4f60\u731c\u5bf9\u4e86\uff01" }; if (g == secret) state = ToolScreenState.Result }
    Column(Modifier.fillMaxSize().padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(28.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u731c\u6570\u5b57", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u6211\u5fc3\u91cc\u6709\u4e2a 1-100 \u7684\u6570\uff0c\u731c\u731c\u770b", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Spacer(Modifier.weight(1f))
        when (state) {
            ToolScreenState.Idle -> { DisclaimerBanner(); Button(onClick = { start() }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6e38\u620f", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) } }
            ToolScreenState.Playing -> { Text("\u7b2c ${attempts+1} \u6b21", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary); Spacer(Modifier.height(16.dp)); OutlinedTextField(value = guess, onValueChange = { guess = it }, label = { Text("\u8f93\u5165 1-100") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth()); Spacer(Modifier.height(12.dp)); Text(hint, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface); Spacer(Modifier.height(16.dp)); Button(onClick = { check() }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u731c\u4e00\u4e0b") } }
            ToolScreenState.Result -> { Text("\u7b54\u6848\u662f $secret", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Spacer(Modifier.height(8.dp)); Text("${attempts} \u6b21\u731c\u4e2d", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); guess = ""; start() }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Text("\u518d\u6765\u4e00\u6b21") }
                    Spacer(Modifier.height(8.dp))
                    val enc = remember { randomEncouragement() }
                    Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("猜数字", "$attempts", enc); shareResult(context, bitmap, "猜数字", "$attempts") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "分享结果", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("分享成绩", fontSize = 13.sp) }
                }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}


// ═══ Chase Button ═══
@Composable fun ChaseButtonGameScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var score by remember { mutableIntStateOf(0) }; var timeLeft by remember { mutableIntStateOf(10) }; var bx by remember { mutableFloatStateOf(100f) }; var by by remember { mutableFloatStateOf(100f) }; val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current
    LaunchedEffect(state) { if (state == ToolScreenState.Playing) { val start = System.currentTimeMillis(); while (System.currentTimeMillis() - start < 10_000) { delay(16L); timeLeft = ((10_000 - (System.currentTimeMillis() - start)) / 1000).toInt().coerceAtLeast(0) }; if (score > best.value) best.value = score; state = ToolScreenState.Result } }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u8ffd\u6309\u94ae", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u8ffd\u7740\u79fb\u52a8\u7684\u6309\u94ae\u70b9\u51fb", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Text("${timeLeft}s | \u547d\u4e2d: $score", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.weight(1f))
        when (state) {
            ToolScreenState.Idle -> { DisclaimerBanner(); Button(onClick = { state = ToolScreenState.Playing; score = 0; timeLeft = 10; bx = Random.nextFloat() * 200f + 40f; by = Random.nextFloat() * 300f + 100f }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6e38\u620f", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) } }
            ToolScreenState.Playing -> { BoxWithConstraints(Modifier.fillMaxSize().sem("interact-area")) { val w = this@BoxWithConstraints.maxWidth; val h = this@BoxWithConstraints.maxHeight; Button(onClick = { score++; bx = Random.nextFloat() * (w.value - 80f) + 20f; by = Random.nextFloat() * (h.value - 80f) + 20f }, modifier = Modifier.offset(x = (bx.coerceIn(0f, w.value - 80f)).dp, y = (by.coerceIn(0f, h.value - 80f)).dp).then(sem("interact-area")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u70b9\u6211!", fontWeight = FontWeight.Bold) } } }
            ToolScreenState.Result -> { ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) { Text("\u8ffd\u6309\u94ae", style = MaterialTheme.typography.titleMedium); Spacer(Modifier.height(8.dp)); Text("$score \u6b21", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) } }; Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                    Spacer(Modifier.height(8.dp))
                    val enc = remember { randomEncouragement() }
                    Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("追按钮", "$score", enc); shareResult(context, bitmap, "追按钮", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "分享结果", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("分享成绩", fontSize = 13.sp) }
                }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}


// ═══ Minesweeper ═══
@Composable fun MinesweeperGameScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var board by remember { mutableStateOf(listOf<Int>()) }; val revealed = remember { mutableStateListOf<Int>() }; var gameOver by remember { mutableStateOf(false) }; var score by remember { mutableIntStateOf(0) }
    val gridSize = 25; var mines by remember { mutableIntStateOf(5) }; val view = LocalView.current; val context = LocalContext.current; val best = rememberBestScore(toolId)
    fun start() { val all = (0 until gridSize).toMutableList(); all.shuffle(); board = List(gridSize) { if (it in all.take(mines)) -1 else 0 }; revealed.clear(); gameOver = false; score = 0; state = ToolScreenState.Playing }
    fun reveal(idx: Int) { if (gameOver || idx in revealed) return; revealed.add(idx); if (board[idx] == -1) { gameOver = true } else { score++; if (revealed.size >= gridSize - mines) { if (score % 3 == 0 && mines < 12) mines++; state = ToolScreenState.Result } } }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u626b\u96f7", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("5×5 \u7f51\u683c\uff0c\u907f\u5f00 5 \u9897\u5730\u96f7", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Text("\u5f97\u5206: $score", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(8.dp))
        when (state) {
            ToolScreenState.Idle -> { Spacer(Modifier.weight(1f)); Button(onClick = { start() }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6e38\u620f", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }; Spacer(Modifier.weight(1f)) }
            ToolScreenState.Playing, ToolScreenState.Result -> { Column(Modifier.aspectRatio(1f).padding(4.dp)) { for (row in 0 until 5) { Row(Modifier.fillMaxWidth().weight(1f)) { for (col in 0 until 5) { val idx = row * 5 + col; val isRevealed = idx in revealed; Surface(modifier = Modifier.weight(1f).fillMaxHeight().padding(2.dp).clip(RoundedCornerShape(6.dp)).clickable(enabled = !gameOver) { reveal(idx); if (gameOver || revealed.size >= gridSize - mines) state = ToolScreenState.Result }, color = if (isRevealed) (if (board[idx] == -1) AppColors.danger else AppColors.success) else MaterialTheme.colorScheme.surfaceVariant) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { if (isRevealed) Text(if (board[idx] == -1) "💣" else "\u2714", fontSize = 20.sp) } } } } } }; Spacer(Modifier.height(12.dp)); if (gameOver) { Text("\u8e29\u5230\u5730\u96f7\uff01", color = AppColors.danger, fontWeight = FontWeight.Bold); Spacer(Modifier.height(8.dp)); FilledTonalButton(onClick = { start() }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Text("\u518d\u6765\u4e00\u6b21") }
                    Spacer(Modifier.height(8.dp))
                    val enc = remember { randomEncouragement() }
                    Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("扫雷", "$score", enc); shareResult(context, bitmap, "扫雷", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "分享结果", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("分享成绩", fontSize = 13.sp) }
                } else if (state == ToolScreenState.Result) { Text("\u5b89\u5168\u5f97\u5206: $score", fontWeight = FontWeight.Bold); Spacer(Modifier.height(8.dp)); FilledTonalButton(onClick = { start() }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Text("\u518d\u6765\u4e00\u6b21") }
                    Spacer(Modifier.height(8.dp))
                    val enc = remember { randomEncouragement() }
                    Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("扫雷", "$score", enc); shareResult(context, bitmap, "扫雷", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "分享结果", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("分享成绩", fontSize = 13.sp) }
                }
            }
            else -> {}
        }
    }
}


// ═══ Don't Press ═══
@Composable fun DontPressGameScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var score by remember { mutableIntStateOf(0) }; val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current; var timeLeft by remember { mutableIntStateOf(15) }; var isGreen by remember { mutableStateOf(false) }; var errors by remember { mutableIntStateOf(0) }
    LaunchedEffect(state) { if (state == ToolScreenState.Playing) { val start = System.currentTimeMillis(); while (System.currentTimeMillis() - start < 15_000) { delay(16L); timeLeft = ((15_000 - (System.currentTimeMillis() - start)) / 1000).toInt().coerceAtLeast(0) }; if (score > best.value) best.value = score; state = ToolScreenState.Result } }
    LaunchedEffect(isGreen, state) { if (isGreen && state == ToolScreenState.Playing) { delay(if (score < 5) 2000L else if (score < 10) 1500L else 1000L); if (state == ToolScreenState.Playing) isGreen = false } }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u522b\u6309\u5b83", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u7eff\u8272\u624d\u70b9\uff0c\u7ea2\u8272\u5343\u4e07\u522b\u70b9\uff01", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.weight(1f))
        when (state) {
            ToolScreenState.Idle -> { Button(onClick = { state = ToolScreenState.Playing; score = 0; errors = 0; timeLeft = 15; isGreen = Random.nextBoolean() }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6e38\u620f", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) } }
            ToolScreenState.Playing -> { Text("${timeLeft}s | \u9519\u8bef: $errors", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary); Spacer(Modifier.height(16.dp)); Box(Modifier.size(180.dp).clip(CircleShape).background(if (isGreen) AppColors.success else AppColors.danger).clickable { if (isGreen) { score++; isGreen = false } else { errors++; state = ToolScreenState.Result } }.then(sem("interact-area")), contentAlignment = Alignment.Center) { Text(if (isGreen) "\u70b9\u51fb!" else "\u4e0d\u8981\u70b9\uff01", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) } }
            ToolScreenState.Result -> { Spacer(Modifier.height(24.dp)); ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) { Text("\u6d4b\u8bd5\u5b8c\u6210", style = MaterialTheme.typography.titleMedium); Spacer(Modifier.height(8.dp)); Text("$score \u5206", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) } }; Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                    Spacer(Modifier.height(8.dp))
                    val enc = remember { randomEncouragement() }
                    Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("别按它", "$score", enc); shareResult(context, bitmap, "别按它", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "分享结果", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("分享成绩", fontSize = 13.sp) }
                }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}


// ═══ Circle Challenge ═══
@Composable fun CircleChallengeGameScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var score by remember { mutableIntStateOf(0) }; val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current; var round by remember { mutableIntStateOf(0) }; var cx by remember { mutableFloatStateOf(150f) }; var cy by remember { mutableFloatStateOf(200f) }; var radius by remember { mutableFloatStateOf(12f) }; val total = 10; var accuracy by remember { mutableStateOf(0f) }
    var areaWidth by remember { mutableFloatStateOf(300f) }
    var areaHeight by remember { mutableFloatStateOf(400f) }
    val primaryColor = MaterialTheme.colorScheme.primary
    fun next() { cx = Random.nextFloat() * (areaWidth - 60f) + 30f; cy = Random.nextFloat() * (areaHeight - 60f) + 30f; radius = Random.nextFloat() * 16f + 8f }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u5706\u5708\u6311\u6218", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u70b9\u51fb\u6bcf\u4e2a\u5706\u5708\u7684\u6b63\u4e2d\u5fc3", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Text("$round / $total | \u547d\u4e2d $score", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.weight(1f))
        when (state) {
            ToolScreenState.Idle -> { Button(onClick = { state = ToolScreenState.Playing; score = 0; round = 0 }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6e38\u620f", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) } }
            ToolScreenState.Playing -> { BoxWithConstraints(Modifier.fillMaxSize().pointerInput(Unit) { detectTapGestures { offset -> val dx = offset.x - cx * density; val dy = offset.y - cy * density; val dist = kotlin.math.sqrt(dx * dx + dy * dy); val withinRadius = dist / density < radius * 1.2f; if (withinRadius) score++; round++; if (round >= total) { if (score > best.value) best.value = score; state = ToolScreenState.Result } else next() } }.sem("interact-area")) { val w = this@BoxWithConstraints.maxWidth; val h = this@BoxWithConstraints.maxHeight; LaunchedEffect(w, h) { areaWidth = w.value; areaHeight = h.value; if (round == 0) next() }; Canvas(Modifier.fillMaxSize()) { drawCircle(primaryColor, radius * density, Offset(cx * density, cy * density)) } } }
            ToolScreenState.Result -> { Spacer(Modifier.height(24.dp)); ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) { Text("\u5706\u5708\u6311\u6218", style = MaterialTheme.typography.titleMedium); Spacer(Modifier.height(8.dp)); Text("$score / $total", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary); Text("\u7cbe\u5ea6: ${"%.0f".format(score * 100f / total)}%", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) } }; Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                    Spacer(Modifier.height(8.dp))
                    val enc = remember { randomEncouragement() }
                    Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("圆圈挑战", "$score", enc); shareResult(context, bitmap, "圆圈挑战", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "分享结果", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("分享成绩", fontSize = 13.sp) }
                }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}


