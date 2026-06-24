package com.mispec.luomashu.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.recentStore by preferencesDataStore(name = "recent_tools")

/**
 * Manages the list of recently used tool IDs (most recent first, max 6).
 */
object RecentStore {
    private val KEY_IDS = stringPreferencesKey("recent_tool_ids")

    /** Flow of recently used tool IDs, most recent first. */
    fun recentIds(context: Context): Flow<List<String>> =
        context.recentStore.data.map { prefs ->
            prefs[KEY_IDS]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
        }

    /** Record a tool use. Moves the tool to the top of the list, max 6 entries. */
    suspend fun recordUse(context: Context, toolId: String) {
        if (toolId.isEmpty()) return
        context.recentStore.edit { prefs ->
            val current = prefs[KEY_IDS]?.split(",")?.filter { it.isNotEmpty() }?.toMutableList() ?: mutableListOf()
            current.remove(toolId)
            current.add(0, toolId)
            val trimmed = current.take(6)
            prefs[KEY_IDS] = trimmed.joinToString(",")
        }
    }
}
