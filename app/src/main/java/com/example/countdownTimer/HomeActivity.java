package com.example.countdownTimer;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countdownTimer.model.CustomAnimations;
import com.example.countdownTimer.model.DataHolder;
import com.example.countdownTimer.popupactivity.TimerNamePopup;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout emptyHolder;
    ImageButton homeAddButton;
    ImageButton returnButton;
    ImageButton settingsButton;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    AdView adView;

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

    @Override
    public void onBackPressed() {
        DataHolder.getInstance().setDisableButtonClick(false);
        super.onBackPressed();
    }

    private void init() {
        adView = findViewById(R.id.adView_home);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        emptyHolder = findViewById(R.id.empty_holder);
        if (DataHolder.getInstance().getListTimerGroup().size() > 0)
            emptyHolder.setVisibility(View.VISIBLE);
        else emptyHolder.setVisibility(View.INVISIBLE);

        homeAddButton = findViewById(R.id.home_add_button);
        returnButton = findViewById(R.id.return_button);
        settingsButton = findViewById(R.id.settings_button);
        homeAddButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);

        recyclerView = findViewById(R.id.timerGroupScrollViewRecyclerView);
        recyclerAdapter = new RecyclerAdapter(this);
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
            else if (view.getId() == returnButton.getId()) onBackPressed();
            else DataHolder.getInstance().setDisableButtonClick(false);
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
        new CustomAnimations().initTransitionAnimations(findViewById(R.id.title_image), recyclerView, returnButton, homeAddButton, settingsButton);
    }
}
