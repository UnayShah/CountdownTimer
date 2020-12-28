package com.example.timerApplication.popupactivity;

import android.content.Context;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.timerApplication.R;
import com.example.timerApplication.RecyclerAdapter;
import com.example.timerApplication.common.ConstantsClass;
import com.example.timerApplication.model.DataHolder;
import com.example.timerApplication.timers.Timer;
import com.example.timerApplication.timers.TimerGroup;
import com.example.timerApplication.timers.TimerGroupType;

public class PopupActivity implements View.OnClickListener {

    RecyclerAdapter recyclerAdapter;
    Button setTimerButton;
    Button cancelSetTimerButton;
    Button toggleNumberPickerTimerGroup;
    TextView timerText;
    NumberPicker numberPickerHours;
    NumberPicker numberPickerMinutes;
    NumberPicker numberPickerSeconds;
    View popupView;
    View timersView;
    View addNewTimerGroupView;
    PopupWindow popupWindow;
    TimerGroup timerGroup;
    Vibrator vibrator;
    Boolean newTimer;
    Integer position;
    Boolean setAndDismiss;
    LinearLayout numberPickerLayout;
    LinearLayout timerGroupPickerLayout;
    RecyclerAdapter.ListItemViewHolder viewHolder;


    /**
     * Popup view to get layout from main activity.
     * Timertext to be able to change value.
     * newTimer to check whether editing or adding new timer.
     * position to identify position of delete.
     * recyclerAdapter to delete from list.
     *
     * @param popupView
     * @param timerText
     * @param newTimer
     * @param position
     */
    public PopupActivity(View popupView, RecyclerAdapter.ListItemViewHolder viewHolder, TextView timerText, Boolean newTimer, Integer position, RecyclerAdapter recyclerAdapter) {
        this.timersView = viewHolder.itemView;
        this.popupView = popupView;
        this.timerText = timerText;
        this.newTimer = newTimer;
        this.position = position;
        this.recyclerAdapter = recyclerAdapter;
        init();
        this.timerGroup = new TimerGroup(viewHolder.getTimerGroupType());
        this.viewHolder = viewHolder;
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

        addNewTimerGroupView = popupView.findViewById(R.id.add_new_timer_group);
        numberPickerLayout = popupView.findViewById(R.id.timer_picker_numbers);
        timerGroupPickerLayout = popupView.findViewById(R.id.new_timer_group);
        timerGroupPickerLayout.setVisibility(View.GONE);
        addNewTimerGroupView.setVisibility(View.GONE);
        numberPickerLayout.setVisibility(View.VISIBLE);

        //Route number picker buttons
        setTimerButton = popupView.findViewById(R.id.set_timer_button);
        cancelSetTimerButton = popupView.findViewById(R.id.cancel_set_timer_button);
        addNewTimerGroupView.setOnClickListener(this);
        toggleNumberPickerTimerGroup = popupView.findViewById(R.id.toggle_numberpicker_timergroup);
        setTimerButton.setOnClickListener(this);
        cancelSetTimerButton.setOnClickListener(this);
        toggleNumberPickerTimerGroup.setOnClickListener(this);
        vibrator = (Vibrator) popupView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onClick(View view) {
        DataHolder.getInstance().setDisableButtonClick(false);
        if (view.getId() == cancelSetTimerButton.getId())
            cancelSetTimer();
        else if (view.getId() == setTimerButton.getId() && !allNumberPickersZero())
            timerText.setText(setTimer().toString());
        else if (view.getId() == toggleNumberPickerTimerGroup.getId())
            toggleView();
        else if (view.getId() == addNewTimerGroupView.getId()) {
            DataHolder.getInstance().getListTimerGroup().add(new TimerGroup(TimerGroupType.TIMER_GROUP));
            recyclerAdapter.setFromHome(false);
            recyclerAdapter.setNewItem(true);
            recyclerAdapter.setFromStorage(false);
            recyclerAdapter.notifyItemInserted(DataHolder.getInstance().getListTimerGroup().size());
            popupWindow.dismiss();
        }
    }

    public void toggleView() {
        if (numberPickerLayout.getVisibility() == View.VISIBLE) {
            numberPickerLayout.setVisibility(View.GONE);
            addNewTimerGroupView.setVisibility(View.VISIBLE);
            timerGroupPickerLayout.setVisibility(View.VISIBLE);
            addNewTimerGroupView.setVisibility(View.VISIBLE);
            toggleNumberPickerTimerGroup.setText(R.string.toggle_number_picker);
            for (TimerGroup tg : DataHolder.getInstance().getAllTimerGroups()) {
                if (!tg.getName().equals(DataHolder.getInstance().getStackNavigation().peek())) {
                    View view = LayoutInflater.from(popupView.getContext()).inflate(R.layout.timergroup_picker_item, null, false);
                    TextView textView = view.findViewById(R.id.timer_group_picker_name);
                    textView.setText(tg.getName());
                    view.setVisibility(View.VISIBLE);
                    view.setOnClickListener(v -> {
                        setAndDismiss = true;
                        popupWindow.dismiss();
                        DataHolder.getInstance().getListTimerGroup().set(position, DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(tg.getName())));
                        DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).setListTimerGroup(DataHolder.getInstance().getListTimerGroup());
                        viewHolder.setTimerGroupType(TimerGroupType.TIMER_GROUP);
                        timerText.setText(tg.getName());
                        timersView.setVisibility(View.VISIBLE);
                        DataHolder.getInstance().setQueueTimers(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getTimersQueue());
                    });
                    timerGroupPickerLayout.addView(view);
                }
            }
        } else {
            numberPickerLayout.setVisibility(View.VISIBLE);
            timerGroupPickerLayout.setVisibility(View.GONE);
            addNewTimerGroupView.setVisibility(View.GONE);
            toggleNumberPickerTimerGroup.setText(R.string.toggle_timer_group_picker);
            timerGroupPickerLayout.removeAllViewsInLayout();
        }
    }

    /**
     * Cancel the setting of timer after add button is pressed
     * Delete from the recycler view
     */
    private void cancelSetTimer() {
        setAndDismiss = true;
        if (newTimer) {
            recyclerAdapter.deleteTimerGroup(position);
        }
        popupWindow.dismiss();
    }

    /**
     * Set value of a new timer or that of edited timer
     */
    private TimerGroup setTimer() {
        setAndDismiss = true;
        timerGroup.getTimer().setHours(numberPickerHours.getValue());
        timerGroup.getTimer().setMinutes(numberPickerMinutes.getValue());
        timerGroup.getTimer().setSeconds(numberPickerSeconds.getValue());
        popupWindow.dismiss();
        timerText.setText(timerGroup.getTimer().toString());
        DataHolder.getInstance().getListTimerGroup().set(position, timerGroup);
        if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()))
            DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).setListTimerGroup(DataHolder.getInstance().getListTimerGroup());
        DataHolder.getInstance().setQueueTimers(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getTimersQueue());
        timersView.setVisibility(View.VISIBLE);
        return timerGroup;
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
        });
        numberPickerMinutes.setOnScrollListener((view, scrollState) -> {
            vibrator.vibrate(ConstantsClass.VIBRATE_VERY_SHORT);
        });
        numberPickerSeconds.setOnScrollListener((view, scrollState) -> {
            vibrator.vibrate(ConstantsClass.VIBRATE_VERY_SHORT);
        });
        numberPickerHours.setOnValueChangedListener((picker, oldVal, newVal) -> setTimerButton.setEnabled(!allNumberPickersZero()));
        numberPickerMinutes.setOnValueChangedListener((picker, oldVal, newVal) -> setTimerButton.setEnabled(!allNumberPickersZero()));
        numberPickerSeconds.setOnValueChangedListener((picker, oldVal, newVal) -> setTimerButton.setEnabled(!allNumberPickersZero()));


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
