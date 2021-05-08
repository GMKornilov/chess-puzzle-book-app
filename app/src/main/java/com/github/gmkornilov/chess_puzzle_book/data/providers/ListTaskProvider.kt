package com.github.gmkornilov.chess_puzzle_book.data.providers

import com.github.gmkornilov.chess_puzzle_book.data.model.Task
import com.github.gmkornilov.chess_puzzle_book.data.api.Result
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.lang.IndexOutOfBoundsException

@Parcelize
class ListTaskProvider(
    private val tasks: List<Task>,
    private var taskIndex: Int,
) : TaskProvider {

    override suspend fun getNextTask(elo: Int): Result<Task> {
        if (!hasNext()) {
            return Result.Error(IndexOutOfBoundsException())
        }
        return Result.Success(tasks[taskIndex++])
    }

    override fun hasNext() = taskIndex < tasks.size
}