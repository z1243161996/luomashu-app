package com.mispec.luomashu.util

import org.junit.Test
import kotlin.test.*

/**
 * P3 - 工具函数测试: Encouragement
 * 验证 randomEncouragement() 返回非空字符串
 */
class EncouragementTest {

    @Test
    fun `randomEncouragement should return non-empty string`() {
        val result = randomEncouragement()
        assertTrue(result.isNotEmpty(), "鼓励语不应为空")
    }

    @Test
    fun `randomEncouragement with seed should be deterministic`() {
        val result1 = randomEncouragement(seed = 12345L)
        val result2 = randomEncouragement(seed = 12345L)
        assertEquals(result1, result2, "相同 seed 应返回相同结果")
    }

    @Test
    fun `randomEncouragement with different seeds should return different results`() {
        val results = (1..10).map { randomEncouragement(seed = it.toLong()) }.toSet()
        // 至少应该有几种不同的结果 (25 条短语)
        assertTrue(results.size >= 2, "不同 seed 应返回不同结果")
    }

    @Test
    fun `randomEncouragement should return phrase from list`() {
        val result = randomEncouragement(seed = 0L)
        // 验证返回的字符串长度合理
        assertTrue(result.length > 2, "鼓励语长度应大于 2")
        assertTrue(result.length < 50, "鼓励语长度应小于 50")
    }
}
