package com.github.gmkornilov.chess_puzzle_book.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Task(
    @SerialName("start_fen")
    val StartFEN: String,
    @SerialName("first_possible_turns")
    val FirstPossibleTurns: List<Turn>,
    @SerialName("is_white_turn")
    val IsWhiteTurn: Boolean,
    @SerialName("game_data")
    val GameData: GameData,
    @SerialName("target_elo")
    val TargetElo: Int,
) : Parcelable