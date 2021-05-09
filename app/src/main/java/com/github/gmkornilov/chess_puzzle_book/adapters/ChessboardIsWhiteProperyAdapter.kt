package com.github.gmkornilov.chess_puzzle_book.adapters

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.github.gmkornilov.chessboard.view.ChessboardView

object ChessboardIsWhiteProperyAdapter {
    @InverseBindingAdapter(attribute = "whiteTowardsUser")
    @JvmStatic fun getIsWhiteValue(view: ChessboardView): Boolean? {
        return view.isWhite
    }

    @BindingAdapter("whiteTowardsUser")
    @JvmStatic fun setIsWhiteValue(view: ChessboardView, isWhite: Boolean?) {
        if (view.isWhite != isWhite && isWhite != null) {
            view.isWhite = isWhite
        }
    }

    @BindingAdapter("app:whiteTowardsUserAttrChanged")
    @JvmStatic fun setListener(view: ChessboardView, isWhiteAttrChanged: InverseBindingListener) {
        view.addBoardListener(object: ChessboardView.BoardListener {
            override fun onMove(move: String) {
            }

            override fun onFenChanged(newFen: String) {
            }

            override fun onIsWhiteChanged(isWhite: Boolean) {
                isWhiteAttrChanged.onChange()
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