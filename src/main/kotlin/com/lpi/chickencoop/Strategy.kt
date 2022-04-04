package com.lpi.chickencoop


interface Strategy {
    fun nextMove(state: PlayerState): PlayerState
}

class ExchangeEggsAndChickensThenRollDicesStrategy : Strategy {
    override fun nextMove(state: PlayerState): PlayerState = with(state.possibleMoves()) {
        if (contains(PossibleMove.Pause)) {
            state.copy(
                skipsNextTurn = false
            )
        } else if (contains(PossibleMove.ExchangeEggs)) {
            state.applyExchange(Exchange.EggsToChicken)
        } else if (contains(PossibleMove.ExchangeChickens)) {
            state.applyExchange(Exchange.ChickensToHen)
        } else {
            val outcome = diceRollOutcome(rollDices())
            state.applyDiceRollOutcome(outcome)
        }
    }
}

class ExchangeOnlyChickensThenRollDicesStrategy : Strategy {
    override fun nextMove(state: PlayerState): PlayerState = with(state.possibleMoves()) {
        if (contains(PossibleMove.Pause)) {
            state.copy(
                skipsNextTurn = false
            )
        } else if (contains(PossibleMove.ExchangeChickens)) {
            state.applyExchange(Exchange.ChickensToHen)
        } else {
            val outcome = diceRollOutcome(rollDices())
            state.applyDiceRollOutcome(outcome)
        }
    }
}

class ExchangeEggsAndChickensAndHensThenRollDicesStrategy : Strategy {
    override fun nextMove(state: PlayerState): PlayerState = with(state.possibleMoves()) {
        if (contains(PossibleMove.Pause)) {
            state.copy(
                skipsNextTurn = false
            )
        } else if (contains(PossibleMove.BuyCock)) {
            state.applyExchange(Exchange.HensToCock)
        } else if (contains(PossibleMove.ExchangeEggs)) {
            state.applyExchange(Exchange.EggsToChicken)
        } else if (contains(PossibleMove.ExchangeChickens)) {
            state.applyExchange(Exchange.ChickensToHen)
        } else {
            val outcome = diceRollOutcome(rollDices())
            state.applyDiceRollOutcome(outcome)
        }
    }
}

class AlwaysRollDicesStrategy : Strategy {
    override fun nextMove(state: PlayerState): PlayerState = with(state.possibleMoves()) {
        if (contains(PossibleMove.Pause)) {
            state.copy(
                skipsNextTurn = false
            )
        } else {
            val outcome = diceRollOutcome(rollDices())
            state.applyDiceRollOutcome(outcome)
        }
    }
}

class BuyCockAndAlwaysRollDicesStrategy : Strategy {
    override fun nextMove(state: PlayerState): PlayerState = with(state.possibleMoves()) {
        if (contains(PossibleMove.Pause)) {
            state.copy(
                skipsNextTurn = false
            )
        } else if (contains(PossibleMove.BuyCock)) {
            state.applyExchange(Exchange.HensToCock)
        } else {
            val outcome = diceRollOutcome(rollDices())
            state.applyDiceRollOutcome(outcome)
        }
    }
}