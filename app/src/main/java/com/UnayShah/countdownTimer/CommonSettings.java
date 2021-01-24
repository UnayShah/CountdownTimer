package com.UnayShah.countdownTimer;

import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.UnayShah.countdownTimer.model.DataHolder;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textview.MaterialTextView;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class CommonSettings extends AppCompatActivity implements View.OnClickListener, MaterialButtonToggleGroup.OnButtonCheckedListener {

    MaterialTextView vibrationTextView;
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
        vibrationTextView = findViewById(R.id.vibration_textView);
        accentColourImageView = findViewById(R.id.accent_color_preview);
        vibrationButtonGroup = findViewById(R.id.vibration_switch);
        vibrationOffButton = findViewById(R.id.vibration_switch_off);
        vibrationOnButton = findViewById(R.id.vibration_switch_on);
        MaterialToolbar timerToolbar = findViewById(R.id.timer_toolbar);
        setSupportActionBar(timerToolbar);
        vibrationButtonGroup.setSingleSelection(true);

        accentColourTextView.setFocusable(true);
        accentColourTextView.setClickable(true);
        vibrationTextView.setClickable(true);
        vibrationTextView.setFocusable(true);

        accentColourLayout.setOnClickListener(this);
        accentColourTextView.setOnClickListener(this);
        resetTextView.setOnClickListener(this);
        vibrationTextView.setOnClickListener(this);
        accentColourImageView.setOnClickListener(this);
        vibrationButtonGroup.addOnButtonCheckedListener(this);
        timerToolbar.setNavigationOnClickListener(v -> onBackPressed());


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

        vibrationOnButton.setRippleColor(DataHolder.getInstance().getAccentColor(getApplicationContext()));
        vibrationOffButton.setRippleColor(DataHolder.getInstance().getAccentColor(getApplicationContext()));

        if (DataHolder.getInstance().getVibration(getApplicationContext())) {
            vibrationOnButton.setBackgroundTintList(DataHolder.getInstance().getAccentColor(getApplicationContext()));
            vibrationOffButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.iconTintDark)));
            vibrationButtonGroup.check(vibrationOnButton.getId());
        } else {
            vibrationOffButton.setBackgroundTintList(DataHolder.getInstance().getAccentColor(getApplicationContext()));
            vibrationOnButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.iconTintDark)));
            vibrationButtonGroup.check(vibrationOffButton.getId());
        }
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    @Override
    public void onClick(View v) {
        if (!DataHolder.getInstance().getDisableButtonClick()) {
            DataHolder.getInstance().setDisableButtonClick(true);
            if (v.getId() == accentColourLayout.getId() || v.getId() == accentColourTextView.getId() || v.getId() == accentColourImageView.getId()) {
                colorPicker();
            } else if (v.getId() == resetTextView.getId()) {
                reset();
            } else if (v.getId() == vibrationTextView.getId()) {
                if (vibrationButtonGroup.getCheckedButtonId() == vibrationOnButton.getId())
                    vibrationButtonGroup.check(vibrationOffButton.getId());
                else if (vibrationButtonGroup.getCheckedButtonId() == vibrationOffButton.getId())
                    vibrationButtonGroup.check(vibrationOnButton.getId());
            } else DataHolder.getInstance().setDisableButtonClick(false);
        }
    }

    private void reset() {
        DataHolder.getInstance().setAccentColor(getApplicationContext(), ContextCompat.getColor(getApplicationContext(), R.color.accent));
        accentColourImageView.setImageTintList(DataHolder.getInstance().getAccentColor(getApplicationContext()));
        vibrationButtonGroup.check(vibrationOnButton.getId());
        DataHolder.getInstance().setVibration(getApplicationContext(), true);
        initVibrationGroup();
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    private void colorPicker() {
        DataHolder.getInstance().setDisableButtonClick(false);
        new ColorPickerDialog.Builder(this)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(R.string.set,
                        (ColorEnvelopeListener) (envelope, fromUser) -> {
                            DataHolder.getInstance().setAccentColor(getApplicationContext(), envelope.getColor());
                            accentColourImageView.setImageTintList(DataHolder.getInstance().getAccentColor(getApplicationContext()));
                            initVibrationGroup();
                        })
                .setNegativeButton(getString(R.string.cancel),
                        (dialogInterface, i) -> dialogInterface.dismiss())
                .attachAlphaSlideBar(false)
                .attachBrightnessSlideBar(false)
                .setBottomSpace(12)
                .show();
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        DataHolder.getInstance().setDisableButtonClick(true);
        DataHolder.getInstance().setVibration(getApplicationContext(), checkedId == vibrationOnButton.getId());
        initVibrationGroup();
    }
}

