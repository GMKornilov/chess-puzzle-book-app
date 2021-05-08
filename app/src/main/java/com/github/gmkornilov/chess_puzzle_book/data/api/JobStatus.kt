package com.github.gmkornilov.chess_puzzle_book.data.api

import com.github.gmkornilov.chess_puzzle_book.data.model.Task
import kotlinx.serialization.Serializable

@Serializable
data class JobStatus(
    val done: Boolean,
    val progress: Float = 1.0f,
    val result: List<Task> = emptyList(),
    val error: String = "",
)
