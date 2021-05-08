package com.github.gmkornilov.chess_puzzle_book.ui.puzzle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.gmkornilov.chess_puzzle_book.data.providers.TaskProvider

class PuzzleFragmentViewModelFactory(
    val taskProvider: TaskProvider,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PuzzleViewModel(taskProvider) as T
    }
}