package com.github.gmkornilov.chess_puzzle_book.data.api

import java.lang.Error
import java.lang.Exception

sealed class Result<out T: Any> {
    data class Success<out T : Any>(val data: T): Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}