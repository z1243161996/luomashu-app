package com.mispec.luomashu.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.reflect.KProperty
import androidx.compose.runtime.saveable.rememberSaveable

// ═══════════════════════════════════════════════════════
// 1. Sealed class state machine — replaces all String states
// ═══════════════════════════════════════════════════════

sealed class ToolScreenState {
    data object Idle    : ToolScreenState()
    data object Playing  : ToolScreenState()
    data object Result  : ToolScreenState()
    data object Waiting : ToolScreenState()
    data object Ready   : ToolScreenState()
    data object Running : ToolScreenState()
    data object Counting : ToolScreenState()
    data object Timeout  : ToolScreenState()
    data object Show     : ToolScreenState()
    data object Hide     : ToolScreenState()
    data object Guessing : ToolScreenState()
    data object Error    : ToolScreenState()
}

// ═══════════════════════════════════════════════════════
// 2. Single shared sem() — dedup across all files
// ═══════════════════════════════════════════════════════

/** Set testTag for automation and contentDescription for accessibility. */
fun Modifier.sem(tag: String, description: String = tag): Modifier =
    this.semantics { testTag = tag; contentDescription = description }

fun sem(tag: String, description: String = tag): Modifier =
    Modifier.semantics { testTag = tag; contentDescription = description }

// ═══════════════════════════════════════════════════════
// 3. Best-score persistence — combines state + DataStore
// ═══════════════════════════════════════════════════════

/**
 * Remembers a best-score value that is persisted via [BestScoreStore].
 *
 * @param toolId unique tool identifier (e.g. "apm", "reaction-time")
 * @param initialValue starting value when no saved score exists
 * @return a [MutableState] whose value auto-persists on update
 */
@Composable
fun rememberBestScore(
    toolId: String,
    initialValue: Int = 0
): MutableState<Int> {
    val best = rememberSaveable { mutableIntStateOf(initialValue) }
    val saveBest = LocalBestScore.current
    return remember {
        object : MutableState<Int> {
            override var value: Int
                get() = best.intValue
                set(newValue) {
                    if (newValue != best.intValue) {
                        best.intValue = newValue
                        if (toolId.isNotEmpty()) saveBest?.invoke(toolId, newValue)
                    }
                }

            override fun component1(): Int = value
            override fun component2(): (Int) -> Unit = { value = it }
        }
    }
}

// ═══════════════════════════════════════════════════════
// 4. Shared disclaimer banner
// ═══════════════════════════════════════════════════════

@Composable
fun DisclaimerBanner() {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            "本测试仅供娱乐参考，不构成专业评估",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
