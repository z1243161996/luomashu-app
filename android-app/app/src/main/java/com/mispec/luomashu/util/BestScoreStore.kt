package com.mispec.luomashu.util

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.bestScoreStore: DataStore<Preferences> by preferencesDataStore(name = "best_scores")

/** Persisted best-score storage backed by Jetpack DataStore Preferences. */
object BestScoreStore {
    private const val PREFIX = "best_"

    /** Save a best score for a given tool. Overwrites previous value. */
    suspend fun save(context: Context, toolId: String, score: Int) {
        val intKey = intPreferencesKey("$PREFIX$toolId")
        context.bestScoreStore.edit { prefs ->
            prefs[intKey] = score
        }
    }

    /** Load the best score for a tool, or 0 if none recorded. Supports migration from string format. */
    suspend fun load(context: Context, toolId: String): Int {
        val intKey = intPreferencesKey("$PREFIX$toolId")
        val stringKey = stringPreferencesKey("$PREFIX$toolId")
        return context.bestScoreStore.data.map { prefs ->
            // Try int key first, fall back to string key for migration
            prefs[intKey] ?: prefs[stringKey]?.toIntOrNull() ?: 0
        }.first()
    }
}

val LocalBestScore = compositionLocalOf<((String, Int) -> Unit)?> { null }
