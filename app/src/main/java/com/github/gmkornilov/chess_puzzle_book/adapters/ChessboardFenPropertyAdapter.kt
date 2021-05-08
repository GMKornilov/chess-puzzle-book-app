package com.github.gmkornilov.chess_puzzle_book.adapters

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.github.gmkornilov.chessboard.view.ChessboardView

object ChessboardFenPropertyAdapter {
    @InverseBindingAdapter(attribute = "fen")
    @JvmStatic fun getFenValue(view: ChessboardView): String? {
        return view.fen
    }

    @BindingAdapter("fen")
    @JvmStatic fun setFenValue(view: ChessboardView, fen: String?) {
        if (view.fen != fen) {
            view.fen = fen
        }
    }

    @BindingAdapter("app:fenAttrChanged")
    @JvmStatic fun setListener(view: ChessboardView, fenAttrChanged: InverseBindingListener) {
        view.addBoardListener(object: ChessboardView.BoardListener {
            override fun onMove(move: String) {
            }

            override fun onFenChanged(newFen: String) {
                fenAttrChanged.onChange()
            }

            override fun onIsWhiteChanged(isWhite: Boolean) {
            }

            override fun onUndo() {
            }

            override fun onAllowOpponentMovesChanged(allowOpponentMovesChanged: Boolean) {
            }

            override fun onCheck(isWhiteChecked: Boolean) {
            }

            override fun onCheckmate(whiteLost: Boolean) {
            }

            override fun onStalemate() {
            }
        })
    }
}