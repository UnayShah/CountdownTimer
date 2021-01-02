package com.example.timerApplication.popupactivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;

import com.example.timerApplication.R;
import com.example.timerApplication.RecyclerAdapter;
import com.example.timerApplication.model.DataHolder;
import com.example.timerApplication.timers.TimerGroup;
import com.example.timerApplication.timers.TimerGroupType;
import com.google.android.material.textfield.TextInputEditText;

public class TimerNamePopup extends PopupWindow implements View.OnClickListener, View.OnFocusChangeListener, PopupWindow.OnDismissListener, TextWatcher {
    TextInputEditText editTextTimerName;
    Button setTimerNameButton;
    Button cancelSetTimerNameButton;
    View view;
    Integer position;
    RecyclerAdapter recyclerAdapter;

    public TimerNamePopup(View view, RecyclerAdapter recyclerAdapter) {
        this(view, recyclerAdapter, null);
    }

    public TimerNamePopup(View view, RecyclerAdapter recyclerAdapter, Integer position) {
        super(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.position = position;
        setOutsideTouchable(true);
        setTouchable(true);
        setFocusable(true);
        this.view = view;
        this.recyclerAdapter = recyclerAdapter;
        init();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    private void init() {
        editTextTimerName = view.findViewById(R.id.editTextTimerName);
        setTimerNameButton = view.findViewById(R.id.setTimerNameButton);
        cancelSetTimerNameButton = view.findViewById(R.id.cancelSetTimerNameButton);
        editTextTimerName.setText(position == null ? "" : DataHolder.getInstance().getListTimerGroup().get(position).getName());
        setTimerNameButton.setEnabled(true);

        setOnDismissListener(this);
        editTextTimerName.addTextChangedListener(this);
        editTextTimerName.setOnFocusChangeListener(this);
        setTimerNameButton.setOnClickListener(this);
        cancelSetTimerNameButton.setOnClickListener(this);
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    private void cancelSetTimerName() {
        dismiss();
    }

    private void setTimerName() {
        TimerGroup timerGroup = new TimerGroup(editTextTimerName.getText().toString(), TimerGroupType.TIMER_GROUP);
        setTimerInRecyclerView(timerGroup);
    }

    private void setTimerInRecyclerView(TimerGroup timerGroup) {
        if (position == null) {
            DataHolder.getInstance().getAllTimerGroups().add(timerGroup);
        } else {
            if (DataHolder.getInstance().getStackNavigation().empty()) {
                DataHolder.getInstance().getAllTimerGroups().set(position, timerGroup);
            } else {
                DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup().set(position, timerGroup);
            }
        }
        DataHolder.getInstance().saveData(getContentView().getContext());
        recyclerAdapter.notifyDataSetChanged();
        dismiss();
    }

    @Override
    public void onClick(View view) {
        DataHolder.getInstance().setDisableButtonClick(true);
        if (view.getId() == setTimerNameButton.getId()) setTimerName();
        else if (view.getId() == cancelSetTimerNameButton.getId()) cancelSetTimerName();
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.edit_text_selected));
        } else {
            view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.edit_text));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        int length = s.length();
        if (length > 0 && !text.matches("([\\w\\d]+ ?)+[A-zZ-z0-9]*")) {
            s.delete(length - 1, length);
        }
        setTimerNameButton.setEnabled(length > 0 && !DataHolder.getInstance().getMapTimerGroups().containsKey(s.toString()));
    }

    @Override
    public void onDismiss() {
        DataHolder.getInstance().setDisableButtonClick(false);
        super.dismiss();
    }
}
