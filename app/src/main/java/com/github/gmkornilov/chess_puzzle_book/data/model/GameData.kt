package com.github.gmkornilov.chess_puzzle_book.data.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GameData(
    @SerialName("white_player")
    val WhitePlayer: String,
    @SerialName("black_player")
    val BlackPlayer: String,
    @SerialName("date")
    val Date: Instant,
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!.toInstant()
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