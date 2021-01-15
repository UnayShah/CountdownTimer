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
import com.google.android.material.textview.MaterialTextView;

public class repsFragmentInflater extends Fragment implements View.OnClickListener {

    MaterialTextView repsTextView;
    MaterialButton increaseReps;
    MaterialButton decreaseReps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reps_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        repsTextView = view.findViewById(R.id.reps_textView);
        increaseReps = view.findViewById(R.id.increase_reps);
        decreaseReps = view.findViewById(R.id.decrease_reps);

        increaseReps.setOnClickListener(this);
        decreaseReps.setOnClickListener(this);

        setRepsTextView();
    }

    private void increaseReps() {
        DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).incrementReps();
        setRepsTextView();
    }

    private void decreaseReps() {
        DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).decrementReps();
        setRepsTextView();
    }

    private void setRepsTextView() {
        repsTextView.setText(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getReps().toString());
        DataHolder.getInstance().setDisableButtonClick(false);
        DataHolder.getInstance().saveData(getContext());
    }

    @Override
    public void onClick(View v) {
        if (!DataHolder.getInstance().getDisableButtonClick()) {
            if (v.getId() == increaseReps.getId()) increaseReps();
            else if (v.getId() == decreaseReps.getId()) decreaseReps();
            else DataHolder.getInstance().setDisableButtonClick(false);
        }
    }
}
