package com.lpi.chickencoop

fun main() {
    val simulations = 10000

    val averageTurns1 = ((1..simulations).sumOf { simulateGame(ExchangeEggsAndChickensThenRollDicesStrategy()) } * 1.0 / simulations)
    val averageTurns1a = ((1..simulations).sumOf { simulateGame(ExchangeOnlyChickensThenRollDicesStrategy()) } * 1.0 / simulations)
    val averageTurns2 = ((1..simulations).sumOf { simulateGame(ExchangeEggsAndChickensAndHensThenRollDicesStrategy()) } * 1.0 / simulations)
    val averageTurns3 = ((1..simulations).sumOf { simulateGame(AlwaysRollDicesStrategy()) } * 1.0 / simulations)
    val averageTurns4 = ((1..simulations).sumOf { simulateGame(BuyCockAndAlwaysRollDicesStrategy()) } * 1.0 / simulations)

    println("Average turns in ExchangeEggsAndChickensThenRollDicesStrategy is: $averageTurns1")
    println("Average turns in ExchangeOnlyChickensThenRollDicesStrategy is: $averageTurns1a")
    println("Average turns in ExchangeEggsAndChickensAndHensThenRollDicesStrategy is: $averageTurns2")
    println("Average turns in AlwaysRollDicesStrategy is: $averageTurns3")
    println("Average turns in BuyCockAndAlwaysRollDicesStrategy is: $averageTurns4")

}

fun simulateGame(strategy: Strategy): Int {
    var turns = 0
    var state = PlayerState()
    while (!state.hasWon()) {
        turns += 1
        state = strategy.nextMove(state)
    }
    return turns
}
