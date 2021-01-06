package com.example.countdownTimer;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countdownTimer.common.ConstantsClass;
import com.example.countdownTimer.countdowntimer.CountdownTimer;
import com.example.countdownTimer.model.DataHolder;
import com.example.countdownTimer.popupactivity.TimePickerPopup;
import com.example.countdownTimer.popupactivity.TimerNamePopup;
import com.example.countdownTimer.timers.TimerGroup;
import com.example.countdownTimer.timers.TimerGroupType;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ListItemViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {
    LayoutInflater layoutInflater;
    IStartDragListener startDragListener;
    Activity activity;

    public RecyclerAdapter(Activity activity) {
        this.activity = activity;
    }

    public RecyclerAdapter(IStartDragListener iStartDragListener, Activity activity) {
        this(activity);
        this.startDragListener = iStartDragListener;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        final View view = layoutInflater.inflate(R.layout.add_timer, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        holder.init();
        DataHolder.getInstance().updateMap();
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    @Override
    public int getItemCount() {
        DataHolder.getInstance().setDisableButtonClick(false);
        if (!DataHolder.getInstance().getStackNavigation().empty())
            ((TextView) activity.findViewById(R.id.timer_name)).setText(DataHolder.getInstance().getStackNavigation().peek());
        return DataHolder.getInstance().getListTimerGroup().size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        Collections.swap(DataHolder.getInstance().getListTimerGroup(), fromPosition, toPosition);
        this.notifyItemMoved(fromPosition, toPosition);
    }

    private void timerNamePopup(int position, View itemView) {
        View timerNamePopupWindowView = layoutInflater.inflate(R.layout.timer_name_popup, null, false);
        PopupWindow timerNamePopupWindow = new TimerNamePopup(timerNamePopupWindowView, this, position);
        timerNamePopupWindow.showAtLocation(itemView, Gravity.CENTER, 0, 0);
    }

    private void timerPickerPopup(int position, View itemView) {
        View timePickerPopupWindowView = layoutInflater.inflate(R.layout.timer_picker_popup, null, false);
        PopupWindow timePickerPopupWindow = new TimePickerPopup(timePickerPopupWindowView, this, position);
        timePickerPopupWindow.showAtLocation(itemView, Gravity.CENTER, 0, 0);
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        ImageView dragImage;
        TextView timerText;
        ImageButton button1;
        ImageButton button2;
        TimerGroupType timerGroupType;
        Pair[] animationPairs;
        ActivityOptions options;


        public ListItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            dragImage = itemView.findViewById(R.id.drag);
            timerText = itemView.findViewById(R.id.timer_textViewList);
            button1 = itemView.findViewById(R.id.edit_timer);
            button2 = itemView.findViewById(R.id.delete_timer);
            button1.setOnClickListener(this);
            button2.setOnClickListener(this);
            timerText.setOnClickListener(this);
            dragImage.setOnTouchListener(this);
        }

        public void init() {
            timerText.setText(DataHolder.getInstance().getListTimerGroup().get(getAdapterPosition()).toString());
            timerGroupType = DataHolder.getInstance().getListTimerGroup().get(getAdapterPosition()).getTimerGroupType();
            setDragImageVisibility();
        }

        public void setDragImageVisibility() {
            if (DataHolder.getInstance().getStackNavigation().empty())
                dragImage.setVisibility(View.GONE);
            else dragImage.setVisibility(View.VISIBLE);
        }

        private void button1() {
            if (DataHolder.getInstance().getStackNavigation().empty()) {
                timerNamePopup(getAdapterPosition(), itemView);
            } else {
                timerPickerPopup(getAdapterPosition(), itemView);
            }
        }

        private void button2() {
            if (!DataHolder.getInstance().getStackNavigation().empty() && DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()) && DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek()) >= 0 && DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek()) < DataHolder.getInstance().getAllTimerGroups().size() && getAdapterPosition() >= 0 && getAdapterPosition() < DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup().size()) {
                if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getListTimerGroup().get(getAdapterPosition()).toString()))
                    DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getListTimerGroup().get(getAdapterPosition()).getName())).decrementInternalUsageCount();
                DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup().remove(getAdapterPosition());
            } else if (DataHolder.getInstance().getStackNavigation().empty()) {
                if (DataHolder.getInstance().getAllTimerGroups().get(getAdapterPosition()).getInternalUsageCount() <= 0) {
                    for (TimerGroup tg : DataHolder.getInstance().getAllTimerGroups().get(getAdapterPosition()).getListTimerGroup()) {
                        if (DataHolder.getInstance().getMapTimerGroups().containsKey(tg.getName())) {
                            DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(tg.getName())).decrementInternalUsageCount();
                        }
                    }
                    DataHolder.getInstance().getAllTimerGroups().remove(getAdapterPosition());
                } else
                    Toast.makeText(itemView.getContext(), ConstantsClass.COUNTER_IN_USE_ELSEWHERE, Toast.LENGTH_SHORT).show();
            }
            notifyDataSetChanged();
            DataHolder.getInstance().saveData(itemView.getContext());
            DataHolder.getInstance().setDisableButtonClick(false);
        }

        private void textViewPress() {
            Intent intent;
            intent = new Intent(itemView.getContext(), TimerActivity.class);
            DataHolder.getInstance().getStackNavigation().push(String.valueOf(timerText.getText()));
            if (DataHolder.getInstance().getStackNavigation().size() <= 1) {
                animationPairs = new Pair[2];
                animationPairs[0] = new Pair<>(activity.findViewById(R.id.home_add_button), "timer_name_popup_transition");
                animationPairs[1] = new Pair<>(itemView.findViewById(R.id.timer_textViewList), "timer_name_transition");
                options = ActivityOptions.makeSceneTransitionAnimation(activity, animationPairs);
                itemView.getContext().startActivity(intent, options.toBundle());
            } else {
                if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()))
                    DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup());
                else DataHolder.getInstance().setListTimerGroup(new ArrayList<>());
                notifyDataSetChanged();
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == dragImage.getId() && !DataHolder.getInstance().getStackNavigation().empty()) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startDragListener.requestDrag(this);
                }
            }
            return true;
        }


        @Override
        public void onClick(View view) {
            if (!TimerActivity.timerRunning && !CountdownTimer.timerPaused && !DataHolder.getInstance().getDisableButtonClick()) {
                DataHolder.getInstance().setDisableButtonClick(true);
                if (view.getId() == button2.getId()) button2();
                else if (view.getId() == button1.getId()) button1();
                else if (DataHolder.getInstance().getListTimerGroup().get(getAdapterPosition()).getTimerGroupType().equals(TimerGroupType.TIMER_GROUP) && view.getId() == timerText.getId())
                    textViewPress();
                else DataHolder.getInstance().setDisableButtonClick(false);
            } else
                Toast.makeText(itemView.getContext(), ConstantsClass.RUNNING_COUNTDOWN_UNEDITABLE_TOAST, Toast.LENGTH_SHORT).show();
        }
    }
}
