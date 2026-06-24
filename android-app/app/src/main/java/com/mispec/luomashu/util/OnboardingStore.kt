package com.mispec.luomashu.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.onboardingStore by preferencesDataStore(name = "onboarding")

object OnboardingStore {
    private val KEY_SHOWN = booleanPreferencesKey("onboarding_shown")

    /** Whether onboarding has been completed. */
    fun hasShown(context: Context): Flow<Boolean> =
        context.onboardingStore.data.map { prefs ->
            prefs[KEY_SHOWN] ?: false
        }

    /** Mark onboarding as completed. */
    suspend fun markShown(context: Context) {
        context.onboardingStore.edit { prefs ->
            prefs[KEY_SHOWN] = true
        }
    }
}
