package com.github.gmkornilov.chess_puzzle_book.ui.puzzle

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gmkornilov.chess_puzzle_book.data.api.Result
import com.github.gmkornilov.chess_puzzle_book.data.model.Task
import com.github.gmkornilov.chess_puzzle_book.data.providers.TaskProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PuzzleViewModel(
    val taskProvider: TaskProvider,
) : ViewModel() {
    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val task: MutableLiveData<Task?> by lazy {
        MutableLiveData()
    }

    val fen = MutableLiveData<String>()

    init {
        getTask()
    }

    fun getTask() = viewModelScope.launch {
        val res = withContext(Dispatchers.Default) {
            taskProvider.getNextTask(1500)
        }
        when (res) {
            is Result.Success<Task> -> {
                Log.println(Log.DEBUG, "Task json", res.data.toString())
                task.postValue(res.data)
                fen.postValue(res.data.StartFEN)
            }
            is Result.Error -> {
                Log.println(Log.ERROR, "Internet error", res.exception.message!!)
            }
        }
    }

}