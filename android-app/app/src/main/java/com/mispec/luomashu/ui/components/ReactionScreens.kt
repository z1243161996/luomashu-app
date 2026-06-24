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
import kotlinx.coroutines.CancellationException
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

@Composable
fun ReactionTimeScreen(toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }
    var reactionMs by remember { mutableIntStateOf(0) }
    val bestMs = rememberBestScore(toolId, Int.MAX_VALUE)
    var startNanos by remember { mutableLongStateOf(0L) }
    var trialCount by remember { mutableIntStateOf(0) }
    val view = LocalView.current
    val context = LocalContext.current

    LaunchedEffect(state) {
        if (state == ToolScreenState.Waiting) {
            delay((1500L..4000L).random())
            startNanos = System.nanoTime()
            state = ToolScreenState.Ready
        }
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(28.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) {
            Text("\u53cd\u5e94\u901f\u5ea6\u6d4b\u8bd5", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("\u770b\u5230\u7eff\u8272\u540e\u7acb\u5373\u70b9\u51fb", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(16.dp))
        if (trialCount > 0) Text("\u6d4b\u8bd5\u6b21\u6570: $trialCount", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (bestMs.value < Int.MAX_VALUE) Text("\u6700\u5feb: ${bestMs.value}ms", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.weight(1f))

        when (state) {
            ToolScreenState.Idle -> {
                DisclaimerBanner()
                Box(Modifier.size(200.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                    Text("\u51c6\u5907", fontSize = 24.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(Modifier.height(32.dp))
                Button(onClick = { performHaptic(view); trialCount++; state = ToolScreenState.Waiting },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("\u5f00\u59cb\u6d4b\u8bd5", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }
            }
            ToolScreenState.Waiting -> {
                Box(Modifier.size(200.dp).clip(CircleShape).background(AppColors.danger), contentAlignment = Alignment.Center) {
                    Text("\u7b49\u5f85...", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(Modifier.height(16.dp))
                Text("\u770b\u5230\u7eff\u8272\u65f6\u7acb\u5373\u70b9\u51fb", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            ToolScreenState.Ready -> {
                Box(Modifier.size(200.dp).clip(CircleShape).background(AppColors.success)
                    .clickable {
                        reactionMs = ((System.nanoTime() - startNanos) / 1_000_000).toInt()
                        if (reactionMs < bestMs.value) bestMs.value = reactionMs
                        state = ToolScreenState.Result
                    }
                    .then(sem("interact-area")), contentAlignment = Alignment.Center
                ) { Text("\u70b9\u51fb\uff01", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
                Spacer(Modifier.height(16.dp))
                Text("\u5feb\u70b9\u51fb\u7eff\u8272\u533a\u57df\uff01", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
            ToolScreenState.Result -> {
                ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(28.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("\u4f60\u7684\u53cd\u5e94\u901f\u5ea6", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(8.dp))
                        Text("${reactionMs}ms", fontSize = 52.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(8.dp))
                        val grade = when { reactionMs < 200 -> "\u25b2 \u95ea\u7535\u53cd\u5e94"; reactionMs < 300 -> "\u25b8 \u4f18\u79c0"; reactionMs < 400 -> "\u25e6 \u826f\u597d"; else -> "\u2216 \u7ee7\u7eed\u52a0\u6cb9" }
                        Text(grade, style = MaterialTheme.typography.titleSmall)
                    }
                }
                Spacer(Modifier.height(20.dp))
                FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")
                ) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                    Spacer(Modifier.height(8.dp))
                    val enc = remember { randomEncouragement() }
                    Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("反应速度", "$reactionMs", enc); shareResult(context, bitmap, "反应速度", "$reactionMs") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}


@Composable
fun TimingChallengeScreen(targetSec: Int = 5, toolId: String = "") {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }
    var elapsed by remember { mutableFloatStateOf(0f) }
    var result by remember { mutableFloatStateOf(0f) }
    val best = rememberBestScore(toolId, Float.MAX_VALUE.toInt())
    val view = LocalView.current
    val context = LocalContext.current

    LaunchedEffect(state) {
        try {
            if (state == ToolScreenState.Counting) {
                val start = System.nanoTime()
                while (state == ToolScreenState.Counting && elapsed < targetSec + 3f) {
                    delay(33L)
                    elapsed = (System.nanoTime() - start) / 1_000_000_000f
                }
                if (state == ToolScreenState.Counting) state = ToolScreenState.Timeout
            }
        } catch (e: CancellationException) { throw e } catch (_: Exception) { state = ToolScreenState.Result }
    }

    // Update best score only when state changes to Result (not on every recomposition)
    LaunchedEffect(state) {
        if (state == ToolScreenState.Result) {
            val diff = abs(result - targetSec)
            val diffMs = (diff * 1000).toInt()
            if (diffMs < best.value) {
                best.value = diffMs
            }
        }
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(28.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) {
            Text("\u8ba1\u65f6\u6311\u6218", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("\u5728\u6070\u597d ${targetSec} \u79d2\u65f6\u70b9\u51fb\u505c\u6b62", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (best.value < Int.MAX_VALUE) Text("\u6700\u4f73: %.2fs".format(best.value / 1000f), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.weight(1f))

        when (state) {
            ToolScreenState.Idle -> {
                Text("%.1f".format(0f), fontSize = 72.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(32.dp))
                Button(onClick = { performHaptic(view); state = ToolScreenState.Counting; elapsed = 0f },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("btn-primary", "开始测试按钮")),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("\u5f00\u59cb\u8ba1\u65f6", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }
            }
            ToolScreenState.Counting -> {
                Text("%.2f".format(elapsed), fontSize = 72.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
                Text("\u79d2", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(24.dp))
                Button(onClick = { performHaptic(view); result = elapsed; state = ToolScreenState.Result },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp).then(sem("interact-area")),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Icon(Icons.Filled.Stop, null, Modifier.size(20.dp)); Spacer(Modifier.width(8.dp)); Text("\u505c\u6b62", fontSize = 16.sp) }
            }
            ToolScreenState.Timeout -> {
                Text("\u8d85\u65f6", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(20.dp))
                FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")
                ) { Text("\u518d\u6765\u4e00\u6b21") }
            }
            ToolScreenState.Result -> {
                val diff = abs(result - targetSec)
                Text("%.3f".format(result), fontSize = 56.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                Text("\u79d2", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(24.dp).then(sem("result-screen", "测试结果")), horizontalAlignment = Alignment.CenterHorizontally) {
                        val msg = when { diff < 0.05f -> "\u25c9 \u5b8c\u7f8e\uff01\u51e0\u4e4e\u7cbe\u786e"; diff < 0.2f -> "\u25b8 \u975e\u5e38\u63a5\u8fd1"; diff < 0.5f -> "\u25e6 \u4e0d\u9519"; else -> "\u2216 \u7ee7\u7eed\u7ec3\u4e60" }
                        Text("\u504f\u5dee: %.2f \u79d2".format(diff), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text(msg, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.height(20.dp))
                FilledTonalButton(onClick = { performHaptic(view); state = ToolScreenState.Idle },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp).sem("btn-secondary", "再来一次按钮")
                ) { Icon(Icons.Filled.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("\u518d\u6765\u4e00\u6b21") }
                    Spacer(Modifier.height(8.dp))
                    val enc = remember { randomEncouragement() }
                    Text(enc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { performHaptic(view); val bitmap = createResultBitmap("计时挑战", "$targetSec", enc); shareResult(context, bitmap, "计时挑战", "$targetSec") }, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().height(44.dp)) { Icon(Icons.Filled.Share, contentDescription = "\u5206\u4eab\u7ed3\u679c", modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("\u5206\u4eab\u6210\u7ee9", fontSize = 13.sp) }
            }
            else -> {}
        }
        Spacer(Modifier.weight(1f))
    }
}


