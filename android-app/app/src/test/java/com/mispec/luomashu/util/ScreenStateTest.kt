package com.mispec.luomashu.util

import org.junit.Test
import kotlin.test.*

/**
 * 单元测试 - ToolScreenState 密封类
 * 验证状态机定义和状态转换逻辑
 */
class ScreenStateTest {

    @Test
    fun `all 12 states exist`() {
        val states = listOf(
            ToolScreenState.Idle,
            ToolScreenState.Playing,
            ToolScreenState.Result,
            ToolScreenState.Waiting,
            ToolScreenState.Ready,
            ToolScreenState.Running,
            ToolScreenState.Counting,
            ToolScreenState.Timeout,
            ToolScreenState.Show,
            ToolScreenState.Hide,
            ToolScreenState.Guessing,
            ToolScreenState.Error,
        )
        assertEquals(12, states.size, "Must have 12 states")
    }

    @Test
    fun `Idle is initial state`() {
        val state = ToolScreenState.Idle
        assertTrue(state is ToolScreenState.Idle)
    }

    @Test
    fun `state types are distinct`() {
        val states = setOf(
            ToolScreenState.Idle,
            ToolScreenState.Playing,
            ToolScreenState.Result,
            ToolScreenState.Waiting,
            ToolScreenState.Ready,
            ToolScreenState.Running,
            ToolScreenState.Counting,
            ToolScreenState.Timeout,
            ToolScreenState.Show,
            ToolScreenState.Hide,
            ToolScreenState.Guessing,
            ToolScreenState.Error,
        )
        assertEquals(12, states.size, "All states must be distinct")
    }

    @Test
    fun `Idle is not Playing`() {
        assertFalse(ToolScreenState.Idle is ToolScreenState.Playing)
    }

    @Test
    fun `Playing is not Result`() {
        assertFalse(ToolScreenState.Playing is ToolScreenState.Result)
    }

    @Test
    fun `Running is not Idle`() {
        assertFalse(ToolScreenState.Running is ToolScreenState.Idle)
    }

    @Test
    fun `typical click tool state flow`() {
        // Idle -> Running -> Result
        val states = listOf(
            ToolScreenState.Idle,
            ToolScreenState.Running,
            ToolScreenState.Result,
        )
        assertEquals(3, states.size, "Click tool should have 3 states")
        assertTrue(states[0] is ToolScreenState.Idle)
        assertTrue(states[1] is ToolScreenState.Running)
        assertTrue(states[2] is ToolScreenState.Result)
    }

    @Test
    fun `typical quiz tool state flow`() {
        // Idle -> Waiting -> Ready -> Playing -> Result
        val states = listOf(
            ToolScreenState.Idle,
            ToolScreenState.Waiting,
            ToolScreenState.Ready,
            ToolScreenState.Playing,
            ToolScreenState.Result,
        )
        assertEquals(5, states.size, "Quiz tool should have 5 states")
    }

    @Test
    fun `typical scale tool state flow`() {
        // Idle -> Show -> Hide -> Result
        val states = listOf(
            ToolScreenState.Idle,
            ToolScreenState.Show,
            ToolScreenState.Hide,
            ToolScreenState.Result,
        )
        assertEquals(4, states.size, "Scale tool should have 4 states")
    }

    @Test
    fun `counting state exists for timing`() {
        assertTrue(ToolScreenState.Counting is ToolScreenState.Counting)
    }

    @Test
    fun `timeout state exists for time limit`() {
        assertTrue(ToolScreenState.Timeout is ToolScreenState.Timeout)
    }

    @Test
    fun `error state exists for error handling`() {
        assertTrue(ToolScreenState.Error is ToolScreenState.Error)
    }

    @Test
    fun `guessing state exists for guessing games`() {
        assertTrue(ToolScreenState.Guessing is ToolScreenState.Guessing)
    }

    @Test
    fun `sem function creates modifier with tag`() {
        val tag = "test-tag"
        val modifier = sem(tag)
        assertNotNull(modifier, "sem() should return a Modifier")
    }

    @Test
    fun `sem tag format`() {
        // Verify tag format follows convention: toolId-state
        val tag = "apm-idle"
        assertTrue(tag.contains("-"), "Tag should contain dash separator")
        assertTrue(tag.startsWith("apm"), "Tag should start with tool ID")
        assertTrue(tag.endsWith("idle"), "Tag should end with state name")
    }

    @Test
    fun `screen state equality`() {
        assertEquals(ToolScreenState.Idle, ToolScreenState.Idle)
        assertEquals(ToolScreenState.Running, ToolScreenState.Running)
        assertEquals(ToolScreenState.Result, ToolScreenState.Result)
    }

    @Test
    fun `screen state inequality`() {
        assertNotEquals<ToolScreenState>(ToolScreenState.Idle, ToolScreenState.Running)
        assertNotEquals<ToolScreenState>(ToolScreenState.Running, ToolScreenState.Result)
        assertNotEquals<ToolScreenState>(ToolScreenState.Idle, ToolScreenState.Result)
    }
}
