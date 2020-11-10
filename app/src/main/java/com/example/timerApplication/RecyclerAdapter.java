package com.example.timerApplication;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerApplication.Timers.Timer;

import java.util.Collections;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {
    private static String TAG = "Recycler";
    List<Timer> listTimers;
    LayoutInflater layoutInflater;
    IStartDragListener startDragListener;

    public RecyclerAdapter(List<Timer> listTimers, IStartDragListener startDragListener) {
        this.listTimers = listTimers;
        this.startDragListener = startDragListener;
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
        listTimers.remove(position);
        this.notifyItemRemoved(position);
        TimerActivityRecycler.setListTimers(listTimers);
    }

    public void setListTimers(List<Timer> listTimers) {
        this.listTimers = listTimers;
        TimerActivityRecycler.setListTimers(listTimers);
    }

    /**
     * print list of timers
     */
    public void printListTimers() {
        System.out.println("START:");
        for (Timer t : listTimers) {
            System.out.println(t.toString());
        }
        System.out.println("END");
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
        listTimers.set(position, PopupActivityFactory.getInstance(popupView, view, timerText, newTimer, listTimers, position, this).editTimer(timerText.getText().toString()));
        TimerActivityRecycler.setListTimers(listTimers);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.itemView.setVisibility(View.GONE);
        holder.timerText.setText(new Timer().toString());
        editTimer(holder.itemView, position, holder.timerText, true);
        holder.dragImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startDragListener.requestDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        TimerActivityRecycler.setListTimers(listTimers);
        return listTimers.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        Collections.swap(listTimers, fromPosition, toPosition);
        this.notifyItemMoved(fromPosition, toPosition);
//        printListTimers();
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
            switch (view.getId()) {
                case R.id.edit_timer:
                    editTimer(view, getAdapterPosition(), timerText, false);
                    break;
                case R.id.delete_timer:
                    deleteTimer(getAdapterPosition());
                    break;
            }
        }
    }
}