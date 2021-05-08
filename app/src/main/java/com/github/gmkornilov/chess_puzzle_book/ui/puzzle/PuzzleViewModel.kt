package com.github.gmkornilov.chess_puzzle_book.ui.puzzle

import android.os.Debug
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gmkornilov.chess_puzzle_book.data.api.Result
import com.github.gmkornilov.chess_puzzle_book.data.model.Task
import com.github.gmkornilov.chess_puzzle_book.data.providers.TaskProvider
import kotlinx.coroutines.launch

class PuzzleViewModel(
    val taskProvider: TaskProvider,
) : ViewModel() {
    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val task: MutableLiveData<Task?> by lazy {
        MutableLiveData()
    }

    fun getTask() {
        viewModelScope.launch {
            val res = taskProvider.getNextTask(1500)
            when (res) {
                is Result.Success<Task> -> {
                    task.value = res.data
                }
                is Result.Error -> {
                    Log.println(Log.ERROR, "Internet error:", res.exception.message!!)
                }
            }
        }
    }
}