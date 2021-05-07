package com.github.gmkornilov.chess_puzzle_book.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.*

@Parcelize
@Serializable
data class Turn(
    val SanNotation: String,
    val IsLastTurn: Boolean,
    val AnswerTurnSanNotation: String,
    val ContinueVariations: List<Turn>,
) : Parcelable {
}