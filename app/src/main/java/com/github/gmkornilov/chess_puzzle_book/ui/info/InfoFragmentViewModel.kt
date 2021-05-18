package com.github.gmkornilov.chess_puzzle_book.ui.info

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gmkornilov.chess_puzzle_book.data.Event
import com.github.gmkornilov.chess_puzzle_book.data.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.Exception


class InfoFragmentViewModel(
    private val taskApi: TaskApi,
) : ViewModel() {
    val username = MutableLiveData<String>()

    val games = MutableLiveData(1)

    private val jobId = MutableLiveData<String>()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> = _progress

    private val _exceptionEvent = MutableLiveData<Event<Exception>>()
    val exceptionEvent: LiveData<Event<Exception>> = _exceptionEvent

    private val _loadedEvent = MutableLiveData<Event<JobStatus>>()
    val loadedEvent: LiveData<Event<JobStatus>> = _loadedEvent

    fun generate(v: View?) {
        v ?: return

        viewModelScope.launch {
            _loading.postValue(true)
            _progress.postValue(0)
            val jobInfoResult = withContext(Dispatchers.IO) {
                fetchJob()
            }
            when (jobInfoResult) {
                is Result.Success<JobInfo> -> {
                    jobId.postValue(jobInfoResult.data.jobId)
                }
                is Result.Error -> {
                    _exceptionEvent.postValue(Event(jobInfoResult.exception))
                    _loading.postValue(false)
                    return@launch
                }
            }
            val channel = ticker(delayMillis = 1000, initialDelayMillis = 150)
            while (true) {
                channel.receive()
                when (val result = fetchJobStatus()) {
                    is Result.Success<JobStatus> -> {
                        if (result.data.done) {
                            _loadedEvent.postValue(Event(result.data))
                            break
                        } else {
                            _progress.postValue(
                                convertToPercent(result.data.progress)
                            )
                        }
                    }
                    is Result.Error -> {
                        Log.println(Log.ERROR, "Internet error", result.exception.message!!)
                        _loading.postValue(false)
                        _exceptionEvent.postValue(Event(result.exception))
                        return@launch
                    }
                }
            }
            channel.cancel()
            _loading.postValue(false)
        }
    }

    private suspend fun fetchJob(): Result<JobInfo> {
        val usernameStr = username.value ?: return Result.Error(Exception("Empty username"))
        if (usernameStr.isBlank()) {
            return Result.Error(Exception("Empty username"))
        }
        val last = games.value ?: return Result.Error(Exception("Empty amount of games"))
        if (last <= 0) {
            return Result.Error(Exception("Not positive amount of games"))
        }
        return try {
            val response = taskApi.startGenerate(usernameStr, last)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(Exception("Error starting generate"))
            }
        } catch (t: Exception) {
            Result.Error(t)
        }
    }

    private suspend fun fetchJobStatus(): Result<JobStatus> {
        val jobIdStr = jobId.value ?: return Result.Error(Exception("Job id is not set"))
        return try {
            val response = withContext(Dispatchers.IO) {
                taskApi.getJobStatus(jobIdStr)
            }
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(JobNotFound("Job with id $jobIdStr not found"))
            }
        } catch (t: Exception) {
            Result.Error(t)
        }
    }

    private fun convertToPercent(progress: Float): Int {
        return (progress * 100).toInt()
    }
}