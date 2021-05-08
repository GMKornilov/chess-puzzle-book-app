package com.github.gmkornilov.chess_puzzle_book.data.api

import com.github.gmkornilov.chess_puzzle_book.data.model.Task
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobStatus(
    @SerialName("done")
    val done: Boolean,
    @SerialName("progress")
    val progress: Float = 1.0f,
    @SerialName("result")
    val result: List<Task> = emptyList(),
    @SerialName("error")
    val error: String = "",
)
