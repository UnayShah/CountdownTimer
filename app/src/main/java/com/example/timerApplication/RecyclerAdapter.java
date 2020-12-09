package com.example.timerApplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerApplication.common.ConstantsClass;
import com.example.timerApplication.countdowntimer.CountdownTimer;
import com.example.timerApplication.popupactivity.PopupActivityFactory;
import com.example.timerApplication.popupactivity.TimerNamePopupActivity;
import com.example.timerApplication.timers.TimerGroup;
import com.example.timerApplication.timers.TimerGroupType;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ListItemViewHolder> {
    LayoutInflater layoutInflater;
    //    IStartDragListener startDragListener;
    Boolean boolRemove;
    Boolean fromHome;
    View timerPopupView;

    public void setFromHome(Boolean fromHome) {
        this.fromHome = fromHome;
    }


    public RecyclerAdapter() {
        boolRemove = false;
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
        if (!boolRemove) {
            holder.itemView.setVisibility(View.GONE);
            holder.timerText.setText(HomeActivity.listTimerGroup.get(position).toString());
            holder.setTimerGroupType(HomeActivity.listTimerGroup.get(position).getTimerGroupType());
            holder.setOnHomeScreen(fromHome);
            editTimerGroup(holder, position, true);
        }
    }

    @Override
    public int getItemCount() {
        return HomeActivity.listTimerGroup.size();
    }

    /**
     * Show popup window and add timer or edit timer text value
     *
     * @param viewHolder
     * @param position
     * @param newTimerGroup
     */
    public void editTimerGroup(ListItemViewHolder viewHolder, int position, Boolean newTimerGroup) {
        if (HomeActivity.listTimerGroup.get(position).equals(TimerGroupType.TIMER)) {
            timerPopupView = layoutInflater.inflate(R.layout.timer_picker_popup, null, false);
            HomeActivity.listTimerGroup.set(position, new TimerGroup(PopupActivityFactory.getInstance(timerPopupView, viewHolder.itemView, viewHolder.timerText, true, position, this).editTimer()));
        } else {
            timerPopupView = layoutInflater.inflate(R.layout.timer_name_popup, null, false);
            HomeActivity.listTimerGroup.set(position, new TimerNamePopupActivity(timerPopupView, viewHolder, HomeActivity.listTimerGroup.get(position), newTimerGroup, position, this).editTimerGroup());
            viewHolder.setDragImageVisibility();
        }
    }


    /**
     * Remove current timer view
     *
     * @param position
     */
    public void deleteTimerGroup(int position) {
        boolRemove = true;
        this.notifyItemRemoved(position);
        HomeActivity.listTimerGroup.remove(HomeActivity.listTimerGroup.get(position));
        boolRemove = false;
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView dragImage;
        TextView timerText;
        ImageButton button1;
        ImageButton button2;
        Boolean onHomeScreen;
        TimerGroupType timerGroupType;

        public Boolean getOnHomeScreen() {
            return onHomeScreen;
        }

        public void setOnHomeScreen(Boolean onHomeScreen) {
            this.onHomeScreen = onHomeScreen;
        }

        public void setDragImageVisibility(){
            if(onHomeScreen)dragImage.setVisibility(View.GONE);
            else dragImage.setVisibility(View.VISIBLE);
        }

        public TimerGroupType getTimerGroupType() {
            return timerGroupType;
        }

        public void setTimerGroupType(TimerGroupType timerGroupType) {
            this.timerGroupType = timerGroupType;
        }

        public TextView getTimerText() {
            return timerText;
        }

        public void setTimerText(String timerText) {
            this.timerText.setText(timerText);
        }

        public ListItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            dragImage = itemView.findViewById(R.id.drag);
//            if(onHomeScreen)dragImage.setVisibility(View.GONE);
            timerText = itemView.findViewById(R.id.timer_textViewList);
            button1 = itemView.findViewById(R.id.edit_timer);
            button2 = itemView.findViewById(R.id.delete_timer);
            button1.setOnClickListener(this);
            button2.setOnClickListener(this);
            timerText.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (timerGroupType == TimerGroupType.TIMER && !TimerActivity.timerRunning && !CountdownTimer.timerPaused) {
                if (view.getId() == button1.getId())
                    editTimerGroup(this, getAdapterPosition(), false);
                else if (view.getId() == button2.getId())
                    deleteTimerGroup(getAdapterPosition());
            } else if (timerGroupType == TimerGroupType.TIMER_GROUP) {
                if (view.getId() == button1.getId())
                    editTimerGroup(this, getAdapterPosition(), false);
                else if (view.getId() == button2.getId())
                    deleteTimerGroup(getAdapterPosition());
                else if (view.getId() == timerText.getId()) {
                    //redirect
                }
            } else
                Toast.makeText(itemView.getContext(), ConstantsClass.RUNNING_COUNTDOWN_UNEDITABLE_TOAST, Toast.LENGTH_SHORT).show();
        }
    }
}
