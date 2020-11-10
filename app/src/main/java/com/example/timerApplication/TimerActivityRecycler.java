package com.example.timerApplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerApplication.Timers.Timer;

import java.util.ArrayList;
import java.util.List;

public class TimerActivityRecycler extends Fragment implements View.OnClickListener, IStartDragListener {

    LinearLayout linearLayoutTimers;
    Button addTimerButton;
    Button startPauseTimerButton;
    Button stopTimerButton;
    ScrollView scrollViewTimers;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerViewAdapter;
    ItemTouchHelper itemTouchHelper;
    static List<Timer> listTimers = new ArrayList<>();

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
     * @param view
     */
    public void init(View view) {
        linearLayoutTimers = view.findViewById(R.id.timers_scrollView_linearLayout);
        addTimerButton = view.findViewById(R.id.add_button);
        startPauseTimerButton = view.findViewById(R.id.start_pause_button);
        scrollViewTimers = view.findViewById(R.id.timers_scrollView);
        stopTimerButton = view.findViewById(R.id.stop_button);
        recyclerView = view.findViewById(R.id.timers_scrollView_recyclerView);
        recyclerViewAdapter = new RecyclerAdapter(listTimers, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ItemTouchHelper.Callback callback = new ItemMoveCallback(recyclerViewAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
        startPauseTimerButton.setOnClickListener(this);
        stopTimerButton.setOnClickListener(this);
        addTimerButton.setOnClickListener(this);
    }

    public void requestDrag(RecyclerAdapter.ViewHolder viewHolder){
        itemTouchHelper.startDrag(viewHolder);
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
                System.out.println("START");
                startPauseTimer();
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
        listTimers.add(new Timer());
        recyclerViewAdapter.notifyItemInserted(listTimers.size());
    }

    private void startPauseTimer() {
        for (Timer t : listTimers) {
            System.out.println(t.toString());
        }
    }

    public static void setListTimers(List<Timer> listTimersExt) {
        listTimers = listTimersExt;
    }

}
