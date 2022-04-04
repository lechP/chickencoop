package com.lpi.chickencoop

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class ApplyDiceRollOutcomeTest {

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