package com.mispec.luomashu.util

import android.view.HapticFeedbackConstants
import android.view.View

/**
 * Performs a haptic feedback confirmation vibration.
 * Call this in a Button's onClick or similar interaction.
 * Usage: performHaptic(LocalView.current)
 */
fun performHaptic(view: View) {
    view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
}
