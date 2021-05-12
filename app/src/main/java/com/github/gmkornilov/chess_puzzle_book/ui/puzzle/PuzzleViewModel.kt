package com.github.gmkornilov.chess_puzzle_book.ui.puzzle

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
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
    private val _isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val isLoading: LiveData<Boolean> = _isLoading

    private val _task: MutableLiveData<Task?> by lazy {
        MutableLiveData()
    }
    val task: LiveData<Task?> = _task


    val _elo = MutableLiveData<Int>()
    val elo: LiveData<Int> = _elo

    private val _eloDiff = MutableLiveData<Int>()
    val eloDiff: LiveData<Int> = _eloDiff


    private val _fenLoadedEvent = MutableLiveData<Event<String>>()
    val fenLoadedEvent: LiveData<Event<String>> = _fenLoadedEvent

    private val _currentFen = MutableLiveData<String?>()
    val currentFen: LiveData<String?> = _currentFen


    private val _taskDone = MutableLiveData<Boolean>()
    val taskDone: LiveData<Boolean> = _taskDone

    private val legalTurns = MutableLiveData<List<Turn>>()

    private val lastMoveHinted = MutableLiveData<Boolean>()


    private val _lastMoveCorrect = MutableLiveData<Boolean>()
    val lastMoveCorrect: LiveData<Boolean> = _lastMoveCorrect

    private val _lastMoveWrong = MutableLiveData<Boolean>()
    val lastMoveWrong: LiveData<Boolean> = _lastMoveWrong

    private val correctMoves = MutableLiveData<Int>()


    private val _turnEvent = MutableLiveData<Event<String>>()
    val turnEvent: LiveData<Event<String>> = _turnEvent


    private val _undoEvent = MutableLiveData<Event<Unit>>()
    val undoEvent: LiveData<Event<Unit>> = _undoEvent

    private val isWhiteTurn = MutableLiveData<Boolean>()

    private val _exceptionEvent = MutableLiveData<Event<Exception>>()
    val exceptionEvent: LiveData<Event<Exception>> = _exceptionEvent

    private val _hasNext = MutableLiveData<Boolean>()
    val hasNext: LiveData<Boolean> = _hasNext

    init {
        getTask()
    }

    fun nextClicked(v: View?) {
        v ?: return
        getTask()
    }

    fun hintMove(v: View?) {
        v ?: return
        val turns = legalTurns.value ?: return
        if (turns.isEmpty()) {
            return
        }
        val turn = turns.first()
        lastMoveHinted.value = true
        _turnEvent.value = Event(turn.SanNotation)
    }

    private fun getTask() = viewModelScope.launch {
        _currentFen.postValue(null)
        correctMoves.postValue(0)
        _lastMoveWrong.postValue(false)
        _lastMoveCorrect.postValue(false)
        lastMoveHinted.postValue(false)
        _isLoading.postValue(true)
        val result = withContext(Dispatchers.IO) {
            //taskProvider.getNextTask(1500)
            taskProvider.getNextTask(EloUtils.elo!!)
        }
        when (result) {
            is Result.Success<Task> -> {
                Log.println(Log.DEBUG, "Task json", result.data.toString())
                _task.postValue(result.data)

                _fenLoadedEvent.postValue(Event(result.data.StartFEN))
                legalTurns.postValue(result.data.FirstPossibleTurns)
                isWhiteTurn.postValue(result.data.IsWhiteTurn)
                _taskDone.postValue(false)
            }
            is Result.Error -> {
                Log.println(Log.ERROR, "Internet error", result.exception.message!!)
                _exceptionEvent.postValue(Event(result.exception))
            }
        }
        _isLoading.postValue(false)
        _hasNext.postValue(taskProvider.hasNext())
    }

    private fun calcElo(percent: Float) {
        val curTask = task.value ?: return
        val curElo = elo.value ?: EloUtils.elo!!
        val eloDelta = EloUtils.estimateDiffElo(curElo, curTask.TargetElo, percent)
        _eloDiff.value = eloDelta
        _elo.value = curElo + eloDelta
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
            _lastMoveCorrect.value = false
            _lastMoveWrong.value = true

            if (taskDone.value != true) {
                val minDepth = turns.minOf { turn -> turn.minDepth }
                val percent = correctTurns.toFloat() / (correctTurns + minDepth).toFloat()
                calcElo(percent)
            }

            _taskDone.value = true
            _undoEvent.value = Event(Unit)
            return
        }

        correctMoves.value = correctMoves.value?.plus(1)
        if (lastMoveHinted.value != true) {
            _lastMoveCorrect.value = true
            _lastMoveWrong.value = false
        } else {
            lastMoveHinted.value = false
        }

        if (turn.IsLastTurn) {
            if (taskDone.value != true) {
                calcElo(1.0f)
            }
            legalTurns.value = emptyList()
            _taskDone.value = true
            return
        }

        legalTurns.value = turn.ContinueVariations
        isWhiteTurn.value = isWhiteTurn.value?.not()
        _turnEvent.value = Event(turn.AnswerTurnSanNotation)
    }

    // view model sets it by itself, no need to handle this
    override fun onAllowOpponentMovesChanged(allowOpponentMovesChanged: Boolean) = Unit

    // vm only needs to check for valid moves, no need to handle check
    override fun onCheck(isWhiteChecked: Boolean) = Unit

    // vm only needs to check for valid moves, no need to handle checkmate
    override fun onCheckmate(whiteLost: Boolean) = Unit

    override fun onFenChanged(newFen: String) {
        _currentFen.value = newFen
    }

    // vm sets isWhite by itself, no need to handle that
    override fun onIsWhiteChanged(isWhite: Boolean) = Unit

    // vm doesn't need to handle that
    override fun onStalemate() = Unit

    // and that (I only extend this for one method)
    override fun onUndo() = Unit

    // endregion
}