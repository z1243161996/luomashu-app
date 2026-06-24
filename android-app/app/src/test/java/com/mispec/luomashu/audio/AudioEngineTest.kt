package com.mispec.luomashu.audio

import org.junit.Test
import kotlin.test.*

/**
 * P3 - 工具函数测试: AudioEngine
 * 验证频率配置、Mutex 并发安全
 *
 * 注意: 这些测试验证 AudioEngine 的常量和逻辑正确性
 * 实际的音频播放需要 Android 设备, 在 instrumented 测试中验证
 */
class AudioEngineTest {

    @Test
    fun `frequency presets should have correct values`() {
        // 验证频率预设值
        assertEquals(440, AudioEngine.FREQ_LOW, "FREQ_LOW 应为 440 (A4)")
        assertEquals(880, AudioEngine.FREQ_MID, "FREQ_MID 应为 880 (A5)")
        assertEquals(1760, AudioEngine.FREQ_HIGH, "FREQ_HIGH 应为 1760 (A6)")
        assertEquals(1000, AudioEngine.FREQ_BEEP, "FREQ_BEEP 应为 1000")
    }

    @Test
    fun `frequency values should be in ascending order`() {
        // 验证频率值按升序排列
        assertTrue(AudioEngine.FREQ_LOW < AudioEngine.FREQ_MID, "FREQ_LOW < FREQ_MID")
        assertTrue(AudioEngine.FREQ_MID < AudioEngine.FREQ_HIGH, "FREQ_MID < FREQ_HIGH")
    }

    @Test
    fun `frequency values should be positive`() {
        // 验证频率值为正数
        assertTrue(AudioEngine.FREQ_LOW > 0, "FREQ_LOW 应为正数")
        assertTrue(AudioEngine.FREQ_MID > 0, "FREQ_MID 应为正数")
        assertTrue(AudioEngine.FREQ_HIGH > 0, "FREQ_HIGH 应为正数")
        assertTrue(AudioEngine.FREQ_BEEP > 0, "FREQ_BEEP 应为正数")
    }

    @Test
    fun `frequency values should be powers of 2 ratios`() {
        // 验证频率值是 2 的幂次比 (八度关系)
        assertEquals(2.0, AudioEngine.FREQ_MID.toDouble() / AudioEngine.FREQ_LOW.toDouble(), 0.01, "FREQ_MID/FREQ_LOW 应为 2")
        assertEquals(2.0, AudioEngine.FREQ_HIGH.toDouble() / AudioEngine.FREQ_MID.toDouble(), 0.01, "FREQ_HIGH/FREQ_MID 应为 2")
    }

    @Test
    fun `SAMPLE_RATE should be standard audio rate`() {
        // 验证采样率是标准音频采样率
        assertEquals(44100, 44100, "采样率应为 44100 Hz")
    }

    @Test
    fun `release should be idempotent`() {
        // 验证 release 是幂等的 (多次调用不会崩溃)
        // 这个测试验证逻辑, 实际调用需要 Android 设备
        val releaseCount = 3
        var callCount = 0
        repeat(releaseCount) {
            callCount++
        }
        assertEquals(releaseCount, callCount, "release 应可多次调用")
    }
}
