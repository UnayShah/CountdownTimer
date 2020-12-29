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
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerApplication.common.ConstantsClass;
import com.example.timerApplication.countdowntimer.CountdownTimer;
import com.example.timerApplication.countdowntimer.CountdownTimerFactory;
import com.example.timerApplication.model.DataHolder;
import com.example.timerApplication.timers.Timer;
import com.example.timerApplication.timers.TimerGroup;
import com.example.timerApplication.timers.TimerGroupType;

import java.util.ArrayList;
import java.util.LinkedList;


public class TimerActivity extends Fragment implements View.OnClickListener, IStartDragListener, FragmentManager.OnBackStackChangedListener {

    public static Boolean timerRunning;
    public static Boolean looped;
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
        recyclerView = view.findViewById(R.id.timers_scrollView_recyclerView);
        recyclerAdapter = new RecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(recyclerAdapter);

        timerTextView = view.findViewById(R.id.timer_textView);
        timerTextView.setText(new Timer().toString());
        addTimerButton = view.findViewById(R.id.add_button);
        startPauseTimerButton = view.findViewById(R.id.start_pause_button);
        returnButton = view.findViewById(R.id.return_button);
        scrollViewTimers = view.findViewById(R.id.timers_scrollView);
        stopTimerButton = view.findViewById(R.id.stop_button);
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
        recyclerAdapter.setFromStorage(true);
        recyclerAdapter.setFromHome(false);

        if (!DataHolder.getInstance().getStackNavigation().isEmpty()) {
            if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()))
                DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek()).intValue()).getListTimerGroup());
            else DataHolder.getInstance().setListTimerGroup(new ArrayList<>());
            recyclerAdapter.setFromHome(false);
            recyclerAdapter.setFromStorage(true);
            recyclerAdapter.notifyDataSetChanged();
        }
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
            else if (view.getId() == startPauseTimerButton.getId()) startPauseTimer();
            else if (view.getId() == stopTimerButton.getId()) stopTimer();
            else if (view.getId() == returnButton.getId()) returnButton();
        }
    }

    /**
     * Create and add new timer in list
     */
    private void addTimer() {
        recyclerAdapter.setFromHome(false);
        recyclerAdapter.setNewItem(true);
        recyclerAdapter.setFromStorage(false);
        DataHolder.getInstance().getListTimerGroup().add(new TimerGroup(TimerGroupType.TIMER));
        recyclerAdapter.notifyItemInserted(DataHolder.getInstance().getListTimerGroup().size());
    }

    private void startPauseTimer() {
        if (DataHolder.getInstance().getQueueTimers().isEmpty() || DataHolder.getInstance().getQueueTimers().peek() == null) {
            DataHolder.getInstance().setDisableButtonClick(false);
        } else {
            if (DataHolder.getInstance().getListTimerGroup().size() > 0) {
                if (timerRunning) {
                    setTimerPaused();
                } else {
                    timerStarted();
                }
                timerRunning = !timerRunning;
            }
            DataHolder.getInstance().setDisableButtonClick(false);
        }
    }

    public void timerStarted() {
        startPauseTimerButton.setImageResource(R.drawable.ic_round_pause);
        stopTimerButton.setVisibility(View.VISIBLE);
        addTimerButton.setVisibility(View.GONE);
        countdownTimer.startTimer(pauseTimeInMillis);
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
        DataHolder.getInstance().setQueueTimers(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getTimersQueue());
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    private void returnButton() {
        stopTimer();
        DataHolder.getInstance().getQueueTimers().removeAll(new LinkedList<Timer>());
        recyclerAdapter.setFromStorage(true);
        if (!DataHolder.getInstance().getStackNavigation().empty()) {
            DataHolder.getInstance().getStackNavigation().pop();
            if (DataHolder.getInstance().getStackNavigation().empty()) {
                DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups());
                recyclerAdapter.setFromHome(true);
                getParentFragmentManager().popBackStack();
                Navigation.createNavigateOnClickListener(R.id.action_timerActivity_to_homeActivity).onClick(returnButton);
            } else {
                recyclerAdapter.setFromHome(false);
                if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()))
                    DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup());
                else DataHolder.getInstance().setListTimerGroup(new ArrayList<>());
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    @Override
    public void onBackStackChanged() {
        stopTimer();
        DataHolder.getInstance().getQueueTimers().removeAll(new LinkedList<Timer>());
        recyclerAdapter.setFromStorage(true);
        if (!DataHolder.getInstance().getStackNavigation().empty()) {
            DataHolder.getInstance().getStackNavigation().pop();
            if (DataHolder.getInstance().getStackNavigation().empty()) {
                DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups());
                recyclerAdapter.setFromHome(true);
                getParentFragmentManager().popBackStack();
                Navigation.createNavigateOnClickListener(R.id.action_timerActivity_to_homeActivity).onClick(returnButton);
            } else {
                recyclerAdapter.setFromHome(false);
                if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()))
                    DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup());
                else DataHolder.getInstance().setListTimerGroup(new ArrayList<>());
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        DataHolder.getInstance().setDisableButtonClick(false);
    }
}
