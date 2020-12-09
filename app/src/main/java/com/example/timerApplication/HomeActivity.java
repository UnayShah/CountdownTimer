package com.example.timerApplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerApplication.timers.TimerGroup;
import com.example.timerApplication.timers.TimerGroupType;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Fragment implements View.OnClickListener {
    ImageButton homeAddButton;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    public static List<TimerGroup> listTimerGroup = new ArrayList<>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_screen, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        homeAddButton = view.findViewById(R.id.home_add_button);
        homeAddButton.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.timerGroupScrollViewRecyclerView);
        recyclerAdapter = new RecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == homeAddButton.getId())addTimerGroup();
    }
    public void addTimerGroup(){
        listTimerGroup.add(new TimerGroup(TimerGroupType.TIMER_GROUP));
        recyclerAdapter.setFromHome(true);
        recyclerAdapter.notifyItemInserted(listTimerGroup.size());
    }
}
