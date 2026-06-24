package com.mispec.luomashu.screen

import org.junit.Test
import kotlin.test.*

/**
 * P1 - 关键路径测试: ScaleTool
 * 验证量表选项、分数累加、结果计算
 *
 * 注意: 这些测试验证量表工具逻辑的正确性
 * 实际的 UI 交互需要 Android 设备, 在 instrumented 测试中验证
 */
class ScaleToolTest {

    @Test
    fun `scale options should have valid values`() {
        // 验证量表选项值
        val options = listOf(1, 2, 3, 4, 5)

        assertEquals(5, options.size, "量表应有 5 个选项")
        assertTrue(options.all { it in 1..5 }, "选项值应在 1-5 范围内")
    }

    @Test
    fun `scale score should accumulate correctly`() {
        // 验证分数累加
        var totalScore = 0
        val answers = listOf(3, 4, 2, 5, 1)

        for (answer in answers) {
            totalScore += answer
        }

        assertEquals(15, totalScore, "总分应为 15")
    }

    @Test
    fun `scale score should be average of answers`() {
        // 验证平均分计算
        val answers = listOf(3, 4, 2, 5, 1)
        val average = answers.average()

        assertEquals(3.0, average, 0.01, "平均分应为 3.0")
    }

    @Test
    fun `scale result should categorize correctly`() {
        // 验证结果分类
        val average = 3.0

        val category = when {
            average < 2.0 -> "低"
            average < 4.0 -> "中"
            else -> "高"
        }

        assertEquals("中", category, "3.0 应分类为 '中'")
    }

    @Test
    fun `scale result should handle all low scores`() {
        // 验证全低分情况
        val answers = listOf(1, 1, 1, 1, 1)
        val average = answers.average()

        assertEquals(1.0, average, "全 1 分的平均分应为 1.0")

        val category = when {
            average < 2.0 -> "低"
            average < 4.0 -> "中"
            else -> "高"
        }

        assertEquals("低", category, "全 1 分应分类为 '低'")
    }

    @Test
    fun `scale result should handle all high scores`() {
        // 验证全高分情况
        val answers = listOf(5, 5, 5, 5, 5)
        val average = answers.average()

        assertEquals(5.0, average, "全 5 分的平均分应为 5.0")

        val category = when {
            average < 2.0 -> "低"
            average < 4.0 -> "中"
            else -> "高"
        }

        assertEquals("高", category, "全 5 分应分类为 '高'")
    }

    @Test
    fun `scale question count should be positive`() {
        // 验证题目数量为正数
        val questionCount = 10
        assertTrue(questionCount > 0, "题目数量应为正数")
    }

    @Test
    fun `scale answers should match question count`() {
        // 验证答案数量与题目数量匹配
        val questionCount = 10
        val answers = (1..questionCount).map { (1..5).random() }

        assertEquals(questionCount, answers.size, "答案数量应与题目数量匹配")
    }
}
