package com.mispec.luomashu.ui.components

import com.mispec.luomashu.util.sem
import com.mispec.luomashu.util.ToolScreenState
import com.mispec.luomashu.util.rememberBestScore
import com.mispec.luomashu.util.randomEncouragement
import com.mispec.luomashu.util.performHaptic
import com.mispec.luomashu.util.createResultBitmap
import com.mispec.luomashu.util.shareResult
import kotlinx.coroutines.launch

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.CancellationException

object ToolDefaults { const val DURATION_SEC = 10 }

// ── Click Tool Screen ──

@Composable
fun ClickToolScreen(
    title: String,
    subtitle: String,
    durationSec: Int = ToolDefaults.DURATION_SEC,
    toolId: String = "",
    resultText: (Int) -> String = { "$it" }
) {
    var state by remember { mutableStateOf<ToolScreenState>(ToolScreenState.Idle) }
    var clicks by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(durationSec) }
    val best = rememberBestScore(toolId)
    val view = LocalView.current
    val context = LocalContext.current

    // Update best score only when state changes to Result (not on every recomposition)
    LaunchedEffect(state) {
        if (state == ToolScreenState.Result && clicks > best.value) {
            best.value = clicks
        }
    }

    LaunchedEffect(state) {
        try {
            if (state == ToolScreenState.Running) {
                while (timeLeft > 0) { delay(1000L); timeLeft-- }
                state = ToolScreenState.Result
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("ClickToolScreen", "Timer error", e)
            state = ToolScreenState.Result
        }
    }

    Column(
        Modifier.fillMaxSize().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

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

        // Title
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("tool-title")) {
            Text(
                title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = sem("tool-subtitle")
            )
        }

        Spacer(Modifier.height(8.dp))

        // Timer indicator
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(Modifier.padding(horizontal = 14.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Timer, contentDescription = "计时器", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("${timeLeft}s", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(Modifier.height(24.dp))

        // Score display — large, warm accent
        // Hide in Result state since the result card below already shows the full result text
        if (state != ToolScreenState.Result) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("score-value")) {
                Text(
                    "$clicks",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                if (best.value > 0) {
                    Text(
                        "历史最佳: ${best.value}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        when (state) {
            ToolScreenState.Idle -> {
                Button(
                    onClick = { performHaptic(view); state = ToolScreenState.Running; timeLeft = durationSec; clicks = 0 },
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
            ToolScreenState.Running -> {
                // Interact area — large, elevated card
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .then(sem("interact-area", "交互操作区域"))
                        .clickable { clicks++ },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                    )
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Filled.TouchApp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "快速点击此区域",
                                modifier = Modifier.sem("interact-hint"),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "越快越好",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Progress bar
                LinearProgressIndicator(
                    progress = { 1f - timeLeft.toFloat() / durationSec },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )

                Spacer(Modifier.height(8.dp))
            }
            ToolScreenState.Result -> {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .then(sem("result-screen", "测试结果")),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Result card
                    ElevatedCard(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Filled.EmojiEvents,
                                contentDescription = "结果图标",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp).sem("result-icon")
                            )
                            Spacer(Modifier.height(8.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = sem("score-value")) {
                                Text(
                                    resultText(clicks),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    val encouragement = remember { randomEncouragement() }

                    Text(
                        encouragement,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 12.dp),
                        textAlign = TextAlign.Center
                    )

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
                            val bitmap = createResultBitmap(title, resultText(clicks), encouragement)
                            shareResult(context, bitmap, title, resultText(clicks))
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Icon(Icons.Filled.Share, contentDescription = "分享结果", modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("分享成绩", fontSize = 13.sp)
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
            else -> {}
        }
    }
}
