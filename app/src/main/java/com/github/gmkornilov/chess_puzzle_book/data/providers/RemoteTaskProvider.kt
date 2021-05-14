package com.github.gmkornilov.chess_puzzle_book.data.providers

import com.github.gmkornilov.chess_puzzle_book.data.api.TaskApi
import com.github.gmkornilov.chess_puzzle_book.data.model.Task
import com.github.gmkornilov.chess_puzzle_book.data.api.Result
import com.github.gmkornilov.chess_puzzle_book.data.api.TaskNotFound
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Response
import retrofit2.Retrofit

@Parcelize
class RemoteTaskProvider(
    private val baseUrl: String,
) : TaskProvider {
    @IgnoredOnParcel
    private val taskApi: TaskApi = run {
        val contentType = "application/json; charset=utf-8"
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(Json.asConverterFactory(MediaType.parse(contentType)!!))
            .build()
            .create(TaskApi::class.java)
    }

    override suspend fun getNextTask(elo: Int): Result<Task> {
        val response: Response<Task>
        try {
            response = taskApi.fetchTask(elo)
        } catch (t: Exception) {
            return Result.Error(t)
        }
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else {
            Result.Error(TaskNotFound("Task not found"))
        }
    }

    override fun hasNext() = true
}