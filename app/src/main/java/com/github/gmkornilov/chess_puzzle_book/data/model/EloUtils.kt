package com.github.gmkornilov.chess_puzzle_book.data.model

import kotlin.math.pow

object EloUtils {
    var elo: Int? = null

    private fun eloCoeff(elo: Int) =
        when {
            elo >= 2400 -> {
                10
            }
            elo >= 2000 -> {
                20
            }
            else -> {
                40
            }
        }
    fun estimateDiffElo(currentElo: Int, taskElo: Int, percent: Float): Int {
        val expectedPercent = 1.0f / (1 + 10f.pow((taskElo - currentElo) / 400))
        return (eloCoeff(currentElo) * (percent - expectedPercent)).toInt()
    }
}