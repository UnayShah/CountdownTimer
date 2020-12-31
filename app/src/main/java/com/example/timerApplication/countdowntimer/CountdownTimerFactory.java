package com.example.timerApplication.countdowntimer;

import android.widget.TextView;

import com.example.timerApplication.TimerActivity;

public class CountdownTimerFactory {

    public static CountdownTimer getInstance(TextView timerTextView, TimerActivity timerActivity) {
        return new CountdownTimer(timerTextView, timerActivity);
    }
}
