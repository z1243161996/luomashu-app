package com.mispec.luomashu.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mispec.luomashu.ui.theme.AppColors

private data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val color: androidx.compose.ui.graphics.Color
)

private val pages = listOf(
    OnboardingPage(
        icon = Icons.Filled.EmojiEvents,
        title = "58 个趣味测试等你挑战",
        subtitle = "反应速度、认知能力、性格测试、知识百科……应有尽有",
        color = AppColors.accent
    ),
    OnboardingPage(
        icon = Icons.Filled.Psychology,
        title = "全面了解你的大脑",
        subtitle = "反应 · 记忆 · 推理 · 性格 · 感官 · 趣味六大维度",
        color = AppColors.catCognitive
    ),
    OnboardingPage(
        icon = Icons.Filled.Timer,
        title = "每次只需 10 秒 ~ 3 分钟",
        subtitle = "碎片时间也能玩，随时随地测试一下",
        color = AppColors.success
    )
)

@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    var currentPage by remember { mutableIntStateOf(0) }

    Column(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Skip button
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onComplete) {
                Text("跳过", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(Modifier.height(40.dp))

        // Page content with crossfade animation
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                },
                label = "onboarding-page"
            ) { page ->
                val p = pages[page]
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        color = p.color.copy(alpha = 0.12f),
                        shape = CircleShape,
                        modifier = Modifier.size(120.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(p.icon, contentDescription = null, tint = p.color, modifier = Modifier.size(56.dp))
                        }
                    }
                    Spacer(Modifier.height(32.dp))
                    Text(p.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(12.dp))
                    Text(p.subtitle, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                }
            }
        }

        // Page indicators with animation
        Row(Modifier.padding(vertical = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(pages.size) { i ->
                val selected = currentPage == i
                val size by animateDpAsState(
                    targetValue = if (selected) 10.dp else 8.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "indicator"
                )
                Surface(
                    color = if (selected) AppColors.accent else MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape,
                    modifier = Modifier.size(size)
                ) {}
            }
        }

        Spacer(Modifier.height(16.dp))

        if (currentPage == pages.lastIndex) {
            Button(
                onClick = onComplete,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.accent)
            ) {
                Text("开始探索", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        } else {
            FilledTonalButton(
                onClick = { currentPage++ },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("下一步", fontSize = 16.sp)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
