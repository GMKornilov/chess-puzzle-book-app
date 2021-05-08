package com.github.gmkornilov.chess_puzzle_book.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Task(
        val StartFEN: String,
        val FirstPossibleTurns: List<Turn>,
        val IsWhiteTurn: Boolean,
        val GameData: GameData,
        val TargetElo: Int,
) : Parcelable {
}