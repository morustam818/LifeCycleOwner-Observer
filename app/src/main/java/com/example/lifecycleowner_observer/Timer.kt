package com.example.lifecycleowner_observer

import android.app.Application
import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class Timer(val timerText: TextView, application: Application, lifecycleOwner: LifecycleOwner) : LifecycleObserver {

    private var isStarted = false
    private val lifecycle = lifecycleOwner.lifecycle

    init {
        lifecycle.addObserver(this)
    }

    private val countDownTimer = object : CountDownTimer(60000, 3000) {
        override fun onTick(millisUntilFinished: Long) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                timerText.text = (millisUntilFinished / 1000).toString()
                Toast.makeText(application, "$millisUntilFinished", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFinish() {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                timerText.text = "Finished"
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (!isStarted) {
            isStarted = true
            countDownTimer.start()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stop() {
        countDownTimer.cancel()
    }

}