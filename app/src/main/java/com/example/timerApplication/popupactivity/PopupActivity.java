package com.example.timerApplication.popupactivity;

import android.content.Context;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.timerApplication.R;
import com.example.timerApplication.RecyclerAdapter;
import com.example.timerApplication.RecyclerAdapterTimer;
import com.example.timerApplication.TimerActivity;
import com.example.timerApplication.common.ConstantsClass;
import com.example.timerApplication.timers.Timer;
import com.example.timerApplication.timers.TimerGroup;

public class PopupActivity implements View.OnClickListener {

    RecyclerAdapter recyclerAdapter;
    Button setTimerButton;
    Button cancelSetTimerButton;
    TextView timerText;
    NumberPicker numberPickerHours;
    NumberPicker numberPickerMinutes;
    NumberPicker numberPickerSeconds;
    View popupView;
    View timersView;
    PopupWindow popupWindow;
    Timer timer;
    Vibrator vibrator;
    Boolean newTimer;
    Integer position;
    Boolean setAndDismiss;
    RecyclerAdapterTimer recyclerAdapterTimer;

    /**
     * Popup view to get layout from main activity.
     * Timertext to be able to change value.
     * newTimer to check whether editing or adding new timer.
     * position to identify position of delete.
     * recyclerAdapter to delete from list.
     *
     * @param popupView
     * @param timersView
     * @param timerText
     * @param newTimer
     * @param position
     * @param recyclerAdapterTimer
     */
    public PopupActivity(View popupView, View timersView, TextView timerText, Boolean newTimer, Integer position, RecyclerAdapterTimer recyclerAdapterTimer) {
        this.timersView = timersView;
        this.popupView = popupView;
        this.timerText = timerText;
        this.newTimer = newTimer;
        this.position = position;
        this.recyclerAdapterTimer = recyclerAdapterTimer;
        init();
        this.timer = new Timer(timerText.getText().toString());
    }
    public PopupActivity(View popupView, View timersView, TextView timerText, Boolean newTimer, Integer position, RecyclerAdapter recyclerAdapter) {
        this.timersView = timersView;
        this.popupView = popupView;
        this.timerText = timerText;
        this.newTimer = newTimer;
        this.position = position;
        this.recyclerAdapter = recyclerAdapter;
        init();
        this.timer = new Timer(timerText.getText().toString());
    }

    /**
     * Initialize popup
     * Set number picker range
     * Map buttons
     * Initialize vibrator
     */
    public void init() {
        //Initialize number picker
        numberPickerHours = popupView.findViewById(R.id.number_picker_hours);
        numberPickerMinutes = popupView.findViewById(R.id.number_picker_minutes);
        numberPickerSeconds = popupView.findViewById(R.id.number_picker_seconds);

        //Set number picker range
        numberPickerInit(numberPickerHours, ConstantsClass.NUMBER_PICKER_HOURS_START, ConstantsClass.NUMBER_PICKER_HOURS_END);
        numberPickerInit(numberPickerMinutes, ConstantsClass.NUMBER_PICKER_MINUTES_START, ConstantsClass.NUMBER_PICKER_MINUTES_END);
        numberPickerInit(numberPickerSeconds, ConstantsClass.NUMBER_PICKER_SECONDS_START, ConstantsClass.NUMBER_PICKER_SECONDS_END);

        //Route number picker buttons
        setTimerButton = popupView.findViewById(R.id.set_timer_button);
        cancelSetTimerButton = popupView.findViewById(R.id.cancel_set_timer_button);
        setTimerButton.setOnClickListener(this);
        cancelSetTimerButton.setOnClickListener(this);
        vibrator = (Vibrator) popupView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == cancelSetTimerButton.getId())
            cancelSetTimer();
        else if (view.getId() == setTimerButton.getId())
            timerText.setText(setTimer().toString());
    }


    /**
     * Cancel the setting of timer after add button is pressed
     * Delete from the recycler view
     */
    private void cancelSetTimer() {
        setAndDismiss = true;
        if (newTimer) {
            recyclerAdapterTimer.deleteTimer(position);
        }
        popupWindow.dismiss();
    }

    /**
     * Set value of a new timer or that of edited timer
     */
    private Timer setTimer() {
        setAndDismiss = true;
        timer.setHours(numberPickerHours.getValue());
        timer.setMinutes(numberPickerMinutes.getValue());
        timer.setSeconds(numberPickerSeconds.getValue());
        popupWindow.dismiss();
        timerText.setText(timer.toString());
        TimerActivity.listTimers.set(position, timer);
        timersView.setVisibility(View.VISIBLE);
        return timer;
    }

    /**
     * Set number picker range
     *
     * @param numberPicker
     * @param min
     * @param max
     */
    private void numberPickerInit(NumberPicker numberPicker, int min, int max) {
        numberPicker.setMaxValue(max);
        numberPicker.setMinValue(min);
        numberPicker.setFormatter(value -> String.format("%02d", value));
    }

    public Timer editTimer() {
        return editTimer(new Timer(timerText.getText().toString()));
    }

    public Timer editTimer(Timer timer) {
        setAndDismiss = false;
        numberPickerHours.setValue(timer.getHours());
        numberPickerMinutes.setValue(timer.getMinutes());
        numberPickerSeconds.setValue(timer.getSeconds());
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(timersView, Gravity.CENTER, 0, 0);

        numberPickerHours.setOnScrollListener((view, scrollState) -> {
            vibrator.vibrate(ConstantsClass.VIBRATE_VERY_SHORT);
            setTimerButton.setEnabled(!allNumberPickersZero());
        });
        numberPickerMinutes.setOnScrollListener((view, scrollState) -> {
            vibrator.vibrate(ConstantsClass.VIBRATE_VERY_SHORT);
            setTimerButton.setEnabled(!allNumberPickersZero());
        });
        numberPickerSeconds.setOnScrollListener((view, scrollState) -> {
            vibrator.vibrate(ConstantsClass.VIBRATE_VERY_SHORT);
            setTimerButton.setEnabled(!allNumberPickersZero());
        });


        popupWindow.setOnDismissListener(() -> {
            if (!setAndDismiss) cancelSetTimer();
        });
        setTimerButton.setEnabled(!allNumberPickersZero());
        return timer;
    }

    private Boolean allNumberPickersZero() {
        return numberPickerHours.getValue() == 0 && numberPickerMinutes.getValue() == 0 && numberPickerSeconds.getValue() == 0;
    }
}
