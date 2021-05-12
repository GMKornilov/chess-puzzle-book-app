package com.github.gmkornilov.chess_puzzle_book.data.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UrlInfo(
    val baseUrl: String
) : Parcelable
