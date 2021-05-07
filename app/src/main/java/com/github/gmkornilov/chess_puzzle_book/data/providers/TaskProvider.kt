package com.github.gmkornilov.chess_puzzle_book.data.providers

import android.os.Parcelable
import com.github.gmkornilov.chess_puzzle_book.data.Task

interface TaskProvider: Parcelable {
    suspend fun getNextTask(): Task

    suspend fun hasNext(): Boolean
}