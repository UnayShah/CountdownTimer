package com.example.timerApplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class TimerActivity extends Fragment implements View.OnClickListener {

    LinearLayout linearLayoutTimers;
    Button addTimerButton;
    Button startPauseTimerButton;
    Button stopTimerButton;
    ScrollView scrollViewTimers;
    View popupView;
    Boolean editingView;
    Integer hours;
    Integer minutes;
    Integer seconds;

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

    /**
     * Create and add new timer in list
     */
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
        linearLayoutTimers.addView(editTimer(timersView, textView));
    }

    private View editTimer(View timersView, TextView textView) {
        popupView = getLayoutInflater().inflate(R.layout.timer_picker, null, false);
        return PopupActivityFactory.getInstance(linearLayoutTimers, popupView, timersView, textView).editTimer();
    }

    private View editTimer(View timersView, TextView textView, Integer hours, Integer minutes, Integer seconds) {
        popupView = getLayoutInflater().inflate(R.layout.timer_picker, null, false);
        return PopupActivityFactory.getInstance(linearLayoutTimers, popupView, timersView, textView).editTimer(hours, minutes, seconds);
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
