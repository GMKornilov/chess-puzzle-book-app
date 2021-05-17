package com.github.gmkornilov.chess_puzzle_book.ui.puzzle_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.gmkornilov.chess_puzzle_book.R
import com.github.gmkornilov.chess_puzzle_book.data.model.Task
import com.github.gmkornilov.chessboard.view.ChessboardView
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MyTaskRecyclerViewAdapter(
    private val values: List<Task>,
    private val callback: (Int) -> Unit,
) : RecyclerView.Adapter<MyTaskRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val whitePlayer: TextView = view.findViewById(R.id.whitePlayer)
        val blackPlayer: TextView = view.findViewById(R.id.blackPlayer)
        val chessboardView: ChessboardView = view.findViewById(R.id.chessboardView2)
        val gameDate: TextView = view.findViewById(R.id.gameDate)
        val eloTextView: TextView = view.findViewById(R.id.eloText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_puzzle_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.whitePlayer.text = item.GameData.WhitePlayer
        holder.blackPlayer.text = item.GameData.BlackPlayer
        holder.chessboardView.fen = item.StartFEN
        holder.chessboardView.isWhite = item.IsWhiteTurn

        val localDate = item.GameData.Date.toLocalDateTime(TimeZone.currentSystemDefault())

        holder.gameDate.text = "%02d.%02d.%04d %02d:%02d".format(
            localDate.dayOfMonth,
            localDate.monthNumber,
            localDate.year,
            localDate.hour,
            localDate.minute
        )

        holder.eloTextView.text = item.TargetElo.toString()
        holder.itemView.setOnClickListener { callback(position) }
    }

    override fun getItemCount(): Int = values.size

}