package com.UnayShah.countdownTimer;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.UnayShah.countdownTimer.common.ConstantsClass;
import com.UnayShah.countdownTimer.countdowntimer.CountdownTimer;
import com.UnayShah.countdownTimer.countdowntimer.CountdownTimerFactory;
import com.UnayShah.countdownTimer.model.CustomAnimations;
import com.UnayShah.countdownTimer.model.DataHolder;
import com.UnayShah.countdownTimer.popupactivity.TimePickerPopup;
import com.UnayShah.countdownTimer.timers.Timer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener, IStartDragListener {
    public static Boolean timerRunning = false;
    ConstraintLayout emptyHolder;
    MaterialButton addTimerButton;
    MaterialButton startPauseTimerButton;
    MaterialButton stopTimerButton;
    MaterialButton homeButton;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ItemTouchHelper itemTouchHelper;
    MaterialTextView timerTextView;
    CountdownTimer countdownTimer;
    Long pauseTimeInMillis;
    Integer indexOfTimer;
    View timerLayout;
    AdView adView;
    MaterialToolbar timerToolbar;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_layout);
        init();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onWindowFocusChanged(hasFocus);
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
        addTimerButton = findViewById(R.id.home_add_button);
        startPauseTimerButton = findViewById(R.id.start_pause_button);
        homeButton = findViewById(R.id.home_button);
        stopTimerButton = findViewById(R.id.stop_button);
        emptyHolder = findViewById(R.id.empty_holder);
        timerToolbar = findViewById(R.id.timer_toolbar);
        timerLayout = findViewById(R.id.timer_layout);

        ItemTouchHelper.Callback callback = new ItemMoveCallback(recyclerAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        startPauseTimerButton.setOnClickListener(this);
        stopTimerButton.setOnClickListener(this);
        stopTimerButton.setVisibility(View.INVISIBLE);
        addTimerButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);

        countdownTimer = CountdownTimerFactory.getInstance(this);
        timerRunning = false;

        pauseTimeInMillis = ConstantsClass.ZERO_LONG;
        indexOfTimer = ConstantsClass.ZERO;

        stopTimerButton.setVisibility(View.GONE);
        homeButton.setVisibility(View.VISIBLE);
        startPauseTimerButton.setVisibility(View.VISIBLE);
        addTimerButton.setVisibility(View.VISIBLE);

        if (!DataHolder.getInstance().getStackNavigation().isEmpty()) {
            timerToolbar.setTitle(DataHolder.getInstance().getStackNavigation().peek());
            if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()) && DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek()) != null)
                DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup());
            else DataHolder.getInstance().setListTimerGroup(new ArrayList<>());
            emptyHolderVisibility();
            recyclerAdapter.notifyDataSetChanged();
        }
        startPauseTimerButton.setIconTint(DataHolder.getInstance().getAccentColor(getApplicationContext()));
        setSupportActionBar(timerToolbar);
        initTransitionAnimations(recyclerView, startPauseTimerButton);
        DataHolder.getInstance().setDisableButtonClick(false);
        timerToolbar.setNavigationOnClickListener(v -> returnButton());
        timerStarted();
        stopTimer();
    }

    public void setText(String s) {
        timerTextView.setText(s);
    }

    private void initTransitionAnimations(View... view) {
        for (View v : view) {
            new CustomAnimations().slideUp(v);
        }
    }

    private void endTransitionAnimations(View... view) {
        for (View v : view) {
            new CustomAnimations().slideDown(v);
        }
    }

    private void emptyHolderVisibility() {
        if (DataHolder.getInstance().getListTimerGroup().size() <= 0)
            emptyHolder.setVisibility(View.VISIBLE);
        else emptyHolder.setVisibility(View.GONE);
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
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            DataHolder.getInstance().setDisableButtonClick(true);
            if (view.getId() == addTimerButton.getId()) addTimer();
            else if (view.getId() == startPauseTimerButton.getId()) {
                if (DataHolder.getInstance().getListTimerGroup().size() > 0)
                    startPauseTimer();
                else DataHolder.getInstance().setDisableButtonClick(false);
            } else if (view.getId() == stopTimerButton.getId()) stopTimer();
            else if (view.getId() == homeButton.getId())
                homeButton();
            else {
                DataHolder.getInstance().setDisableButtonClick(false);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
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

    private void timerStarted() {
        timerRunning = true;
        startPauseTimerButton.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_round_pause));
        stopTimerButton.setVisibility(View.VISIBLE);
        addTimerButton.setVisibility(View.GONE);
        countdownTimer.startTimer(pauseTimeInMillis);
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    private void setTimerPaused() {
        startPauseTimerButton.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_round_play_arrow));
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        countdownTimer.repsSetOne();
        recyclerAdapter.notifyDataSetChanged();
        addTimerButton.setVisibility(View.VISIBLE);
        startPauseTimerButton.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_round_play_arrow));
        stopTimerButton.setVisibility(View.GONE);
        pauseTimeInMillis = ConstantsClass.ZERO_LONG;
        indexOfTimer = ConstantsClass.ZERO;
        countdownTimer.stopTimer();
        timerRunning = false;
        DataHolder.getInstance().setDisableButtonClick(false);
        setText(new Timer().toString());
    }

    private void homeButton() {
        stopTimer();
        DataHolder.getInstance().getStackNavigation().clear();
        endTransitionAnimations(recyclerView, startPauseTimerButton);
        DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onBackPressed();
    }

    private void returnButton() {
        if (!DataHolder.getInstance().getStackNavigation().empty()) {
            stopTimer();
            DataHolder.getInstance().getStackNavigation().pop();
            if (DataHolder.getInstance().getStackNavigation().empty()) {
                endTransitionAnimations(recyclerView, startPauseTimerButton);
                DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups());
                super.onBackPressed();
            } else {
                endTransitionAnimations(recyclerView);
                if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()))
                    DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup());
                else DataHolder.getInstance().setListTimerGroup(new ArrayList<>());
                if (findViewById(R.id.loop_button) != null) {
                    findViewById(R.id.loop_button).callOnClick();
                    findViewById(R.id.loop_button).callOnClick();
                }
                if (findViewById(R.id.increase_reps) != null && findViewById(R.id.decrease_reps) != null) {
                    findViewById(R.id.increase_reps).callOnClick();
                    findViewById(R.id.decrease_reps).callOnClick();
                }
                timerToolbar.setTitle(DataHolder.getInstance().getStackNavigation().peek());
                initTransitionAnimations(recyclerView);
                recyclerAdapter.notifyDataSetChanged();
                emptyHolderVisibility();
            }
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        DataHolder.getInstance().setDisableButtonClick(false);
    }


}
