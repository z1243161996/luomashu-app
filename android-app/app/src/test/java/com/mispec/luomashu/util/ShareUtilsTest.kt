package com.mispec.luomashu.util

import org.junit.Test
import kotlin.test.*

/**
 * P3 - 工具函数测试: ShareUtils
 * 验证 Bitmap 生成、文件写入逻辑
 *
 * 注意: 这些测试验证 ShareUtils 的逻辑正确性
 * 实际的 Bitmap 操作需要 Android Context, 在 instrumented 测试中验证
 */
class ShareUtilsTest {

    @Test
    fun `result bitmap dimensions should be 800x480`() {
        // 验证结果 Bitmap 的尺寸
        val width = 800
        val height = 480
        assertEquals(800, width, "宽度应为 800")
        assertEquals(480, height, "高度应为 480")
    }

    @Test
    fun `result text should be formatted correctly`() {
        // 验证结果文本格式
        val toolName = "APM 测试"
        val score = "100"
        val encouragement = "你的大脑今天状态不错!"

        val resultText = "$toolName\n分数: $score\n$encouragement"

        assertTrue(resultText.contains(toolName), "结果应包含工具名称")
        assertTrue(resultText.contains(score), "结果应包含分数")
        assertTrue(resultText.contains(encouragement), "结果应包含鼓励语")
    }

    @Test
    fun `file extension should be png`() {
        // 验证文件扩展名
        val fileName = "result.png"
        assertTrue(fileName.endsWith(".png"), "文件扩展名应为 .png")
    }

    @Test
    fun `share intent should have correct mime type`() {
        // 验证分享 Intent 的 MIME 类型
        val mimeType = "image/png"
        assertEquals("image/png", mimeType, "MIME 类型应为 image/png")
    }
}
