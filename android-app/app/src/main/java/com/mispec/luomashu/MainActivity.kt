package com.mispec.luomashu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.mispec.luomashu.audio.AudioEngine
import com.mispec.luomashu.ui.home.HomeScreen
import com.mispec.luomashu.ui.theme.LuomashuTheme
import com.mispec.luomashu.util.BestScoreStore
import com.mispec.luomashu.util.LocalBestScore
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            val ctx = this@MainActivity
            LuomashuTheme {
                CompositionLocalProvider(
                    LocalBestScore provides { toolId, score ->
                        scope.launch {
                            BestScoreStore.save(ctx, toolId, score)
                        }
                    }
                ) {
                    HomeScreen()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AudioEngine.release()
    }
}
