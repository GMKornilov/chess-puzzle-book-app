package com.github.gmkornilov.chess_puzzle_book.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobInfo(
    @SerialName("job_id")
    val jobId: String,
)