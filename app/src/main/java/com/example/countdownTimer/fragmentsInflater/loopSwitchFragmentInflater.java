package com.example.countdownTimer.fragmentsInflater;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.example.countdownTimer.R;
import com.example.countdownTimer.model.DataHolder;

public class loopSwitchFragmentInflater extends Fragment implements View.OnClickListener {

    ImageButton loopButton;

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
        loopButton.setImageDrawable(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getLooped() ? AppCompatResources.getDrawable(getContext(), R.drawable.ic_round_loop_accent) : AppCompatResources.getDrawable(getContext(), R.drawable.ic_round_loop));
    }

    @Override
    public void onClick(View v) {
        DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).setLooped(!DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getLooped());
        loopButton.setImageDrawable(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getLooped() ? AppCompatResources.getDrawable(getContext(), R.drawable.ic_round_loop_accent) : AppCompatResources.getDrawable(getContext(), R.drawable.ic_round_loop));
//        Toast.makeText(getContext(), DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getLooped() ? ConstantsClass.LOOPED_COUNTDOWN_TOAST : ConstantsClass.UNLOOPED_COUNTDOWN_TOAST, Toast.LENGTH_SHORT).show();
    }
}
