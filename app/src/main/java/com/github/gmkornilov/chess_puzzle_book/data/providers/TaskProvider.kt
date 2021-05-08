package com.github.gmkornilov.chess_puzzle_book.data.providers

import android.os.Parcelable
import com.github.gmkornilov.chess_puzzle_book.data.model.Task
import com.github.gmkornilov.chess_puzzle_book.data.api.Result

interface TaskProvider : Parcelable {
    suspend fun getNextTask(elo: Int): Result<Task>

    fun hasNext(): Boolean
}