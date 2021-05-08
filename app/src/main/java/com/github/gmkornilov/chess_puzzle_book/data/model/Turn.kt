package com.github.gmkornilov.chess_puzzle_book.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.*

@Parcelize
@Serializable
data class Turn(
        val SanNotation: String,
        val IsLastTurn: Boolean = false,
        val AnswerTurnSanNotation: String = "",
        val ContinueVariations: List<Turn> = emptyList(),
) : Parcelable {
}