package com.example.timerApplication;

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
import com.example.timerApplication.popupactivity.PopupActivityFactory;
import com.example.timerApplication.timers.Timer;

public class RecyclerAdapterTimer extends RecyclerView.Adapter<RecyclerAdapterTimer.ViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {
    LayoutInflater layoutInflater;
    IStartDragListener startDragListener;
    Boolean boolRemove;

    public RecyclerAdapterTimer(IStartDragListener startDragListener) {
        this.startDragListener = startDragListener;
        boolRemove = false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        final View view = layoutInflater.inflate(R.layout.add_timer, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Remove current timer view
     *
     * @param position
     */
    public void deleteTimer(int position) {
        boolRemove = true;
        this.notifyItemRemoved(position);
        TimerActivity.listTimers.remove(TimerActivity.listTimers.getListTimers().get(position));
        boolRemove = false;
    }

    //    /**
//     * Show popup window and add timer or edit timer text value
//     *
//     * @param view
//     * @param position
//     * @param timerText
//     * @param newTimer
//     */
//    public void editTimer(View view, int position, TextView timerText, Boolean newTimer) {
//        View popupView = layoutInflater.inflate(R.layout.timer_picker_popup, null, false);
//        TimerActivity.listTimers.set(position, PopupActivityFactory.getInstance(popupView, view, timerText, newTimer, position, this).editTimer(timerText.getText().toString()));
//    }
    public void editTimer(ViewHolder viewHolder, int position, Boolean newTimer) {
        View popupView = layoutInflater.inflate(R.layout.timer_picker_popup, null, false);
        TimerActivity.listTimers.set(position, PopupActivityFactory.getInstance(popupView, viewHolder.itemView, viewHolder.timerText, newTimer, position, this).editTimer());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (!boolRemove) {
            holder.itemView.setVisibility(View.GONE);
            holder.timerText.setText(new Timer().toString());
            editTimer(holder, position, true);
            holder.dragImage.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    startDragListener.requestDrag(holder);
                }
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return TimerActivity.listTimers.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        TimerActivity.listTimers.swap(TimerActivity.listTimers, fromPosition, toPosition);
        this.notifyItemMoved(fromPosition, toPosition);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView dragImage;
        TextView timerText;
        ImageButton editButton;
        ImageButton deleteButton;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            dragImage = itemView.findViewById(R.id.drag);
            timerText = itemView.findViewById(R.id.timer_textViewList);
            editButton = itemView.findViewById(R.id.edit_timer);
            deleteButton = itemView.findViewById(R.id.delete_timer);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
            dragImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (!TimerActivity.timerRunning && !CountdownTimer.timerPaused) {
                if (view.getId() == editButton.getId())
                    editTimer(this, getAdapterPosition(), false);
                else if (view.getId() == deleteButton.getId())
                    deleteTimer(getAdapterPosition());
                else ;
            } else
                Toast.makeText(itemView.getContext(), ConstantsClass.RUNNING_COUNTDOWN_UNEDITABLE_TOAST, Toast.LENGTH_SHORT).show();
        }
    }
}