package com.example.timerApplication.countdowntimer;

import android.widget.TextView;

import com.example.timerApplication.TimerActivity;
import com.example.timerApplication.TimerActivityActivity;

public class CountdownTimerFactory {
    public static CountdownTimer getInstance(TextView timerTextView, TimerActivity timerActivity) {
        return new CountdownTimer(timerTextView, timerActivity);
    }

    public static CountdownTimer getInstance(TextView timerTextView, TimerActivityActivity timerActivityActivity) {
        return new CountdownTimer(timerTextView, timerActivityActivity);
    }
}
