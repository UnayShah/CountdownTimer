package com.UnayShah.countdownTimer;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.UnayShah.countdownTimer.common.CustomFragmentPagerAdapter;
import com.UnayShah.countdownTimer.common.DataHolder;
import com.google.android.material.button.MaterialButton;

public class TutorialActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    ProgressBar progressBar;
    ViewPager viewPager;
    FragmentPagerAdapter fragmentPagerAdapter;
    MaterialButton tutorialNext;
    MaterialButton tutorialPrev;
    Integer item;

    @Override
    protected void onResume() {
        if (DataHolder.getInstance().getThemeMode() != AppCompatDelegate.getDefaultNightMode())
            AppCompatDelegate.setDefaultNightMode(DataHolder.getInstance().getThemeMode());
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
        fragmentPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        tutorialNext = findViewById(R.id.tutorial_next);
        tutorialPrev = findViewById(R.id.tutorial_previous);
        progressBar = findViewById(R.id.tutorial_progressBar);
        progressBar.setMax(6);
        progressBar.setProgressTintList(DataHolder.getInstance().getAccentColor(getApplicationContext()));
        progressBar.setProgressBackgroundTintList(DataHolder.getInstance().backgroundTintDark(getApplicationContext()));
        viewPager.setCurrentItem(item);
        tutorialNext.setOnClickListener(this);
        tutorialPrev.setOnClickListener(this);
        tutorialNext.setBackgroundTintList(DataHolder.getInstance().getAccentColor(getApplicationContext()));
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
        progressBar.setProgress(item + 1);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}