package com.mispec.luomashu

import com.mispec.luomashu.ui.components.QuizQuestion
import org.junit.Test
import kotlin.test.*

/**
 * Unit tests for QuizQuestion data class validation invariants.
 */
class QuizQuestionTest {

    @Test
    fun `valid question with 4 options passes`() {
        val q = QuizQuestion("1+1=?", listOf("2", "3", "4", "5"), 0)
        assertEquals("1+1=?", q.question)
        assertEquals(4, q.options.size)
        assertEquals(0, q.answer)
    }

    @Test
    fun `valid question with 2 options passes`() {
        val q = QuizQuestion("True or false?", listOf("True", "False"), 0)
        assertEquals(2, q.options.size)
    }

    @Test
    fun `valid question with 8 options passes`() {
        val q = QuizQuestion("Pick one", (1..8).map { "Opt$it" }, 0)
        assertEquals(8, q.options.size)
    }

    @Test
    fun `throws when single option less than 2`() {
        assertFailsWith<IllegalArgumentException> {
            QuizQuestion("Bad", listOf("Only"), 0)
        }
    }

    @Test
    fun `throws when more than 8 options`() {
        assertFailsWith<IllegalArgumentException> {
            QuizQuestion("Too many", (1..9).map { "Opt$it" }, 0)
        }
    }

    @Test
    fun `throws when answer index out of bounds`() {
        assertFailsWith<IllegalArgumentException> {
            QuizQuestion("Invalid answer", listOf("A", "B", "C"), 5)
        }
    }

    @Test
    fun `negative answer index throws`() {
        assertFailsWith<IllegalArgumentException> {
            QuizQuestion("Negative answer", listOf("A", "B"), -1)
        }
    }
}
