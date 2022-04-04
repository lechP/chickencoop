package com.lpi.chickencoop

import java.lang.RuntimeException
import kotlin.math.max
import kotlin.random.Random

fun PlayerState.applyExchange(exchangeType: Exchange): PlayerState =
    if (skipsNextTurn) throw RuntimeException("Cannot exchange because skips next turn") else
        when (exchangeType) {
            Exchange.EggsToChicken -> if (eggs < 3) throw RuntimeException("illegal exchange") else copy(
                eggs = eggs - 3,
                chickens = chickens + 1
            )
            Exchange.ChickensToHen -> if (chickens < 3) throw RuntimeException("illegal exchange") else copy(
                chickens = chickens - 3,
                hens = hens + 1
            )
            Exchange.HensToCock -> if (hens < 3 || hasCock) throw RuntimeException("illegal exchange") else copy(
                hens = hens - 3,
                hasCock = true
            )
        }

fun PlayerState.applyDiceRollOutcome(outcome: Outcome): PlayerState =
    if (skipsNextTurn) throw RuntimeException("Cannot roll dices because skips next turn") else
        when (outcome) {
            is Outcome.GainPoultry -> copy(
                eggs = eggs + outcome.eggs,
                chickens = chickens + outcome.chickens,
                hens = hens + outcome.hens
            )
            Outcome.LooseAllChickens -> copy(
                chickens = 0,
            )
            Outcome.LooseAllEggs -> copy(
                eggs = 0,
            )
            Outcome.LooseAllHens -> copy(
                hens = 0,
            )
            is Outcome.FoxStealsHens -> if (hasCock) this else
                copy(
                    hens = max(hens - outcome.number, 0),
                    chickens = max(chickens - max(outcome.number - hens, 0), 0),
                    eggs = max(eggs - max(outcome.number - hens - chickens, 0), 0)
                )
            Outcome.PassChicken -> copy(
                chickens = max(chickens - 1, 0)
            )
            Outcome.PassEgg -> copy(
                eggs = max(eggs - 1, 0)
            )
            Outcome.PassHen -> copy(
                hens = max(hens - 1, 0)
            )
            Outcome.SkipNextTurn -> copy(
                skipsNextTurn = true
            )
        }

fun PlayerState.possibleMoves(): Set<PossibleMove> =
    if (skipsNextTurn) {
        setOf(PossibleMove.Pause)
    } else {
        setOfNotNull(
            PossibleMove.RollDices,
            PossibleMove.ExchangeEggs.takeIf { eggs >= 3 },
            PossibleMove.ExchangeChickens.takeIf { chickens >= 3 },
            PossibleMove.BuyCock.takeIf { hens >= 3 && !hasCock },
        )
    }

fun PlayerState.hasWon() = hens >= 9

fun diceRollOutcome(dicesResult: DicesResult): Outcome =
    if (dicesResult.d1 != dicesResult.d2) {
        gainPoultryOtucome(dicesResult.d1) + gainPoultryOtucome(dicesResult.d2)
    } else {
        when (dicesResult.d1.value) {
            1 -> Outcome.PassEgg
            2 -> Outcome.PassChicken
            3 -> Outcome.PassHen
            4 -> Outcome.LooseAllEggs
            5 -> when (randomDiceRoll()) {
                in 1..3 -> Outcome.SkipNextTurn
                in 4..5 -> Outcome.LooseAllChickens
                6 -> Outcome.LooseAllHens
                else -> throw RuntimeException("cannot happen")
            }
            6 -> Outcome.FoxStealsHens(rollDice().value)
            else -> throw RuntimeException("cannot happen")
        }
    }


fun gainPoultryOtucome(dice: DiceResult): Outcome.GainPoultry = when (dice.value) {
    in 1..3 -> Outcome.GainPoultry(eggs = 1)
    in 4..5 -> Outcome.GainPoultry(chickens = 1)
    6 -> Outcome.GainPoultry(hens = 1)
    else -> throw RuntimeException("cannot happen")
}

fun randomDiceRoll(): Int = Random.nextInt(1, 7)

fun rollDice(): DiceResult = DiceResult(randomDiceRoll())

fun rollDices() = DicesResult(d1 = rollDice(), d2 = rollDice())