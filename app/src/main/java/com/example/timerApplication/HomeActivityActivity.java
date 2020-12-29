package com.example.timerApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerApplication.common.ConstantsClass;
import com.example.timerApplication.model.DataHolder;
import com.example.timerApplication.timers.TimerGroup;
import com.example.timerApplication.timers.TimerGroupType;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HomeActivityActivity extends AppCompatActivity implements View.OnClickListener {

    final Gson gson = new Gson();
    ImageButton homeAddButton;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        init();
    }

    private void init() {
        homeAddButton = findViewById(R.id.home_add_button);
        homeAddButton.setOnClickListener(this);
        recyclerView = findViewById(R.id.timerGroupScrollViewRecyclerView);
        recyclerAdapter = new RecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(ConstantsClass.HOME_LIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ConstantsClass.HOME_LIST);
        editor.clear();
        editor.putString(ConstantsClass.HOME_LIST, gson.toJson(DataHolder.getInstance().getAllTimerGroups()));
        editor.apply();
        editor.commit();
    }

    private void loadData() {
        if (DataHolder.getInstance().getAllTimerGroups() != null && DataHolder.getInstance().getAllTimerGroups().size() > 0)
            DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups());
        else {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(ConstantsClass.HOME_LIST, Context.MODE_PRIVATE);
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
    }
}
