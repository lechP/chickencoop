package com.lpi.chickencoop

data class PlayerState(
    val eggs: Int = 0,
    val chickens: Int = 0,
    val hens: Int = 0,
    val hasCock: Boolean = false,
    val skipsNextTurn: Boolean = false
)

@JvmInline
value class DiceResult(val value: Int) {
    init {
        require(value in 1..6)
    }
}

data class DicesResult(val d1: DiceResult, val d2: DiceResult)

sealed class Exchange {
    object EggsToChicken : Exchange()
    object ChickensToHen : Exchange()
    object HensToCock : Exchange()
}

sealed class Outcome {
    object PassEgg : Outcome()
    object PassChicken : Outcome()
    object PassHen : Outcome()
    object LooseAllEggs : Outcome()
    object LooseAllChickens : Outcome()
    object LooseAllHens : Outcome()
    data class FoxStealsHens(val number: Int) : Outcome()
    object SkipNextTurn : Outcome()
    data class GainPoultry(
        val eggs: Int = 0,
        val chickens: Int = 0,
        val hens: Int = 0,
    ) : Outcome() {
        operator fun plus(other: GainPoultry) = GainPoultry(
            eggs = eggs + other.eggs,
            chickens = chickens + other.chickens,
            hens = hens + other.hens,
        )
    }
}

enum class PossibleMove {
    ExchangeEggs, ExchangeChickens, BuyCock, RollDices, Pause
}