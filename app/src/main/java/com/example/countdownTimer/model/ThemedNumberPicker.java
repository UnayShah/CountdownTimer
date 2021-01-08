package com.example.countdownTimer.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.widget.NumberPicker;

import com.example.countdownTimer.R;

public class ThemedNumberPicker extends NumberPicker {
    public ThemedNumberPicker(Context context) {
        super(context);
    }

    public ThemedNumberPicker(Context context, AttributeSet attrs) {
        super(new ContextThemeWrapper(context, R.style.AppTheme_PopupNumberPicker), attrs);
    }
}
