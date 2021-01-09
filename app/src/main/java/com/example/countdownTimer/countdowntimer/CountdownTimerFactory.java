package com.example.countdownTimer.countdowntimer;

import com.example.countdownTimer.TimerActivity;

public class CountdownTimerFactory {

    public static CountdownTimer getInstance(TimerActivity timerActivity) {
        return new CountdownTimer(timerActivity);
    }
}
