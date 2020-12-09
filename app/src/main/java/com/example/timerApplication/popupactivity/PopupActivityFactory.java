package com.example.timerApplication.popupactivity;

import android.view.View;
import android.widget.TextView;

import com.example.timerApplication.RecyclerAdapter;
import com.example.timerApplication.RecyclerAdapterTimer;

public class PopupActivityFactory {
    public static PopupActivity getInstance(View popupView, View timersView, TextView textView, Boolean newTimer, Integer position, RecyclerAdapterTimer recyclerAdapterTimer){
        return new PopupActivity(popupView, timersView, textView, newTimer, position, recyclerAdapterTimer);
    }
    public static PopupActivity getInstance(View popupView, View timersView, TextView textView, Boolean newTimer, Integer position, RecyclerAdapter recyclerAdapter){
        return new PopupActivity(popupView, timersView, textView, newTimer, position, recyclerAdapter);
    }
}
