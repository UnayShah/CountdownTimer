package com.example.timerApplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerApplication.common.ConstantsClass;
import com.example.timerApplication.countdowntimer.CountdownTimer;
import com.example.timerApplication.countdowntimer.CountdownTimerFactory;
import com.example.timerApplication.timers.IListTimers;
import com.example.timerApplication.timers.ListTimersImpl;
import com.example.timerApplication.timers.Timer;

public class TimerActivity extends Fragment implements View.OnClickListener, IStartDragListener {

    ImageButton addTimerButton;
    ImageButton startPauseTimerButton;
    ImageButton stopTimerButton;
    ImageButton returnButton;
    ScrollView scrollViewTimers;
    RecyclerView recyclerView;
    RecyclerAdapterTimer recyclerViewAdapter;
    ItemTouchHelper itemTouchHelper;
    TextView timerTextView;
    CountdownTimer countdownTimer;
    public static Boolean timerRunning;
    Long pauseTimeInMillis;
    Integer indexOfTimer;
    public static IListTimers listTimers;
    public static Boolean looped;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.timer_layout_recycler, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    /**
     * Initialize view objects
     *
     * @param view of layout
     */
    public void init(View view) {
        looped = false;
        listTimers = new ListTimersImpl();
        timerTextView = view.findViewById(R.id.timer_textView);
        timerTextView.setText(new Timer().toString());
        addTimerButton = view.findViewById(R.id.add_button);
        startPauseTimerButton = view.findViewById(R.id.start_pause_button);
        returnButton = view.findViewById(R.id.return_button);
        scrollViewTimers = view.findViewById(R.id.timers_scrollView);
        stopTimerButton = view.findViewById(R.id.stop_button);
        recyclerView = view.findViewById(R.id.timers_scrollView_recyclerView);
        recyclerViewAdapter = new RecyclerAdapterTimer(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ItemTouchHelper.Callback callback = new ItemMoveCallback(recyclerViewAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
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
    }

    public void requestDrag(RecyclerAdapterTimer.ViewHolder viewHolder) {
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
        if (view.getId() == addTimerButton.getId()) addTimer();
        else if (view.getId() == startPauseTimerButton.getId()) startPauseTimer();
        else if (view.getId() == stopTimerButton.getId()) stopTimer();
        else if (view.getId() == returnButton.getId()) returnButton();
        else System.out.println(ConstantsClass.NONE_STRING);
    }

    /**
     * Create and add new timer in list
     */
    private void addTimer() {
        listTimers.add(new Timer());
        recyclerViewAdapter.notifyItemInserted(listTimers.size());
    }

    private void startPauseTimer() {
        if (listTimers.size() > 0) {
            if (timerRunning) {
                setTimerPaused();
            } else {
                timerStarted();
            }
            timerRunning = !timerRunning;
        }
    }

    public void timerStarted() {
        startPauseTimerButton.setImageResource(R.drawable.ic_round_pause);
        stopTimerButton.setVisibility(View.VISIBLE);
        addTimerButton.setVisibility(View.GONE);
        countdownTimer.startTimer(indexOfTimer, pauseTimeInMillis);
    }

    public void setTimerPaused() {
        startPauseTimerButton.setImageResource(R.drawable.ic_round_play_arrow);
        pauseTimeInMillis = countdownTimer.pauseTimer();
        indexOfTimer = countdownTimer.getIndexOfTimer();
    }

    public void stopTimer() {
        addTimerButton.setVisibility(View.VISIBLE);
        startPauseTimerButton.setImageResource(R.drawable.ic_round_play_arrow);
        stopTimerButton.setVisibility(View.GONE);
        timerRunning = false;
        pauseTimeInMillis = ConstantsClass.ZERO_LONG;
        indexOfTimer = ConstantsClass.ZERO;
        countdownTimer.stopTimer();
    }

    private void returnButton() {
        stopTimer();
        System.exit(ConstantsClass.ZERO);
    }
}
