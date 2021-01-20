package com.example.countdownTimer;

import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.countdownTimer.model.DataHolder;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textview.MaterialTextView;

import top.defaults.colorpicker.ColorPickerPopup;

public class CommonSettings extends AppCompatActivity implements View.OnClickListener, MaterialButtonToggleGroup.OnButtonCheckedListener {

    private MaterialTextView accentColourTextView;
    private MaterialTextView resetTextView;
    private ImageView accentColourImageView;
    private LinearLayout accentColourLayout;
    private MaterialButtonToggleGroup vibrationButtonGroup;
    private MaterialButton vibrationOffButton;
    private MaterialButton vibrationOnButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_settings);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        accentColourLayout = findViewById(R.id.accent_color_view);
        accentColourTextView = findViewById(R.id.accent_color_textView);
        resetTextView = findViewById(R.id.reset_textView);
        accentColourImageView = findViewById(R.id.accent_color_preview);
        vibrationButtonGroup = findViewById(R.id.vibration_switch);
        vibrationOffButton = findViewById(R.id.vibration_switch_off);
        vibrationOnButton = findViewById(R.id.vibration_switch_on);
        MaterialToolbar timerToolbar = findViewById(R.id.timer_toolbar);
        setSupportActionBar(timerToolbar);
        vibrationButtonGroup.setSingleSelection(true);

        accentColourLayout.setOnClickListener(this);
        accentColourTextView.setOnClickListener(this);
        resetTextView.setOnClickListener(this);
        accentColourImageView.setOnClickListener(this);
        vibrationButtonGroup.addOnButtonCheckedListener(this);
        timerToolbar.setNavigationOnClickListener(v -> onBackPressed());

        accentColourTextView.setFocusable(true);
        accentColourTextView.setClickable(true);

        GradientDrawable accentDrawable = new GradientDrawable();
        accentDrawable.setShape(GradientDrawable.OVAL);
        accentDrawable.setColor(DataHolder.getInstance().getAccentColor(getApplicationContext()));
        accentDrawable.setSize(200, 200);
        accentColourImageView.setImageDrawable(accentDrawable);

        initVibrationGroup();
    }

    private void initVibrationGroup() {
        vibrationOffButton.setStrokeColor(DataHolder.getInstance().getAccentColor(getApplicationContext()));
        vibrationOffButton.setStrokeWidth((int) getResources().getDimension(R.dimen.padding_vvsmall));
        vibrationOffButton.setCornerRadius((int) getResources().getDimension(R.dimen.padding_huge));

        vibrationOnButton.setStrokeColor(DataHolder.getInstance().getAccentColor(getApplicationContext()));
        vibrationOnButton.setStrokeWidth((int) getResources().getDimension(R.dimen.padding_vvsmall));
        vibrationOnButton.setCornerRadius((int) getResources().getDimension(R.dimen.padding_huge));

        if (DataHolder.getInstance().getVibration(getApplicationContext())) {
            vibrationOnButton.setBackgroundTintList(DataHolder.getInstance().getAccentColor(getApplicationContext()));
            vibrationOffButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.iconTintDark)));
        } else {
            vibrationOffButton.setBackgroundTintList(DataHolder.getInstance().getAccentColor(getApplicationContext()));
            vibrationOnButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.iconTintDark)));
        }
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    @Override
    public void onClick(View v) {
        if (!DataHolder.getInstance().getDisableButtonClick()) {
            DataHolder.getInstance().setDisableButtonClick(true);
            if (v.getId() == accentColourLayout.getId() || v.getId() == accentColourTextView.getId() || v.getId() == accentColourImageView.getId()) {
                colorPicker(v);
            } else if (v.getId() == resetTextView.getId()) {
                reset();
            } else DataHolder.getInstance().setDisableButtonClick(false);
        }
    }

    private void reset() {
        DataHolder.getInstance().setAccentColor(getApplicationContext(), ContextCompat.getColor(getApplicationContext(), R.color.accent));
        accentColourImageView.setImageTintList(DataHolder.getInstance().getAccentColor(getApplicationContext()));
        vibrationButtonGroup.check(vibrationOnButton.getId());
        DataHolder.getInstance().setVibration(getApplicationContext(), true);
        initVibrationGroup();
        DataHolder.getInstance().setDisableButtonClick(true);
    }

    private void colorPicker(View v) {
        DataHolder.getInstance().setDisableButtonClick(false);
        new ColorPickerPopup.Builder(this).initialColor(
                DataHolder.getInstance().getAccentColorColor(getApplicationContext())) // set initial color
                .enableBrightness(true).enableAlpha(true).okTitle("Choose").cancelTitle("Cancel").showIndicator(true).showValue(true).build().show(v, new ColorPickerPopup.ColorPickerObserver() {
            @Override
            public void
            onColorPicked(int color) {
                DataHolder.getInstance().setAccentColor(getApplicationContext(), color);
                accentColourImageView.setImageTintList(DataHolder.getInstance().getAccentColor(getApplicationContext()));
                initVibrationGroup();
            }
        });
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        DataHolder.getInstance().setDisableButtonClick(true);
        DataHolder.getInstance().setVibration(getApplicationContext(), checkedId == vibrationOnButton.getId());
        initVibrationGroup();
    }
}

