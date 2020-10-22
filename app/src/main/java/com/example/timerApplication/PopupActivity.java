package com.example.timerApplication;

import android.content.Context;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopupActivity implements View.OnClickListener {

    LinearLayout linearLayoutTimers;
    Button setTimerButton;
    Button cancelSetTimerButton;
    NumberPicker numberPickerHours;
    NumberPicker numberPickerMinutes;
    NumberPicker numberPickerSeconds;
    View popupView;
    View timersView;
    PopupWindow popupWindow;
    Boolean editingView;
    Integer hours;
    Integer minutes;
    Integer seconds;
    Vibrator vibrator;
    TextView textView;

    public PopupActivity(LinearLayout linearLayoutTimers, View popupView, View timersView, TextView textView) {
        this.linearLayoutTimers = linearLayoutTimers;
        this.timersView = timersView;
        this.textView = textView;
        this.popupView = popupView;
        init();
        this.hours = ConstantsClass.NUMBER_PICKER_HOURS_START;
        this.minutes = ConstantsClass.NUMBER_PICKER_MINUTES_START;
        this.seconds = ConstantsClass.NUMBER_PICKER_SECONDS_START;
    }

    /**
     * Initialize popup
     */
    public void init() {
        //Initialize number picker
        numberPickerHours = popupView.findViewById(R.id.numberpicker_hours);
        numberPickerMinutes = popupView.findViewById(R.id.numberpicker_minutes);
        numberPickerSeconds = popupView.findViewById(R.id.numberpicker_seconds);

        //Set number picker range
        numberPickerInit(numberPickerHours, ConstantsClass.NUMBER_PICKER_HOURS_START, ConstantsClass.NUMBER_PICKER_HOURS_END);
        numberPickerInit(numberPickerMinutes, ConstantsClass.NUMBER_PICKER_MINUTES_START, ConstantsClass.NUMBER_PICKER_MINUTES_END);
        numberPickerInit(numberPickerSeconds, ConstantsClass.NUMBER_PICKER_SECONDS_START, ConstantsClass.NUMBER_PICKER_SECONDS_END);

        //Route number picker buttons
        setTimerButton = popupView.findViewById(R.id.set_timer_button);
        cancelSetTimerButton = popupView.findViewById(R.id.cancel_set_timer_button);

        vibrator = (Vibrator) popupView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_set_timer_button:
                cancelSetTimer();
                break;
            case R.id.set_timer_button:
                setTimer();
                break;
        }
    }

    /**
     * Cancel the setting of timer after add button is pressed
     */
    private void cancelSetTimer() {
        if (!editingView)
            linearLayoutTimers.removeView(timersView);
        popupWindow.dismiss();
        editingView = false;
    }

    /**
     * Set value of a new timer or that of edited timer
     */
    private void setTimer() {
        hours = numberPickerHours.getValue();
        minutes = numberPickerMinutes.getValue();
        seconds = numberPickerSeconds.getValue();
        textView.setText(String.format("%" + 2 + "s", hours).replace(' ', '0') + ":" + String.format("%" + 2 + "s", minutes).replace(' ', '0') + ":" + String.format("%" + 2 + "s", seconds).replace(' ', '0'));
        popupWindow.dismiss();
        timersView.setVisibility(View.VISIBLE);
        editingView = false;
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
        numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });
    }

    public View editTimer() {
        return editTimer(ConstantsClass.NUMBER_PICKER_HOURS_START, ConstantsClass.NUMBER_PICKER_MINUTES_START, ConstantsClass.NUMBER_PICKER_SECONDS_START);
    }

    public View editTimer(Integer hours, Integer minutes, Integer seconds) {
//        init();
        numberPickerHours.setValue(hours);
        numberPickerMinutes.setValue(minutes);
        numberPickerSeconds.setValue(seconds);
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(timersView, Gravity.CENTER, 0, 0);
        numberPickerHours.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                vibrator.vibrate(ConstantsClass.VIBRATE_VERY_SHORT);
            }
        });
        numberPickerMinutes.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                vibrator.vibrate(ConstantsClass.VIBRATE_VERY_SHORT);
            }
        });
        numberPickerSeconds.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                vibrator.vibrate(ConstantsClass.VIBRATE_VERY_SHORT);
            }
        });
        setTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimer();
            }
        });

        cancelSetTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSetTimer();
            }
        });
        return timersView;
    }
}
