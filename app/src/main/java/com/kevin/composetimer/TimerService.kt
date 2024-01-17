package com.kevin.composetimer

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class TimerService : Service() {

    private val timerFlow = MutableStateFlow(0L)
    private val binder = TimerBinder()

    private val timer = object : CountDownTimer(3600000L, 1) {
        override fun onTick(millisUntilFinished: Long) {
            timerFlow.value = millisUntilFinished
        }

        override fun onFinish() {
            timerFlow.value = 0L
        }

    }

    fun startTimer() {
        timer.start()
    }

    fun stopTimer() {
        timer.cancel()
        timerFlow.value = 0L
    }

    fun getTimer() = timerFlow.asStateFlow()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }
}

fun Long.toTime(): String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    val millis = this % 1000
    return String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, millis)
}