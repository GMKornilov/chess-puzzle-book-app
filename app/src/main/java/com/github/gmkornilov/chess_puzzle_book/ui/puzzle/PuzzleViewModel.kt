package com.github.gmkornilov.chess_puzzle_book.ui.puzzle

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gmkornilov.chess_puzzle_book.data.Event
import com.github.gmkornilov.chess_puzzle_book.data.api.Result
import com.github.gmkornilov.chess_puzzle_book.data.model.EloUtils
import com.github.gmkornilov.chess_puzzle_book.data.model.Task
import com.github.gmkornilov.chess_puzzle_book.data.model.Turn
import com.github.gmkornilov.chess_puzzle_book.data.providers.TaskProvider
import com.github.gmkornilov.chessboard.view.ChessboardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class PuzzleViewModel(
    private val taskProvider: TaskProvider,
) : ViewModel(), ChessboardView.BoardListener {
    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val task: MutableLiveData<Task?> by lazy {
        MutableLiveData()
    }

    val elo = MutableLiveData<Int>()
    val eloDiff = MutableLiveData<Int>()

    val fenLoadedEvent = MutableLiveData<Event<String>>()
    val currentFen = MutableLiveData<String?>()

    val taskDone = MutableLiveData<Boolean>()
    private val legalTurns = MutableLiveData<List<Turn>>()

    private val lastMoveHinted = MutableLiveData<Boolean>()
    val lastMoveCorrect = MutableLiveData<Boolean>()
    val lastMoveWrong = MutableLiveData<Boolean>()
    private val correctMoves = MutableLiveData<Int>()

    val turnEvent = MutableLiveData<Event<String>>()

    val undoEvent = MutableLiveData<Event<Unit>>()
    private val isWhiteTurn = MutableLiveData<Boolean>()

    val exceptionEvent = MutableLiveData<Event<Exception>>()

    init {
        getTask()
    }

    fun nextClicked(v: View?) {
        v ?: return
        getTask()
    }

    fun hintMove(v : View?)  {
        v ?: return
        val turns = legalTurns.value ?: return
        if (turns.isEmpty()) {
            return
        }
        val turn = turns.first()
        lastMoveHinted.value = true
        turnEvent.value = Event(turn.SanNotation)
    }

    private fun getTask() = viewModelScope.launch {
        currentFen.postValue(null)
        correctMoves.postValue(0)
        lastMoveWrong.postValue(false)
        lastMoveCorrect.postValue(false)
        lastMoveHinted.postValue(false)
        isLoading.postValue(true)
        val res = withContext(Dispatchers.IO) {
            //taskProvider.getNextTask(1500)
            taskProvider.getNextTask(EloUtils.elo!!)
        }
        when (res) {
            is Result.Success<Task> -> {
                Log.println(Log.DEBUG, "Task json", res.data.toString())
                task.postValue(res.data)

                fenLoadedEvent.postValue(Event(res.data.StartFEN))
                legalTurns.postValue(res.data.FirstPossibleTurns)
                isWhiteTurn.postValue(res.data.IsWhiteTurn)
                taskDone.postValue(false)
            }
            is Result.Error -> {
                Log.println(Log.ERROR, "Internet error", res.exception.message!!)
                exceptionEvent.postValue(Event(res.exception))
            }
        }
        isLoading.postValue(false)
    }

    private fun calcElo(percent: Float) {
        val curTask = task.value ?: return
        val curElo = elo.value ?: EloUtils.elo!!
        val eloDelta = EloUtils.estimateDiffElo(curElo, curTask.TargetElo, percent)
        eloDiff.value = eloDelta
        elo.value = curElo + eloDelta
        EloUtils.elo = elo.value!!
    }

    // region chessboard events

    override fun onMove(move: String) {
        // if incoming move is bot move, we need to pass the turn back to player
        if (isWhiteTurn.value != task.value?.IsWhiteTurn) {
            isWhiteTurn.value = isWhiteTurn.value?.not()
            return
        }

        val turns = legalTurns.value ?: return
        val correctTurns = correctMoves.value ?: return

        val moveToFind = if (move.contains("#") || move.contains("+")) {
            move.dropLast(1)
        } else {
            move
        }


        val turn = turns.find { it.SanNotation == moveToFind }
        if (turn == null) {
            lastMoveCorrect.value = false
            lastMoveWrong.value = true

            if (taskDone.value != true) {
                val minDepth = turns.minOf { turn -> turn.minDepth }
                val percent = correctTurns.toFloat() / (correctTurns + minDepth).toFloat()
                calcElo(percent)
            }

            taskDone.value = true
            undoEvent.value = Event(Unit)
            return
        }

        correctMoves.value = correctMoves.value?.plus(1)
        if (lastMoveHinted.value != true) {
            lastMoveCorrect.value = true
            lastMoveWrong.value = false
        } else {
            lastMoveHinted.value = false
        }

        if (turn.IsLastTurn) {
            if (taskDone.value != true) {
                calcElo(1.0f)
            }
            legalTurns.value = emptyList()
            taskDone.value = true
            return
        }

        legalTurns.value = turn.ContinueVariations
        isWhiteTurn.value = isWhiteTurn.value?.not()
        turnEvent.value = Event(turn.AnswerTurnSanNotation)
    }

    // view model sets it by itself, no need to handle this
    override fun onAllowOpponentMovesChanged(allowOpponentMovesChanged: Boolean) = Unit

    // vm only needs to check for valid moves, no need to handle check
    override fun onCheck(isWhiteChecked: Boolean) = Unit

    // vm only needs to check for valid moves, no need to handle checkmate
    override fun onCheckmate(whiteLost: Boolean) = Unit

    override fun onFenChanged(newFen: String) {
        currentFen.value = newFen
    }

    // vm sets isWhite by itself, no need to handle that
    override fun onIsWhiteChanged(isWhite: Boolean) = Unit

    // vm doesn't need to handle that
    override fun onStalemate() = Unit

    // and that (I only extend this for one method)
    override fun onUndo() = Unit

    // endregion
}