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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
import androidx.compose.runtime.rememberCoroutineScope
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


// ═══ Visual Acuity ═══
@Composable fun VisualAcuityScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var score by remember { mutableIntStateOf(0) }; val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current; var round by remember { mutableIntStateOf(0) }; var direction by remember { mutableIntStateOf(0) }; var size by remember { mutableFloatStateOf(0.5f) }; val total = 10; val dirs = listOf("\u4e0a","\u53f3","\u4e0b","\u5de6")
    fun next() { direction = Random.nextInt(4); size = (Random.nextFloat() * 0.4f + 0.1f) }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u89c6\u529b\u6d4b\u8bd5", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u5224\u65ad C \u5b57\u5f00\u53e3\u65b9\u5411\u5e76\u70b9\u51fb\u5bf9\u5e94\u6309\u94ae", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Text("$round / $total | \u6b63\u786e: $score", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.weight(1f)); when (state) {
            ToolScreenState.Idle -> { DisclaimerBanner(); Button(onClick = { state = ToolScreenState.Playing; score = 0; round = 0; next() }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) } }
            ToolScreenState.Playing -> { Canvas(Modifier.size(150.dp).sem("interact-area")) { drawCircle(Color.Black.copy(alpha = 0.1f), 70f); val arcAngle = direction * 90f; drawArc(Color.Black, arcAngle + 30f, 300f, false, style = DrawStroke(6f), topLeft = Offset(15f, 15f), size = CSize(120f, 120f)) }; Spacer(Modifier.height(20.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { dirs.forEachIndexed { i, label -> Box(Modifier.size(56.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant).clickable { if (i == direction) score++; round++; if (round >= total) { if (score > best.value) best.value = score; state = ToolScreenState.Result } else next() }.then(sem("quiz-option-$i")), contentAlignment = Alignment.Center) { Text(label, fontSize = 20.sp, fontWeight = FontWeight.Bold) } } } }
            ToolScreenState.Result -> { Spacer(Modifier.height(24.dp)); ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) { Text("\u89c6\u529b\u6d4b\u8bd5", style = MaterialTheme.typography.titleMedium); Spacer(Modifier.height(8.dp)); Text("$score / $total", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) } }; Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                    Spacer(Modifier.height(8.dp))
                    val enc = remember { randomEncouragement() }
                    Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("视力测试", "$score", enc); shareResult(context, bitmap, "视力测试", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}

// ═══ Color Blind Test ═══
@Composable fun ColorBlindTestScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }; var score by remember { mutableIntStateOf(0) }; val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current; var round by remember { mutableIntStateOf(0) }; var digit by remember { mutableIntStateOf(0) }; val total = 8
    fun next() { digit = listOf(12, 8, 6, 15, 5, 74, 42, 29).random() }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) { Text("\u8272\u89c9\u654f\u611f\u5ea6\u6d4b\u8bd5", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("\u4ec5\u4f9b\u5a31\u4e50\u53c2\u8003\uff0c\u975e\u533b\u5b66\u8bca\u65ad", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline) }
        Text("$round / $total | \u6b63\u786e: $score", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.weight(1f)); when (state) {
            ToolScreenState.Idle -> { DisclaimerBanner(); Button(onClick = { state = ToolScreenState.Playing; score = 0; round = 0; next() }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) } }
            ToolScreenState.Playing -> { val noisePoints = remember { List(201) { Triple(Random.nextFloat() * 360f, Random.nextFloat(), Random.nextFloat() * 5f + 2f) } }; Canvas(Modifier.size(240.dp).sem("interact-area")) { drawCircle(AppColors.success.copy(alpha = 0.4f), size.minDimension * 0.45f); noisePoints.forEach { (angle, distF, radius) -> val dist = distF * size.minDimension * 0.45f; val x = size.width / 2 + kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat() * dist; val y = size.height / 2 + kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat() * dist; drawCircle(Color(0xFF81C784).copy(alpha = 0.6f), radius, Offset(x, y)) } }; Text("$digit", fontSize = 64.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFE91E63)); Spacer(Modifier.height(16.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { listOf(digit, (digit + 1) % 100, digit - 1, (digit + 3) % 100).shuffled().take(4).forEachIndexed { i, n -> Button(onClick = { if (n == digit) score++; round++; if (round >= total) { if (score > best.value) best.value = score; state = ToolScreenState.Result } else next() }, shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f).height(48.dp).padding(horizontal = 4.dp)) { Text("$n", fontSize = 18.sp) } } } }
            ToolScreenState.Result -> { Spacer(Modifier.height(24.dp)); ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(24.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) { Text("\u8272\u89c9\u6d4b\u8bd5", style = MaterialTheme.typography.titleMedium); Spacer(Modifier.height(8.dp)); Text("$score / $total", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) } }; Spacer(Modifier.height(20.dp)); FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                    Spacer(Modifier.height(8.dp))
                    val enc = remember { randomEncouragement() }
                    Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("色觉测试", "$score", enc); shareResult(context, bitmap, "色觉测试", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun SpotDiffGameScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }
    var score by remember { mutableIntStateOf(0) }
    var round by remember { mutableIntStateOf(0) }
    var baseColor by remember { mutableStateOf(Color(0xFF4CAF50)) }
    var diffColor by remember { mutableStateOf(Color(0xFF4CAF50)) }
    var diffIndex by remember { mutableIntStateOf(0) }
    var gridSize by remember { mutableIntStateOf(4) }
    val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current
    val total = 10

    fun nextRound() {
        gridSize = 4 + round / 3
        val hue = Random.nextFloat() * 360f
        baseColor = Color.hsl(hue, 0.6f, 0.5f)
        diffColor = Color.hsl((hue + 15f + round * 2f) % 360f, 0.6f, 0.5f)
        diffIndex = Random.nextInt(gridSize * gridSize)
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) {
            Text("\u627e\u4e0d\u540c\u6e38\u620f", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("\u627e\u5230\u989c\u8272\u4e0d\u540c\u7684\u90a3\u4e2a\u683c\u5b50", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text("$round / $total | \u5f97\u5206: $score", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
        Spacer(Modifier.weight(1f))

        when (state) {
            ToolScreenState.Idle -> {
                Text("\u672c\u6d4b\u8bd5\u4ec5\u4f9b\u5a31\u4e50\u53c2\u8003", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                Spacer(Modifier.height(16.dp))
                Button(onClick = { state = ToolScreenState.Playing; score = 0; round = 0; nextRound() },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("\u5f00\u59cb\u6e38\u620f", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }
            }
            ToolScreenState.Playing -> {
                Text("\u627e\u5230\u989c\u8272\u4e0d\u540c\u7684\u683c\u5b50!", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                Column(Modifier.aspectRatio(1f).padding(4.dp).sem("interact-area")) {
                    for (row in 0 until gridSize) {
                        Row(Modifier.fillMaxWidth().weight(1f)) {
                            for (col in 0 until gridSize) {
                                val idx = row * gridSize + col
                                Surface(
                                    modifier = Modifier.weight(1f).fillMaxHeight().padding(2.dp)
                                        .clip(RoundedCornerShape(8.dp)).clickable {
                                            if (idx == diffIndex) {
                                                score++
                                            }
                                            round++
                                            if (round >= total) {
                                                if (score > best.value) best.value = score
                                                state = ToolScreenState.Result
                                            } else nextRound()
                                        },
                                    color = if (idx == diffIndex) diffColor else baseColor,
                                    tonalElevation = 0.dp
                                ) {
                                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {}
                                }
                            }
                        }
                    }
                }
            }
            ToolScreenState.Result -> {
                ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(28.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("\u627e\u4e0d\u540c\u6e38\u620f", style = MaterialTheme.typography.titleMedium)
                        if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                        Spacer(Modifier.height(8.dp))
                        Text("$score / $total", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        val grade = when { score >= 9 -> "\u89c2\u5bdf\u529b\u8d85\u5f3a!"; score >= 7 -> "\u4e0d\u9519"; score >= 5 -> "\u4e00\u822c"; else -> "\u7ee7\u7eed\u52a0\u6cb9" }
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
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("找不同", "$score", enc); shareResult(context, bitmap, "找不同", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun DynamicVisionGameScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }
    var score by remember { mutableIntStateOf(0) }
    var misses by remember { mutableIntStateOf(0) }
    var timer by remember { mutableIntStateOf(15) }
    val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current
    var targets by remember { mutableStateOf(listOf<Pair<Float, Float>>()) }
    var totalAppeared by remember { mutableIntStateOf(0) }
    var areaWidth by remember { mutableFloatStateOf(300f) }
    var areaHeight by remember { mutableFloatStateOf(400f) }
    val targetsMutex = remember { Mutex() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state) {
        if (state == ToolScreenState.Playing) {
            val start = System.currentTimeMillis()
            while (System.currentTimeMillis() - start < 15_000L) {
                delay(100L)
                timer = ((15_000L - (System.currentTimeMillis() - start)) / 1000).toInt().coerceAtLeast(0)
            }
            if (score > best.value) best.value = score
            state = ToolScreenState.Result
        }
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) {
            Text("\u52a8\u6001\u89c6\u529b\u6d4b\u8bd5", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("\u5feb\u901f\u70b9\u51fb\u51fa\u73b0\u7684\u79fb\u52a8\u76ee\u6807", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Text("\u547d\u4e2d: $score", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text("\u6f0f\u6389: $misses", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
            Text("${timer}s", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
        Spacer(Modifier.weight(1f))

        when (state) {
            ToolScreenState.Idle -> {
                Text("\u672c\u6d4b\u8bd5\u4ec5\u4f9b\u5a31\u4e50\u53c2\u8003", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                Spacer(Modifier.height(16.dp))
                Button(onClick = {
                        state = ToolScreenState.Playing; score = 0; misses = 0; timer = 15
                        targets = listOf(Random.nextFloat() * 200f + 20f to Random.nextFloat() * 300f + 50f)
                        totalAppeared = 1
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }
            }
            ToolScreenState.Playing -> {
                BoxWithConstraints(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp)).clip(RoundedCornerShape(12.dp)).sem("interact-area")) {
                    val w = this@BoxWithConstraints.maxWidth
                    val h = this@BoxWithConstraints.maxHeight
                    LaunchedEffect(w, h) {
                        areaWidth = w.value
                        areaHeight = h.value
                    }
                    targets.forEachIndexed { i, (tx, ty) ->
                        Box(Modifier.offset(x = (tx.coerceIn(10f, w.value - 50f)).dp, y = (ty.coerceIn(10f, h.value - 50f)).dp)
                            .size(44.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary)
                            .clickable {
                                scope.launch {
                                targetsMutex.withLock {
                                        score++
                                        targets = targets.toMutableList().also {
                                            it.removeAt(i)
                                            it.add(Random.nextFloat() * (w.value - 60f) + 10f to Random.nextFloat() * (h.value - 60f) + 10f)
                                        }
                                    }
                                }
                            }.then(sem("interact-area")),
                            contentAlignment = Alignment.Center
                        ) { Text("\u25cf", color = Color.White, fontSize = 12.sp) }
                    }
                }
                LaunchedEffect(state) {
                    while (state == ToolScreenState.Playing) {
                        delay(Random.nextLong(800L, 2000L))
                        if (state == ToolScreenState.Playing) {
                            targetsMutex.withLock {
                                targets = targets + listOf(Random.nextFloat() * (areaWidth - 60f) + 10f to Random.nextFloat() * (areaHeight - 60f) + 10f)
                                totalAppeared++
                                if (targets.size > 6) {
                                    misses++
                                    targets = targets.drop(1)
                                }
                            }
                        }
                    }
                }
            }
            ToolScreenState.Result -> {
                ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(28.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("\u52a8\u6001\u89c6\u529b\u6d4b\u8bd5", style = MaterialTheme.typography.titleMedium)
                        if (best.value > 0) Text("\u5386\u53f2\u6700\u4f73: ${best.value}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                        Spacer(Modifier.height(8.dp))
                        Text("$score \u547d\u4e2d", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        Text("\u6f0f\u6389 $misses", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (score + misses > 0) {
                            Spacer(Modifier.height(4.dp))
                            Text("\u51c6\u786e\u7387: ${score * 100 / (score + misses)}%", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
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
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("动态视力", "$score", enc); shareResult(context, bitmap, "动态视力", "$score") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}


