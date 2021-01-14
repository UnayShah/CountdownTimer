package com.example.countdownTimer.fragmentsInflater;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.countdownTimer.R;
import com.example.countdownTimer.model.DataHolder;
import com.google.android.material.button.MaterialButton;

public class loopSwitchFragmentInflater extends Fragment implements View.OnClickListener {

    MaterialButton loopButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loop_button, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        loopButton = view.findViewById(R.id.loop_button);
        loopButton.setOnClickListener(this);
        loopButton.setCheckable(true);
        loopButton.setIconTintResource(R.color.text);
        loopButton.setChecked(true);
        setTint();
    }

    @Override
    public void onClick(View v) {
        DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).setLooped(!DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getLooped());
        setTint();
    }

    private void setTint() {
        if (DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getLooped()) {
            loopButton.setIconTintResource(R.color.material_on_background_disabled);
            loopButton.setRippleColorResource(R.color.accent);
        } else {
            loopButton.setIconTintResource(R.color.accent);
            loopButton.setRippleColorResource(R.color.material_on_background_disabled);
        }
    }
}
