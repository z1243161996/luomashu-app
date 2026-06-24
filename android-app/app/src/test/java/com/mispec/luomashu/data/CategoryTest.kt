package com.mispec.luomashu.data

import org.junit.Test
import kotlin.test.*

/**
 * 单元测试 - Category 枚举
 * 验证 6 个分类的定义完整性和正确性
 */
class CategoryTest {

    @Test
    fun `all 6 categories exist`() {
        assertEquals(6, Category.entries.size)
    }

    @Test
    fun `category IDs are unique`() {
        val ids = Category.entries.map { it.id }
        assertEquals(ids.size, ids.toSet().size, "Category IDs must be unique")
    }

    @Test
    fun `category labels are non-empty`() {
        Category.entries.forEach { cat ->
            assertTrue(cat.label.isNotEmpty(), "Category ${cat.id} label must not be empty")
        }
    }

    @Test
    fun `category descriptions are non-empty`() {
        Category.entries.forEach { cat ->
            assertTrue(cat.desc.isNotEmpty(), "Category ${cat.id} desc must not be empty")
        }
    }

    @Test
    fun `category icons are non-empty`() {
        Category.entries.forEach { cat ->
            assertTrue(cat.icon.isNotEmpty(), "Category ${cat.id} icon must not be empty")
        }
    }

    @Test
    fun `NEURO category has correct properties`() {
        val cat = Category.NEURO
        assertEquals("neuro", cat.id)
        assertEquals("神经反应", cat.label)
        assertEquals("反应速度 · 协调性 · 手速", cat.desc)
    }

    @Test
    fun `COGNITIVE category has correct properties`() {
        val cat = Category.COGNITIVE
        assertEquals("cognitive", cat.id)
        assertEquals("思维认知", cat.label)
        assertEquals("记忆 · 专注 · 推理", cat.desc)
    }

    @Test
    fun `PERSONALITY category has correct properties`() {
        val cat = Category.PERSONALITY
        assertEquals("personality", cat.id)
        assertEquals("性格镜像", cat.label)
        assertEquals("MBTI · 九型 · 大五", cat.desc)
    }

    @Test
    fun `KNOWLEDGE category has correct properties`() {
        val cat = Category.KNOWLEDGE
        assertEquals("knowledge", cat.id)
        assertEquals("知识百科", cat.label)
        assertEquals("常识 · 成语 · 谜语", cat.desc)
    }

    @Test
    fun `SENSORY category has correct properties`() {
        val cat = Category.SENSORY
        assertEquals("sensory", cat.id)
        assertEquals("感官挑战", cat.label)
        assertEquals("视觉 · 听觉 · 色觉", cat.desc)
    }

    @Test
    fun `PLAYGROUND category has correct properties`() {
        val cat = Category.PLAYGROUND
        assertEquals("playground", cat.id)
        assertEquals("趣味游乐", cat.label)
    }

    @Test
    fun `each category has tools`() {
        Category.entries.forEach { cat ->
            val tools = categoryTools(cat)
            assertTrue(tools.isNotEmpty(), "Category ${cat.id} must have at least one tool")
        }
    }

    @Test
    fun `categoryTools returns correct category`() {
        val neuroTools = categoryTools(Category.NEURO)
        val cognitiveTools = categoryTools(Category.COGNITIVE)
        val personalityTools = categoryTools(Category.PERSONALITY)
        val knowledgeTools = categoryTools(Category.KNOWLEDGE)
        val sensoryTools = categoryTools(Category.SENSORY)
        val playgroundTools = categoryTools(Category.PLAYGROUND)

        assertNotEquals(neuroTools, cognitiveTools)
        assertNotEquals(neuroTools, personalityTools)
        assertNotEquals(cognitiveTools, knowledgeTools)
        assertNotEquals(personalityTools, sensoryTools)
        assertNotEquals(knowledgeTools, playgroundTools)
    }
}
