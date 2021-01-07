package com.example.countdownTimer;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countdownTimer.common.ConstantsClass;
import com.example.countdownTimer.countdowntimer.CountdownTimer;
import com.example.countdownTimer.countdowntimer.CountdownTimerFactory;
import com.example.countdownTimer.model.DataHolder;
import com.example.countdownTimer.popupactivity.TimePickerPopup;
import com.example.countdownTimer.timers.Timer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener, IStartDragListener {
    public static Boolean timerRunning = false;
    ImageButton addTimerButton;
    ImageButton startPauseTimerButton;
    ImageButton stopTimerButton;
    ImageButton returnButton;
    ScrollView scrollViewTimers;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ItemTouchHelper itemTouchHelper;
    TextView timerTextView;
    CountdownTimer countdownTimer;
    Long pauseTimeInMillis;
    Integer indexOfTimer;
    View timerLayout;
    AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_layout_recycler);
        init();
    }

    public void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        adView = findViewById(R.id.adView_timer);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        recyclerView = findViewById(R.id.timers_scrollView_recyclerView);
        recyclerAdapter = new RecyclerAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recyclerAdapter);
        timerTextView = findViewById(R.id.timer_textView);
        timerTextView.setText(new Timer().toString());
        addTimerButton = findViewById(R.id.add_button);
        startPauseTimerButton = findViewById(R.id.start_pause_button);
        returnButton = findViewById(R.id.return_button);
        scrollViewTimers = findViewById(R.id.timers_scrollView);
        stopTimerButton = findViewById(R.id.stop_button);
        ItemTouchHelper.Callback callback = new ItemMoveCallback(recyclerAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        startPauseTimerButton.setOnClickListener(this);
        stopTimerButton.setOnClickListener(this);
        stopTimerButton.setVisibility(View.INVISIBLE);
        addTimerButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        countdownTimer = CountdownTimerFactory.getInstance(timerTextView, this);
        timerRunning = false;
        pauseTimeInMillis = ConstantsClass.ZERO_LONG;
        indexOfTimer = ConstantsClass.ZERO;
        returnButton.setVisibility(View.GONE);
        startPauseTimerButton.setVisibility(View.GONE);
        addTimerButton.setVisibility(View.GONE);
        stopTimerButton.setVisibility(View.GONE);
        returnButton.setVisibility(View.VISIBLE);
        startPauseTimerButton.setVisibility(View.VISIBLE);
        addTimerButton.setVisibility(View.VISIBLE);
        timerLayout = findViewById(R.id.timer_layout);
        if (!DataHolder.getInstance().getStackNavigation().isEmpty()) {
            if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()) && DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek()) != null)
                DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup());
            else DataHolder.getInstance().setListTimerGroup(new ArrayList<>());
            recyclerAdapter.notifyDataSetChanged();
        }
        DataHolder.getInstance().setDisableButtonClick(false);
    }


    public void requestDrag(RecyclerAdapter.ListItemViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    /**
     * Override onCLick
     * Call functions based on button click
     *
     * @param view of button clicked
     */
    @Override
    public void onClick(View view) {
        if (!DataHolder.getInstance().getDisableButtonClick()) {
            DataHolder.getInstance().setDisableButtonClick(true);
            if (view.getId() == addTimerButton.getId()) addTimer();
            else if (view.getId() == startPauseTimerButton.getId())
                startPauseTimer();
            else if (view.getId() == stopTimerButton.getId()) stopTimer();
            else if (view.getId() == returnButton.getId()) returnButton();
            else DataHolder.getInstance().setDisableButtonClick(false);
        }
    }

    /**
     * Create and add new timer in list
     */
    private void addTimer() {
        View timePickerPopupWindowView = getLayoutInflater().inflate(R.layout.timer_picker_popup, (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), false);
        PopupWindow timePickerPopupWindow = new TimePickerPopup(timePickerPopupWindowView, recyclerAdapter);
        timePickerPopupWindow.showAtLocation(findViewById(R.id.timer_layout), Gravity.CENTER, 0, 0);
    }

    private void startPauseTimer() {
        if (timerRunning) {
            setTimerPaused();
        } else {
            timerStarted();
        }
    }

    public void timerStarted() {
        timerRunning = true;
        startPauseTimerButton.setImageResource(R.drawable.ic_round_pause);
        stopTimerButton.setVisibility(View.VISIBLE);
        addTimerButton.setVisibility(View.GONE);
        countdownTimer.startTimer(pauseTimeInMillis);
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    public void setTimerPaused() {
        startPauseTimerButton.setImageResource(R.drawable.ic_round_play_arrow);
        stopTimerButton.setVisibility(View.VISIBLE);
        addTimerButton.setVisibility(View.GONE);
        pauseTimeInMillis = countdownTimer.pauseTimer();
        indexOfTimer = countdownTimer.getIndexOfTimer();
        timerRunning = false;
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    @Override
    public void onBackPressed() {
        returnButton();
    }

    public void stopTimer() {
        addTimerButton.setVisibility(View.VISIBLE);
        startPauseTimerButton.setImageResource(R.drawable.ic_round_play_arrow);
        stopTimerButton.setVisibility(View.GONE);
        pauseTimeInMillis = ConstantsClass.ZERO_LONG;
        indexOfTimer = ConstantsClass.ZERO;
        countdownTimer.stopTimer();
        timerRunning = false;
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    private void returnButton() {
        if (!DataHolder.getInstance().getStackNavigation().empty()) {
            stopTimer();
            DataHolder.getInstance().getStackNavigation().pop();
            if (DataHolder.getInstance().getStackNavigation().empty()) {
                DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups());
                super.onBackPressed();
            } else {
                if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()))
                    DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup());
                else DataHolder.getInstance().setListTimerGroup(new ArrayList<>());
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        DataHolder.getInstance().setDisableButtonClick(false);
    }
}
