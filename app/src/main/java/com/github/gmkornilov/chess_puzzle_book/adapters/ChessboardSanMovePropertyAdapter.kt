package com.github.gmkornilov.chess_puzzle_book.adapters

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.github.gmkornilov.chessboard.view.ChessboardView

object ChessboardSanMovePropertyAdapter {
    @InverseBindingAdapter(attribute = "lastMove")
    @JvmStatic fun getFenValue(view: ChessboardView): String? {
        return view.lastMove
    }

    @BindingAdapter("lastMove")
    @JvmStatic fun setFenValue(view: ChessboardView, lastMove: String?) {
        if (view.lastMove != lastMove) {
            view.lastMove = lastMove
        }
    }

    @BindingAdapter("app:lastMoveAttrChanged")
    @JvmStatic fun setListener(view: ChessboardView, fenAttrChanged: InverseBindingListener) {
        view.addBoardListener(object: ChessboardView.BoardListener {
            override fun onMove(move: String) {
                fenAttrChanged.onChange()
            }

            override fun onFenChanged(newFen: String) {
            }

            override fun onUndo() {
            }

            override fun onCheck(isWhiteChecked: Boolean) {
            }

            override fun onCheckmate(whiteLost: Boolean) {
            }

            override fun onStalemate() {
            }

            override fun onIsWhiteChanged(isWhite: Boolean) {

            }

            override fun onAllowOpponentMovesChanged(allowOpponentMovesChanged: Boolean) {
            }
        })
    }
}