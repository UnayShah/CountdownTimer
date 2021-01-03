package com.example.timerApplication;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerApplication.model.DataHolder;
import com.example.timerApplication.popupactivity.TimerNamePopup;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton homeAddButton;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        init();
    }

    @Override
    protected void onResume() {
        recyclerAdapter.notifyDataSetChanged();
        super.onResume();
    }

    private void init() {
        homeAddButton = findViewById(R.id.home_add_button);
        homeAddButton.setOnClickListener(this);
        recyclerView = findViewById(R.id.timerGroupScrollViewRecyclerView);
        recyclerAdapter = new RecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
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
        View timerNamePopupWindowView = getLayoutInflater().inflate(R.layout.timer_name_popup, (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), false);
        PopupWindow timerNamePopupWindow = new TimerNamePopup(timerNamePopupWindowView, recyclerAdapter);
        timerNamePopupWindow.showAtLocation(findViewById(R.id.home_screen), Gravity.CENTER, 0, 0);
    }

    private void loadData() {
        DataHolder.getInstance().loadData(getApplicationContext());
        recyclerAdapter.notifyDataSetChanged();
    }
}
