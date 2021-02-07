package com.UnayShah.countdownTimer;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.UnayShah.countdownTimer.common.CustomPagerAdapter;
import com.UnayShah.countdownTimer.model.DataHolder;
import com.google.android.material.button.MaterialButton;

public class TutorialActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    ViewPager viewPager;
    MaterialButton tutorialNext;
    MaterialButton tutorialPrev;
    Integer item;

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setStatusBarColor(DataHolder.getInstance().getAccentColorColor(getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);

        item = 0;
        viewPager = findViewById(R.id.tutorial_viewPager);
        viewPager.setAdapter(new CustomPagerAdapter(getApplicationContext()));
        tutorialNext = findViewById(R.id.tutorial_next);
        tutorialPrev = findViewById(R.id.tutorial_previous);
        viewPager.setCurrentItem(item);
        tutorialNext.setOnClickListener(this);
        tutorialPrev.setOnClickListener(this);
        tutorialNext.setIconTint(DataHolder.getInstance().getAccentColor(getApplicationContext()));
        viewPager.addOnPageChangeListener(this);
        tutorialPrev.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == tutorialNext.getId() && item == 5) super.onBackPressed();
        if (v.getId() == tutorialNext.getId()) item++;
        else if (v.getId() == tutorialPrev.getId()) item--;
        viewPager.setCurrentItem(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        item = viewPager.getCurrentItem();
        if (item == 0)
            tutorialPrev.setVisibility(View.GONE);
        else
            tutorialPrev.setVisibility(View.VISIBLE);
        if (item == 5)
            tutorialNext.setText(R.string.done);
        else
            tutorialNext.setText(R.string.next);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}