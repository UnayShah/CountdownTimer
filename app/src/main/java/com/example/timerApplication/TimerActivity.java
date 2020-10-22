package com.example.timerApplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class TimerActivity extends Fragment implements View.OnClickListener {

    LinearLayout linearLayoutTimers;
    Button addTimerButton;
    Button startPauseTimerButton;
    Button stopTimerButton;
    Button setTimerButton;
    Button cancelSetTimerButton;
    ScrollView scrollViewTimers;
    NumberPicker numberPickerHours;
    NumberPicker numberPickerMinutes;
    NumberPicker numberPickerSeconds;
    View popupView;
    PopupWindow popupWindow;
    Boolean editingView;
    Integer hours;
    Integer minutes;
    Integer seconds;
    Vibrator vibrator;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.timer_layout, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    /**
     * Initialize view objects
     *
     * @param view
     */
    public void init(View view) {
        editingView = false;
        linearLayoutTimers = view.findViewById(R.id.timers_scrollView_linearLayout);
        addTimerButton = view.findViewById(R.id.add_button);
        startPauseTimerButton = view.findViewById(R.id.start_pause_button);
        scrollViewTimers = view.findViewById(R.id.timers_scrollView);
        stopTimerButton = view.findViewById(R.id.stop_button);
        startPauseTimerButton.setOnClickListener(this);
        stopTimerButton.setOnClickListener(this);
        addTimerButton.setOnClickListener(this);
        vibrator = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * Initialize popup
     */
    public void popupInit() {
        //Initialize popup
        popupView = getLayoutInflater().inflate(R.layout.timer_picker, null, true);

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
    }

    /**
     * Set number picker range
     *
     * @param numberPicker
     * @param min
     * @param max
     */
    public void numberPickerInit(NumberPicker numberPicker, int min, int max) {
        numberPicker.setMaxValue(max);
        numberPicker.setMinValue(min);
        numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });
    }

    /**
     * Override onCLick
     * Call functions based on button click
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_button:
                addTimer();
                break;
            case R.id.start_pause_button:
                System.out.println("Start Pause");
                break;
            case R.id.stop_button:
                System.out.println("Stop");
                break;
            default:
                System.out.println("None");
        }
    }


    private void addTimer() {
        //Create new view
        final View timersView = getLayoutInflater().inflate(R.layout.add_timer, null, true);
        //Set visibility to GONE otherwise it is visible in background
        timersView.setVisibility(View.GONE);
        //Initialize contents of view
        final TextView textView = timersView.findViewById(R.id.timer_textView);
        final ImageButton editButton = timersView.findViewById(R.id.edit_timer);
        ImageButton deleteButton = timersView.findViewById(R.id.delete_timer);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editingView = true;
                hours = Integer.valueOf(textView.getText().toString().split(":")[0]);
                minutes = Integer.valueOf(textView.getText().toString().split(":")[1]);
                seconds = Integer.valueOf(textView.getText().toString().split(":")[2]);
                editTimer(timersView, textView, hours, minutes, seconds);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTimer(timersView);
            }
        });
        editTimer(timersView, textView, 0, 0, 0);
        linearLayoutTimers.addView(timersView);

    }

    private void editTimer(final View timersView, final TextView textView, int hours, int minutes, int seconds) {
        popupInit();
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
                setTimer(timersView, textView);
            }
        });

        cancelSetTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSetTimer(timersView);
            }
        });
    }

    /**
     * Cancel the setting of timer after add button is pressed
     *
     * @param timersView
     */
    private void cancelSetTimer(View timersView) {
        if (!editingView)
            linearLayoutTimers.removeView(timersView);
        popupWindow.dismiss();
        editingView = false;
    }

    /**
     * Set value of a new timer or that of edited timer
     *
     * @param timersView
     * @param textView
     */
    private void setTimer(View timersView, TextView textView) {
        hours = numberPickerHours.getValue();
        minutes = numberPickerMinutes.getValue();
        seconds = numberPickerSeconds.getValue();
        textView.setText(String.format("%" + 2 + "s", hours).replace(' ', '0') + ":" + String.format("%" + 2 + "s", minutes).replace(' ', '0') + ":" + String.format("%" + 2 + "s", seconds).replace(' ', '0'));
        popupWindow.dismiss();
        timersView.setVisibility(View.VISIBLE);
        editingView = false;
    }

    /**
     * Remove current timer view
     *
     * @param view
     */
    private void deleteTimer(View view) {
        linearLayoutTimers.removeView(view);
    }

}
