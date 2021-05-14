package com.github.gmkornilov.chess_puzzle_book.ui.info

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.gmkornilov.chess_puzzle_book.R
import com.github.gmkornilov.chess_puzzle_book.data.api.TaskApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class InfoViewModelFactory(
    private val resources: Resources
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val taskApi: TaskApi = run {
            val baseUrl = resources.getString(R.string.base_url)
            val contentType = "application/json; charset=utf-8"
            Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(Json.asConverterFactory(MediaType.parse(contentType)!!))
                .build()
                .create(TaskApi::class.java)
        }
        return InfoViewModel(taskApi) as T
    }
}