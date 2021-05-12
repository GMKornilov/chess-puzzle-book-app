package com.github.gmkornilov.chess_puzzle_book.data.api

import android.os.Parcelable
import com.github.gmkornilov.chess_puzzle_book.data.model.Task
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoadedTasks(val tasks: List<Task>) : Parcelable
