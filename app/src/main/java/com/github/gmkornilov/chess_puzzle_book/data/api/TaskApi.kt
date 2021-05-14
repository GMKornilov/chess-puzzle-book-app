package com.github.gmkornilov.chess_puzzle_book.data.api

import com.github.gmkornilov.chess_puzzle_book.data.model.Task
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskApi {
    @GET("task")
    suspend fun fetchTask(@Query("elo") elo: Int): Response<Task>

    @GET("task/{username}")
    suspend fun startGenerate(
        @Path("username") username: String,
        @Query("last") last: Int
    ): Response<JobInfo>

    @GET("job/{job_id}")
    suspend fun getJobStatus(@Path("job_id") jobId: String): Response<JobStatus>
}