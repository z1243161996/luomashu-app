package com.mispec.luomashu.data

import org.junit.Test
import kotlin.test.*

/**
 * 单元测试 - ToolDef 数据类
 * 验证工具定义的完整性和正确性
 */
class ToolDefTest {

    @Test
    fun `total tool count is 58`() {
        val allTools = Category.entries.flatMap { categoryTools(it) }
        assertEquals(58, allTools.size, "App must have exactly 58 tools")
    }

    @Test
    fun `tool IDs are unique`() {
        val allTools = Category.entries.flatMap { categoryTools(it) }
        val ids = allTools.map { it.id }
        assertEquals(ids.size, ids.toSet().size, "Tool IDs must be unique")
    }

    @Test
    fun `tool names are non-empty`() {
        val allTools = Category.entries.flatMap { categoryTools(it) }
        allTools.forEach { tool ->
            assertTrue(tool.name.isNotEmpty(), "Tool ${tool.id} name must not be empty")
        }
    }

    @Test
    fun `tool estimated seconds are positive`() {
        val allTools = Category.entries.flatMap { categoryTools(it) }
        allTools.forEach { tool ->
            assertTrue(tool.estimatedSeconds > 0, "Tool ${tool.id} estimatedSeconds must be positive")
        }
    }

    @Test
    fun `NEURO tools count`() {
        assertEquals(6, NeuroTools.size, "NEURO must have 6 tools")
    }

    @Test
    fun `COGNITIVE tools count`() {
        assertEquals(13, CognitiveTools.size, "COGNITIVE must have 13 tools")
    }

    @Test
    fun `PERSONALITY tools count`() {
        assertEquals(12, PersonalityTools.size, "PERSONALITY must have 12 tools")
    }

    @Test
    fun `KNOWLEDGE tools count`() {
        assertEquals(12, KnowledgeTools.size, "KNOWLEDGE must have 12 tools")
    }

    @Test
    fun `SENSORY tools count`() {
        assertEquals(8, SensoryTools.size, "SENSORY must have 8 tools")
    }

    @Test
    fun `PLAYGROUND tools count`() {
        assertEquals(7, PlaygroundTools.size, "PLAYGROUND must have 7 tools")
    }

    @Test
    fun `all tool types are valid`() {
        val allTools = Category.entries.flatMap { categoryTools(it) }
        allTools.forEach { tool ->
            assertTrue(
                tool.type in ToolType.entries,
                "Tool ${tool.id} has invalid type: ${tool.type}"
            )
        }
    }

    @Test
    fun `CLICK tools exist`() {
        val allTools = Category.entries.flatMap { categoryTools(it) }
        val clickTools = allTools.filter { it.type == ToolType.CLICK }
        assertTrue(clickTools.isNotEmpty(), "Must have at least one CLICK tool")
    }

    @Test
    fun `QUIZ tools exist`() {
        val allTools = Category.entries.flatMap { categoryTools(it) }
        val quizTools = allTools.filter { it.type == ToolType.QUIZ }
        assertTrue(quizTools.isNotEmpty(), "Must have at least one QUIZ tool")
    }

    @Test
    fun `SCALE tools exist`() {
        val allTools = Category.entries.flatMap { categoryTools(it) }
        val scaleTools = allTools.filter { it.type == ToolType.SCALE }
        assertTrue(scaleTools.isNotEmpty(), "Must have at least one SCALE tool")
    }

    @Test
    fun `apm tool exists in NEURO`() {
        val apm = NeuroTools.find { it.id == "apm" }
        assertNotNull(apm, "APM tool must exist in NEURO")
        assertEquals("APM 测试", apm.name)
        assertEquals(ToolType.CLICK, apm.type)
        assertEquals(10, apm.estimatedSeconds)
    }

    @Test
    fun `iq tool exists in COGNITIVE`() {
        val iq = CognitiveTools.find { it.id == "iq" }
        assertNotNull(iq, "IQ tool must exist in COGNITIVE")
        assertEquals("IQ 测试", iq.name)
        assertEquals(ToolType.QUIZ, iq.type)
        assertEquals(90, iq.estimatedSeconds)
    }

    @Test
    fun `mbti tool exists in PERSONALITY`() {
        val mbti = PersonalityTools.find { it.id == "mbti" }
        assertNotNull(mbti, "MBTI tool must exist in PERSONALITY")
        assertEquals("MBTI 测试", mbti.name)
        assertEquals(ToolType.SCALE, mbti.type)
        assertEquals(60, mbti.estimatedSeconds)
    }

    @Test
    fun `trivia tool exists in KNOWLEDGE`() {
        val trivia = KnowledgeTools.find { it.id == "trivia" }
        assertNotNull(trivia, "Trivia tool must exist in KNOWLEDGE")
        assertEquals("百科知识测试", trivia.name)
        assertEquals(ToolType.QUIZ, trivia.type)
    }

    @Test
    fun `minesweeper tool exists in PLAYGROUND`() {
        val ms = PlaygroundTools.find { it.id == "minesweeper" }
        assertNotNull(ms, "Minesweeper tool must exist in PLAYGROUND")
        assertEquals("扫雷", ms.name)
        assertEquals(ToolType.CLICK, ms.type)
        assertEquals(30, ms.estimatedSeconds)
    }

    @Test
    fun `ToolType enum has 5 values`() {
        assertEquals(5, ToolType.entries.size)
        assertTrue(ToolType.entries.contains(ToolType.CLICK))
        assertTrue(ToolType.entries.contains(ToolType.QUIZ))
        assertTrue(ToolType.entries.contains(ToolType.SCALE))
        assertTrue(ToolType.entries.contains(ToolType.TARGET))
        assertTrue(ToolType.entries.contains(ToolType.CUSTOM))
    }

    @Test
    fun `ToolDef data class properties`() {
        val tool = ToolDef("test", "Test Tool", ToolType.CLICK, 15)
        assertEquals("test", tool.id)
        assertEquals("Test Tool", tool.name)
        assertEquals(ToolType.CLICK, tool.type)
        assertEquals(15, tool.estimatedSeconds)
    }

    @Test
    fun `ToolDef default estimatedSeconds`() {
        val tool = ToolDef("test", "Test Tool", ToolType.QUIZ)
        assertEquals(30, tool.estimatedSeconds, "Default estimatedSeconds should be 30")
    }
}
