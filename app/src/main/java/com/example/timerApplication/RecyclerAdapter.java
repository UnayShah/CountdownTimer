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

import com.example.timerApplication.countdowntimer.CountdownTimer;
import com.example.timerApplication.popupactivity.PopupActivityFactory;
import com.example.timerApplication.timers.Timer;
import com.example.timerApplication.common.ConstantsClass;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {
    LayoutInflater layoutInflater;
    IStartDragListener startDragListener;
    Boolean boolRemove;

    public RecyclerAdapter(IStartDragListener startDragListener) {
        this.startDragListener = startDragListener;
        boolRemove = false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        final View view = layoutInflater.inflate(R.layout.add_timer, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
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

    /**
     * Show popup window and add timer or edit timer text value
     *
     * @param view
     * @param position
     * @param timerText
     * @param newTimer
     */
    public void editTimer(View view, int position, TextView timerText, Boolean newTimer) {
        View popupView = layoutInflater.inflate(R.layout.timer_picker, null, false);
        TimerActivity.listTimers.set(position, PopupActivityFactory.getInstance(popupView, view, timerText, newTimer, position, this).editTimer(timerText.getText().toString()));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (!boolRemove) {
            holder.itemView.setVisibility(View.GONE);
            holder.timerText.setText(new Timer().toString());
            editTimer(holder.itemView, position, holder.timerText, true);
            holder.dragImage.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startDragListener.requestDrag(holder);
                }
                return false;
            });
        }
        boolRemove = false;
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
            timerText = itemView.findViewById(R.id.timer_textView);
            editButton = itemView.findViewById(R.id.edit_timer);
            deleteButton = itemView.findViewById(R.id.delete_timer);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (!TimerActivity.timerRunning && !CountdownTimer.timerPaused)
                switch (view.getId()) {
                    case R.id.edit_timer:
                        editTimer(view, getAdapterPosition(), timerText, false);
                        break;
                    case R.id.delete_timer:
                        deleteTimer(getAdapterPosition());
                        break;
                }
            else
                Toast.makeText(itemView.getContext(), ConstantsClass.RUNNING_COUNTDOWN_UNEDITABLE_TOAST, Toast.LENGTH_SHORT).show();
        }
    }
}