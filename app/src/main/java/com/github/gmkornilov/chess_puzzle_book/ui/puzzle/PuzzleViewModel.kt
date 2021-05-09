package com.github.gmkornilov.chess_puzzle_book.ui.puzzle

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gmkornilov.chess_puzzle_book.data.Event
import com.github.gmkornilov.chess_puzzle_book.data.api.Result
import com.github.gmkornilov.chess_puzzle_book.data.model.Task
import com.github.gmkornilov.chess_puzzle_book.data.model.Turn
import com.github.gmkornilov.chess_puzzle_book.data.providers.TaskProvider
import com.github.gmkornilov.chessboard.view.ChessboardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PuzzleViewModel(
    val taskProvider: TaskProvider,
) : ViewModel(), ChessboardView.BoardListener {
    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val task: MutableLiveData<Task?> by lazy {
        MutableLiveData()
    }

    val taskSolved = MutableLiveData<Boolean>()

    val undoEvent = MutableLiveData<Event<Unit>>()

    val taskStartFen = MutableLiveData<String>()

    val legalTurns = MutableLiveData<List<Turn>>()

    val isWhiteTurn = MutableLiveData<Boolean>()

    val targetElo = MutableLiveData<Int>()

    val botAnswerTurn = MutableLiveData<String>()

    init {
        getTask()
    }

    fun nextClicked(v: View?) {
        v ?: return
        getTask()
    }

    fun getTask() = viewModelScope.launch {
        isLoading.postValue(true)
        val res = withContext(Dispatchers.IO) {
            taskProvider.getNextTask(1500)
        }
        when (res) {
            is Result.Success<Task> -> {
                Log.println(Log.DEBUG, "Task json", res.data.toString())
                task.postValue(res.data)

                taskStartFen.postValue(res.data.StartFEN)
                legalTurns.postValue(res.data.FirstPossibleTurns)
                isWhiteTurn.postValue(res.data.IsWhiteTurn)
                targetElo.postValue(res.data.TargetElo)
                taskSolved.postValue(false)
            }
            is Result.Error -> {
                Log.println(Log.ERROR, "Internet error", res.exception.message!!)
            }
        }
        isLoading.postValue(false)
    }

    // region chessboard events

    // view model sets it by itself, no need to handle this
    override fun onAllowOpponentMovesChanged(allowOpponentMovesChanged: Boolean) = Unit

    // vm only needs to check for valid moves, no need to handle check
    override fun onCheck(isWhiteChecked: Boolean) = Unit

    // vm only needs to check for valid moves, no need to handle checkmate
    override fun onCheckmate(whiteLost: Boolean) = Unit

    // vm sets fen by itself, no need to handle that
    override fun onFenChanged(newFen: String) = Unit

    // vm sets isWhite by itself, no need to handle that
    override fun onIsWhiteChanged(isWhite: Boolean) = Unit

    override fun onMove(move: String) {
        // if incoming move is bot move, we need to pass the turn back to player
        if (isWhiteTurn.value != task.value?.IsWhiteTurn) {
            isWhiteTurn.value = isWhiteTurn.value?.not()
            return
        }

        val moveToFind = if (move.contains("#") || move.contains("+")) {
            move.dropLast(1)
        } else {
            move
        }

        val turn = legalTurns.value?.find { it.SanNotation == moveToFind }
        if (turn == null) {
            undoEvent.postValue(Event(Unit))
            return
        }
        if (turn.IsLastTurn) {
            taskSolved.value = true
            return
        }

        botAnswerTurn.postValue(turn.AnswerTurnSanNotation)
        legalTurns.postValue(turn.ContinueVariations)

        isWhiteTurn.value = isWhiteTurn.value?.not()
    }

    // vm doesn't need to handle that
    override fun onStalemate() = Unit

    // and that (I only extend this for one method)
    override fun onUndo() = Unit

    // endregion
}