package com.mispec.luomashu.tools

import androidx.compose.runtime.Composable
import com.mispec.luomashu.ui.components.*

@Composable fun VisionScreen() = VisualAcuityScreen(toolId = "vision")
@Composable fun HearingScreen() = HearingTestScreen(toolId = "hearing")
@Composable fun PitchScreen() = PitchTestScreen(toolId = "pitch")
@Composable fun RhythmScreen() = RhythmTestScreen(toolId = "rhythm")
@Composable fun ColorSensitivityScreen() = ColorBlindTestScreen(toolId = "color-sensitivity")
@Composable fun AngleScreen() = AngleTestScreen(toolId = "angle")
@Composable fun SpotDiffScreen() = SpotDiffGameScreen(toolId = "spot-difference")
@Composable fun DynamicVisionScreen() = DynamicVisionGameScreen(toolId = "dynamic-vision")
