package com.github.gmkornilov.chess_puzzle_book.ui.puzzle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.gmkornilov.chess_puzzle_book.data.model.Task

class PuzzleViewModel : ViewModel() {
    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val task: MutableLiveData<Task?> by lazy {
        MutableLiveData()
    }
}