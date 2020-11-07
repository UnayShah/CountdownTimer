package com.example.timerApplication;

import android.view.View;
import android.widget.TextView;

import com.example.timerApplication.Timers.Timer;

import java.util.List;

public class PopupActivityFactory {
    public static PopupActivity getInstance(View popupView, View timersView, TextView textView, Boolean newTimer, List<Timer> listTimers, Integer position, RecyclerAdapter recyclerAdapter){
        return new PopupActivity(popupView, timersView, textView, newTimer, listTimers, position, recyclerAdapter);
    }
}
