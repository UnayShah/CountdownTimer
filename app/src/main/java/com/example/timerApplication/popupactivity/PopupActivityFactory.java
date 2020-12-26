package com.example.timerApplication.popupactivity;

import android.view.View;
import android.widget.TextView;

import com.example.timerApplication.RecyclerAdapter;

public class PopupActivityFactory {
    public static PopupActivity getInstance(View popupView, RecyclerAdapter.ListItemViewHolder viewHolder, TextView textView, Boolean newTimer, Integer position, RecyclerAdapter recyclerAdapter){
        return new PopupActivity(popupView, viewHolder, textView, newTimer, position, recyclerAdapter);
    }
}
