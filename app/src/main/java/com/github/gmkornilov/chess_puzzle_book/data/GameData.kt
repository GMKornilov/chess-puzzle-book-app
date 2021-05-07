package com.github.gmkornilov.chess_puzzle_book.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.datetime.*

@Serializable
data class GameData(
        val WhitePlayer: String,
        val BlackPlayer: String,
        val Date: LocalDateTime,
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!.toLocalDateTime()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(WhitePlayer)
                parcel.writeString(BlackPlayer)
                parcel.writeString(Date.toString())
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<GameData> {
                override fun createFromParcel(parcel: Parcel): GameData {
                        return GameData(parcel)
                }

                override fun newArray(size: Int): Array<GameData?> {
                        return arrayOfNulls(size)
                }
        }

}