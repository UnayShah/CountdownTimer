package com.example.timerApplication;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PopupActivityFactory {
    public static PopupActivity getInstance(LinearLayout linearLayoutTimers, View popupView, View timersView, TextView textView){
        return new PopupActivity( linearLayoutTimers, popupView, timersView, textView);
    }
}
