package com.github.gmkornilov.chess_puzzle_book.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Task(
        val StartFEN: String,
        val FirstPossibleTurns: List<Turn>,
) : Parcelable {
}