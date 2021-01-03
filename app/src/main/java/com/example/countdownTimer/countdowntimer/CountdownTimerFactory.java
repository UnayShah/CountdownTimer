package com.example.countdownTimer.countdowntimer;

import android.widget.TextView;

import com.example.countdownTimer.TimerActivity;

public class CountdownTimerFactory {

    public static CountdownTimer getInstance(TextView timerTextView, TimerActivity timerActivity) {
        return new CountdownTimer(timerTextView, timerActivity);
    }
}
