package com.github.gmkornilov.chess_puzzle_book.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.*

@Parcelize
@Serializable
data class Turn(
        @SerialName("san_notation")
        val SanNotation: String,
        @SerialName("is_last_turn")
        val IsLastTurn: Boolean = false,
        @SerialName("answer_turn_san_notation")
        val AnswerTurnSanNotation: String = "",
        @SerialName("continue_variations")
        val ContinueVariations: List<Turn> = emptyList(),
) : Parcelable {
}