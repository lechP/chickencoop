package com.lpi.chickencoop

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class TurnOutcomeTest {

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

    @Test
    fun `applyDiceRollOutcome should fail when skipsNextTurn`() {
        val state = PlayerState(
            skipsNextTurn = true
        )

        assertFailsWith<RuntimeException> {
            state.applyDiceRollOutcome(Outcome.PassEgg)
        }
    }

    @Test
    fun `applyDiceRollOutcome should apply pause`() {
        val state = PlayerState()

        val result = state.applyDiceRollOutcome(Outcome.SkipNextTurn)

        assertTrue(result.skipsNextTurn)
    }

    @Test
    fun `applyDiceRollOutcome should apply PassEgg`() {
        val state = PlayerState(
            eggs = 1
        )

        val result = state.applyDiceRollOutcome(Outcome.PassEgg)

        assertEquals(0, result.eggs)
    }

    @Test
    fun `applyDiceRollOutcome should apply PassEgg and handle 0 eggs`() {
        val state = PlayerState(
            eggs = 0
        )

        val result = state.applyDiceRollOutcome(Outcome.PassEgg)

        assertEquals(0, result.eggs)
    }

    @Test
    fun `applyDiceRollOutcome should apply PassChicken`() {
        val state = PlayerState(
            chickens = 11
        )

        val result = state.applyDiceRollOutcome(Outcome.PassChicken)

        assertEquals(10, result.chickens)
    }

    @Test
    fun `applyDiceRollOutcome should apply PassChicken and handle 0 chickens`() {
        val state = PlayerState(
            chickens = 0
        )

        val result = state.applyDiceRollOutcome(Outcome.PassChicken)

        assertEquals(0, result.chickens)
    }

    @Test
    fun `applyDiceRollOutcome should apply PassHen`() {
        val state = PlayerState(
            hens = 7
        )

        val result = state.applyDiceRollOutcome(Outcome.PassHen)

        assertEquals(6, result.hens)
    }

    @Test
    fun `applyDiceRollOutcome should apply PassHen and handle 0 hens`() {
        val state = PlayerState(
            hens = 0
        )

        val result = state.applyDiceRollOutcome(Outcome.PassHen)

        assertEquals(0, result.hens)
    }

    @Test
    fun `applyDiceRollOutcome should apply FoxStealsHens with enough hens`() {
        val state = PlayerState(
            hens = 6
        )

        val result = state.applyDiceRollOutcome(Outcome.FoxStealsHens(4))

        assertEquals(2, result.hens)
    }

    @Test
    fun `applyDiceRollOutcome should apply FoxStealsHens with not enough hens`() {
        val state = PlayerState(
            hens = 2,
            chickens = 2,
            eggs = 5
        )

        val result = state.applyDiceRollOutcome(Outcome.FoxStealsHens(5))

        assertEquals(0, result.hens)
        assertEquals(0, result.chickens)
        assertEquals(4, result.eggs)
    }

    @Test
    fun `applyDiceRollOutcome should apply FoxStealsHens with cock`() {
        val state = PlayerState(
            hens = 2,
            chickens = 2,
            eggs = 5,
            hasCock = true
        )

        val result = state.applyDiceRollOutcome(Outcome.FoxStealsHens(3))

        assertEquals(state, result)
    }

    @Test
    fun `applyDiceRollOutcome should apply GainPoultry`() {
        val state = PlayerState(
            hens = 2,
            chickens = 2,
            eggs = 5,
        )

        val result = state.applyDiceRollOutcome(Outcome.GainPoultry(eggs = 1, hens = 1))

        assertEquals(state.hens + 1, result.hens)
        assertEquals(state.chickens, result.chickens)
        assertEquals(state.eggs + 1, result.eggs)
    }

    @Test
    fun `applyDiceRollOutcome should apply LooseAllEggs`() {
        val state = PlayerState(
            hens = 2,
            chickens = 2,
            eggs = 5,
        )

        val result = state.applyDiceRollOutcome(Outcome.LooseAllEggs)

        assertEquals(state.hens, result.hens)
        assertEquals(state.chickens, result.chickens)
        assertEquals(0, result.eggs)
    }


    @Test
    fun `applyDiceRollOutcome should apply LooseAllChickens`() {
        val state = PlayerState(
            hens = 2,
            chickens = 2,
            eggs = 5,
        )

        val result = state.applyDiceRollOutcome(Outcome.LooseAllChickens)

        assertEquals(state.hens, result.hens)
        assertEquals(0, result.chickens)
        assertEquals(state.eggs, result.eggs)
    }


    @Test
    fun `applyDiceRollOutcome should apply LooseAllHens`() {
        val state = PlayerState(
            hens = 2,
            chickens = 2,
            eggs = 5,
        )

        val result = state.applyDiceRollOutcome(Outcome.LooseAllHens)

        assertEquals(0, result.hens)
        assertEquals(state.chickens, result.chickens)
        assertEquals(state.eggs, result.eggs)
    }

}