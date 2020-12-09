package com.example.timerApplication.popupactivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.timerApplication.R;
import com.example.timerApplication.RecyclerAdapter;
import com.example.timerApplication.timers.TimerGroup;
import com.example.timerApplication.timers.TimerGroupType;

public class TimerNamePopupActivity implements View.OnClickListener, View.OnFocusChangeListener {
    EditText editTextTimerName;
    TextView timerTextView;
    Button setTimerNameButton;
    Button cancelSetTimerNameButton;
    View timerPopupView;
    RecyclerAdapter.ListItemViewHolder timerGroupView;
    Integer position;
    PopupWindow popupWindow;
    TimerGroup timerGroup;
    Boolean newTimerGroup;
    Boolean setAndDismiss;
    RecyclerAdapter recyclerAdapter;

    public TimerNamePopupActivity(View timerPopupView, RecyclerAdapter.ListItemViewHolder timerGroupView, TimerGroup timerGroup, Boolean newTimerGroup, Integer position, RecyclerAdapter recyclerAdapter) {
        this.timerPopupView = timerPopupView;
        this.timerGroupView = timerGroupView;
        this.position = position;
        this.recyclerAdapter = recyclerAdapter;
        this.newTimerGroup = newTimerGroup;
        init();
        this.timerGroup = timerGroup;
    }

    private void init() {
        editTextTimerName = timerPopupView.findViewById(R.id.editTextTimerName);
        setTimerNameButton = timerPopupView.findViewById(R.id.setTimerNameButton);
        cancelSetTimerNameButton = timerPopupView.findViewById(R.id.cancelSetTimerNameButton);
        timerTextView = timerPopupView.findViewById(R.id.timer_textView);
        editTextTimerName.setOnFocusChangeListener(this);
        setTimerNameButton.setOnClickListener(this);
        cancelSetTimerNameButton.setOnClickListener(this);
    }

    private void cancelSetTimerName() {
        setAndDismiss = true;
        if (newTimerGroup) {
            recyclerAdapter.deleteTimerGroup(position);
        }
        popupWindow.dismiss();
    }

    private TimerGroup setTimerName(){
        timerGroup.setName(editTextTimerName.getText().toString());
        timerGroupView.setTimerText(editTextTimerName.getText().toString());
        timerGroupView.itemView.setVisibility(View.VISIBLE);
        popupWindow.dismiss();
        return timerGroup;
    }


    public TimerGroup editTimerGroup() {
        popupWindow = new PopupWindow(timerPopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(timerGroupView.itemView, Gravity.CENTER, 0, 0);
        editTextTimerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<=0 || s.charAt(0) == ' ')setTimerNameButton.setEnabled(false);
                else setTimerNameButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextTimerName.setText(timerGroupView.getTimerText().getText());
        return timerGroup;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == setTimerNameButton.getId()) timerGroup.setName(setTimerName().getName());
        else if(view.getId()==cancelSetTimerNameButton.getId())cancelSetTimerName();
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

    }
}
