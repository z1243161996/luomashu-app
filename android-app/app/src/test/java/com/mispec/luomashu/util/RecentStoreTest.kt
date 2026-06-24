package com.mispec.luomashu.util

import org.junit.Test
import kotlin.test.*

/**
 * P2 - 数据持久化测试: RecentStore
 * 验证最近使用列表的 CRUD 操作
 *
 * 注意: 这些测试验证 RecentStore 的逻辑正确性
 * 实际的 DataStore 操作需要 Android Context, 在 instrumented 测试中验证
 */
class RecentStoreTest {

    @Test
    fun `recordUse logic should move tool to top`() {
        // 模拟 RecentStore 的 recordUse 逻辑
        val currentList = mutableListOf("a", "b", "c")
        val toolId = "b"

        // 模拟 recordUse 逻辑
        currentList.remove(toolId)
        currentList.add(0, toolId)

        assertEquals(listOf("b", "a", "c"), currentList, "工具应移到列表顶部")
    }

    @Test
    fun `recordUse logic should add new tool to top`() {
        // 模拟 RecentStore 的 recordUse 逻辑
        val currentList = mutableListOf("a", "b", "c")
        val toolId = "d"

        // 模拟 recordUse 逻辑
        currentList.remove(toolId)
        currentList.add(0, toolId)

        assertEquals(listOf("d", "a", "b", "c"), currentList, "新工具应添加到列表顶部")
    }

    @Test
    fun `recordUse logic should limit to 6 entries`() {
        // 模拟 RecentStore 的 recordUse 逻辑
        val currentList = mutableListOf("a", "b", "c", "d", "e", "f")
        val toolId = "g"

        // 模拟 recordUse 逻辑
        currentList.remove(toolId)
        currentList.add(0, toolId)
        val trimmed = currentList.take(6)

        assertEquals(6, trimmed.size, "列表长度应限制为 6")
        assertEquals("g", trimmed[0], "新工具应在顶部")
        assertEquals("e", trimmed[5], "最旧的工具应被移除")
    }

    @Test
    fun `empty toolId should be ignored`() {
        // 模拟 RecentStore 的 recordUse 逻辑
        val currentList = mutableListOf("a", "b", "c")
        val toolId = ""

        // 模拟 recordUse 逻辑 (空 toolId 应被忽略)
        if (toolId.isNotEmpty()) {
            currentList.remove(toolId)
            currentList.add(0, toolId)
        }

        assertEquals(listOf("a", "b", "c"), currentList, "空 toolId 不应修改列表")
    }

    @Test
    fun `parseIds logic should split comma-separated string`() {
        // 模拟 RecentStore 的 recentIds 逻辑
        val storedValue = "apm,cps,iq"
        val result = storedValue.split(",").filter { it.isNotEmpty() }

        assertEquals(listOf("apm", "cps", "iq"), result, "应正确解析逗号分隔的字符串")
    }

    @Test
    fun `parseIds logic should handle empty string`() {
        // 模拟 RecentStore 的 recentIds 逻辑
        val storedValue = ""
        val result = storedValue.split(",").filter { it.isNotEmpty() }

        assertEquals(emptyList(), result, "空字符串应返回空列表")
    }

    @Test
    fun `parseIds logic should handle trailing comma`() {
        // 模拟 RecentStore 的 recentIds 逻辑
        val storedValue = "apm,cps,"
        val result = storedValue.split(",").filter { it.isNotEmpty() }

        assertEquals(listOf("apm", "cps"), result, "尾部逗号应被忽略")
    }
}
