package com.example.timerApplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.example.timerApplication.model.DataHolder;
import com.example.timerApplication.popupactivity.PopupActivityFactory;
import com.example.timerApplication.popupactivity.TimerNamePopupActivity;
import com.example.timerApplication.timers.TimerGroup;
import com.example.timerApplication.timers.TimerGroupType;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ListItemViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {
    LayoutInflater layoutInflater;
    IStartDragListener startDragListener;
    Boolean boolRemove;
    Boolean fromHome;
    Boolean fromStorage;
    Boolean newItem;
    View timerPopupView;

    public RecyclerAdapter() {
        boolRemove = false;
        newItem = false;
    }

    public RecyclerAdapter(IStartDragListener iStartDragListener) {
        this.startDragListener = iStartDragListener;
        boolRemove = false;
    }

    public Boolean getFromHome() {
        return fromHome;
    }

    public void setFromHome(Boolean fromHome) {
        this.fromHome = fromHome;
    }

    public void setNewItem(Boolean newItem) {
        this.newItem = newItem;
    }

    public void setFromStorage(Boolean fromStorage) {
        this.fromStorage = fromStorage;
    }

    private void navigate(String timerText) {
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
        if (fromStorage) {
            holder.timerText.setText(DataHolder.getInstance().getListTimerGroup().get(position).toString());
            holder.setTimerGroupType(DataHolder.getInstance().getListTimerGroup().get(position).getTimerGroupType());
            holder.setOnHomeScreen(fromHome);
            holder.setDragImageVisibility();
        } else if (!boolRemove && newItem) {
            holder.itemView.setVisibility(View.GONE);
            holder.timerText.setText(DataHolder.getInstance().getListTimerGroup().get(position).toString());
            holder.setTimerGroupType(DataHolder.getInstance().getListTimerGroup().get(position).getTimerGroupType());
            holder.setOnHomeScreen(fromHome);
            editTimerGroup(holder, position, true);
        }
    }

    @Override
    public int getItemCount() {
        return DataHolder.getInstance().getListTimerGroup().size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        Collections.swap(DataHolder.getInstance().getListTimerGroup(), fromPosition, toPosition);
        DataHolder.getInstance().setQueueTimers(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getTimersQueue());
        this.notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * Show popup window and add timer or edit timer text value
     *
     * @param viewHolder
     * @param position
     * @param newTimerGroup
     */
    public void editTimerGroup(ListItemViewHolder viewHolder, int position, Boolean newTimerGroup) {
        if (DataHolder.getInstance().getListTimerGroup().get(position).getTimerGroupType().equals(TimerGroupType.TIMER)) {
            timerPopupView = layoutInflater.inflate(R.layout.timer_picker_popup, null, false);
            DataHolder.getInstance().getListTimerGroup().set(position, new TimerGroup(PopupActivityFactory.getInstance(timerPopupView, viewHolder, viewHolder.timerText, newTimerGroup, position, this).editTimer()));
        } else if (DataHolder.getInstance().getListTimerGroup().get(position).getTimerGroupType().equals(TimerGroupType.TIMER_GROUP)) {
            timerPopupView = layoutInflater.inflate(R.layout.timer_name_popup, null, false);
            DataHolder.getInstance().getListTimerGroup().set(position, new TimerNamePopupActivity(timerPopupView, viewHolder, DataHolder.getInstance().getListTimerGroup().get(position), newTimerGroup, position, this).editTimerGroup());
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
        if (fromHome && !newItem) {
            if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getListTimerGroup().get(position).getName())) {
            }
            DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups());
        } else {
            DataHolder.getInstance().getListTimerGroup().remove(position);
        }
        this.notifyItemRemoved(position);
//        DataHolder.getInstance().getListTimerGroup().remove(DataHolder.getInstance().getListTimerGroup().get(position));
        boolRemove = false;
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        ImageView dragImage;
        TextView timerText;
        ImageButton button1;
        ImageButton button2;
        Boolean onHomeScreen;
        TimerGroupType timerGroupType;

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

        public Boolean getOnHomeScreen() {
            return onHomeScreen;
        }

        public void setOnHomeScreen(Boolean onHomeScreen) {
            this.onHomeScreen = onHomeScreen;
        }

        public TimerGroupType getTimerGroupType() {
            return timerGroupType;
        }

        public void setTimerGroupType(TimerGroupType timerGroupType) {
            this.timerGroupType = timerGroupType;
        }

        public void setDragImageVisibility() {
            if (onHomeScreen) dragImage.setVisibility(View.GONE);
            else dragImage.setVisibility(View.VISIBLE);
        }

        public TextView getTimerText() {
            return timerText;
        }

        public void setTimerText(String timerText) {
            String processedString = "";
            for (String s : timerText.split(" "))
                processedString += s + " ";
            processedString = processedString.substring(0, processedString.length() - 1);
            this.timerText.setText(processedString);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == dragImage.getId() && !onHomeScreen) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startDragListener.requestDrag(this);
                }
            }
            return true;
        }


        @Override
        public void onClick(View view) {
            final Intent intent;
            if (timerGroupType == TimerGroupType.TIMER && !TimerActivity.timerRunning && !CountdownTimer.timerPaused && !DataHolder.getInstance().getDisableButtonClick()) {
                DataHolder.getInstance().setDisableButtonClick(true);
                if (view.getId() == button1.getId())
                    editTimerGroup(this, getAdapterPosition(), false);
                else if (view.getId() == button2.getId())
                    deleteTimerGroup(getAdapterPosition());
                else DataHolder.getInstance().setDisableButtonClick(false);
            } else if (timerGroupType == TimerGroupType.TIMER_GROUP) {
                if (view.getId() == button1.getId()) {
                    if (onHomeScreen) {
                        DataHolder.getInstance().getMapTimerGroups().remove(timerText.getText());
                    }
                    editTimerGroup(this, getAdapterPosition(), false);
                } else if (view.getId() == button2.getId()) {
                    if (onHomeScreen) {
                        if (DataHolder.getInstance().getAllTimerGroups().get(getAdapterPosition()).getInternalUsageCount() <= 0) {
                            for (TimerGroup tg : DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(timerText.getText())).getListTimerGroup()) {
                                if (tg.getTimerGroupType().equals(TimerGroupType.TIMER_GROUP)) {
                                    DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(tg.getName())).decrementInternalUsageCount();
                                }
                            }
                            deleteTimerGroup(getAdapterPosition());
                        } else
                            Toast.makeText(itemView.getContext(), ConstantsClass.COUNTER_IN_USE_ELSEWHERE, Toast.LENGTH_SHORT).show();

                    } else {
                        DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup().get(getAdapterPosition()).getName())).decrementInternalUsageCount();
                        deleteTimerGroup(getAdapterPosition());
                    }
                    DataHolder.getInstance().updateMap();
                } else if (view.getId() == timerText.getId()) {
                    DataHolder.getInstance().getStackNavigation().push(String.valueOf(timerText.getText()));
                    if (onHomeScreen) {
                        fromStorage = true;
                        intent = new Intent(itemView.getContext(), TimerActivity.class);
                        itemView.getContext().startActivity(intent);
                    } else {
                        fromStorage = true;
                        fromHome = false;
                        if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()))
                            DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup());
                        else DataHolder.getInstance().setListTimerGroup(new ArrayList<>());
                        notifyDataSetChanged();
                    }
                }
            } else
                Toast.makeText(itemView.getContext(), ConstantsClass.RUNNING_COUNTDOWN_UNEDITABLE_TOAST, Toast.LENGTH_SHORT).show();
        }
    }
}
