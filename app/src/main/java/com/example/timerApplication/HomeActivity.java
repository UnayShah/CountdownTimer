package com.example.timerApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerApplication.common.ConstantsClass;
import com.example.timerApplication.model.DataHolder;
import com.example.timerApplication.timers.TimerGroup;
import com.example.timerApplication.timers.TimerGroupType;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HomeActivity extends Fragment implements View.OnClickListener {
    ImageButton homeAddButton;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    final Gson gson = new Gson();

    public HomeActivity returnThis() {
        return this;
    }

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
        loadData();
    }

    @Override
    public void onClick(View view) {
        if (!DataHolder.getInstance().getDisableButtonClick()) {
            DataHolder.getInstance().setDisableButtonClick(true);
            if (view.getId() == homeAddButton.getId()) addTimerGroup();
        }
    }

    public void addTimerGroup() {
        DataHolder.getInstance().getListTimerGroup().add(new TimerGroup(TimerGroupType.TIMER_GROUP));
        recyclerAdapter.setFromHome(true);
        recyclerAdapter.setNewItem(true);
        recyclerAdapter.setFromStorage(false);
        recyclerAdapter.notifyItemInserted(DataHolder.getInstance().getListTimerGroup().size());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(ConstantsClass.HOME_LIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ConstantsClass.HOME_LIST);
        editor.clear();
        editor.putString(ConstantsClass.HOME_LIST, gson.toJson(DataHolder.getInstance().getAllTimerGroups()));
        editor.commit();

    }

    private void loadData() {
        if (DataHolder.getInstance().getAllTimerGroups() != null && DataHolder.getInstance().getAllTimerGroups().size() > 0)
            DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups());
        else {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(ConstantsClass.HOME_LIST, Context.MODE_PRIVATE);
            ArrayList<TimerGroup> list = gson.fromJson(sharedPreferences.getString(ConstantsClass.HOME_LIST, gson.toJson(new ArrayList<TimerGroup>())), new ArrayList<TimerGroup>() {
            }.getClass());
            if (list != null && list.size() > 0) {
                DataHolder.getInstance().setListTimerGroup(list);
                DataHolder.getInstance().setAllTimerGroups(list);
            } else {
                DataHolder.getInstance().setListTimerGroup(new ArrayList<>());
                DataHolder.getInstance().setAllTimerGroups(new ArrayList<>());
            }
        }
        recyclerAdapter.setFromStorage(true);
        recyclerAdapter.setFromHome(true);
        recyclerAdapter.notifyDataSetChanged();
//        recyclerAdapter.setFromStorage(false);

    }
}
