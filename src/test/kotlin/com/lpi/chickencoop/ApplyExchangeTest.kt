package com.lpi.chickencoop

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class ApplyExchangeTest {

    @Test
    fun `applyExchange should fail when skipsNextTurn`() {
        val state = PlayerState(
            eggs = 7,
            skipsNextTurn = true
        )

        assertFailsWith<RuntimeException> {
            state.applyExchange(Exchange.EggsToChicken)
        }
    }

    @Test
    fun `applyExchange should exchange eggs to chicken`() {
        val state = PlayerState(
            eggs = 5,
            chickens = 2
        )

        val result = state.applyExchange(Exchange.EggsToChicken)

        assertEquals(2, result.eggs)
        assertEquals(3, result.chickens)
    }

    @Test
    fun `applyExchange should not exchange not enough eggs`() {
        val state = PlayerState(
            eggs = 2,
        )

        assertFailsWith<RuntimeException> {
            state.applyExchange(Exchange.EggsToChicken)
        }
    }

    @Test
    fun `applyExchange should exchange chickens to hen`() {
        val state = PlayerState(
            chickens = 5,
            hens = 2
        )

        val result = state.applyExchange(Exchange.ChickensToHen)

        assertEquals(2, result.chickens)
        assertEquals(3, result.hens)
    }

    @Test
    fun `applyExchange should not exchange not enough chickens`() {
        val state = PlayerState(
            chickens = 2,
        )

        assertFailsWith<RuntimeException> {
            state.applyExchange(Exchange.ChickensToHen)
        }
    }

    @Test
    fun `applyExchange should exchange hens to cock`() {
        val state = PlayerState(
            hens = 4,
        )

        val result = state.applyExchange(Exchange.HensToCock)

        assertEquals(1, result.hens)
        assertTrue(result.hasCock)
    }

    @Test
    fun `applyExchange should not exchange not enough hens`() {
        val state = PlayerState(
            hens = 2,
        )

        assertFailsWith<RuntimeException> {
            state.applyExchange(Exchange.HensToCock)
        }
    }

}