package com.example.countdownTimer.countdowntimer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.ImageView;

import com.example.countdownTimer.R;
import com.example.countdownTimer.TimerActivity;
import com.example.countdownTimer.common.ConstantsClass;
import com.example.countdownTimer.model.DataHolder;
import com.example.countdownTimer.timers.TimerGroup;
import com.example.countdownTimer.timers.TimerGroupType;
import com.google.android.material.textview.MaterialTextView;

import java.util.Stack;

public class CountdownTimer {
    private static final TimerGroup timerGroup = new TimerGroup();
    public static Boolean timerPaused = false;
    public static Long timePassed;
    public static Long totalTime;
    private final TimerActivity timerActivity;
    private final Stack<TimerGroup> countdownStack;
    private final ImageView timerAnimation;
    Canvas progressBar;
    Paint paint;
    String iterationText;
    private Integer reps;
    private MaterialTextView indexOfTimerTextView;
    private Integer indexOfTimer;
    private CountDownTimer countDownTimer;
    private Long timeInMillis;

    public CountdownTimer(TimerActivity timerActivity) {
        this.timerActivity = timerActivity;
        timePassed = 0L;
        totalTime = 0L;
        timerPaused = false;
        indexOfTimer = 0;
        reps = ConstantsClass.ONE;
        countdownStack = new Stack<>();
        progressBar = new Canvas();
        paint = new Paint();
        iterationText = "";
        timerAnimation = timerActivity.findViewById(R.id.timer_animation);
        timerAnimation.setRotation(-90f);
//        progressBar.drawColor(timerTextView.getResources().getColor(R.color.blueGray600));
        indexOfTimerTextView = timerActivity.findViewById(R.id.index_textView);
    }

    public void setTotalTime() {
        totalTime = ConstantsClass.ZERO_LONG;
        if (countdownStack.size() == 1) {
            for (TimerGroup timerGroup : countdownStack) {
                if (timerGroup == null) continue;
                if (DataHolder.getInstance().getMapTimerGroups().containsKey(timerGroup.getName()))
                    totalTime += DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(timerGroup.getName())).totalTimerGroupDuration();
                else totalTime += timerGroup.getTimeInMilliseconds();
            }
        }
    }

    public void startTimer(Long pauseTimeInMillis) {
        indexOfTimerTextView = timerActivity.findViewById(R.id.index_textView);
        if (indexOfTimerTextView != null && !DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getLooped() && DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getReps() > ConstantsClass.ONE) {
            iterationText = "Loop " + reps;
            indexOfTimerTextView.setText(iterationText);
        }
        if (countdownStack.isEmpty() && !DataHolder.getInstance().getStackNavigation().isEmpty() && DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()) && DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup().size() > indexOfTimer) {
            if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup().get(indexOfTimer).toString()))
                countdownStack.push(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup().get(indexOfTimer).toString())));
            else
                countdownStack.push(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup().get(indexOfTimer));
            setTotalTime();
        }
        if (!countdownStack.isEmpty() && countdownStack.peek() == null) {
            countdownStack.pop();
            countdownStack.pop();
            if (countdownStack.isEmpty()) indexOfTimer++;
            if (countDownTimer != null) countDownTimer.cancel();
            startTimer(pauseTimeInMillis);
        } else if (!countdownStack.isEmpty() && countdownStack.peek() != null && countdownStack.peek().getTimerGroupType().equals(TimerGroupType.TIMER)) {
            if (!timerPaused) {
                timeInMillis = countdownStack.peek().getTimeInMilliseconds();
            } else timeInMillis = pauseTimeInMillis;
            timerPaused = false;
            countDownTimer = new CountDownTimer(timeInMillis, ConstantsClass.TWO_HUNDRED_FIFTY_MILLIS_IN_MILLIS) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeInMillis = millisUntilFinished;
                    timerActivity.setText(timerGroup.setTimer(millisUntilFinished).toString());
                    timePassed += ConstantsClass.TWO_HUNDRED_FIFTY_MILLIS_IN_MILLIS;
                    timerAnimation.setRotation(-(((float) timePassed * 360) / totalTime) - 90);
//                    float width = ((float) timePassed) / ((float) totalTime);
//                    width *= timerActivity.getRecyclerView().findViewHolderForAdapterPosition(indexOfTimer).itemView.getWidth();
//                    paint.setColor(timerTextView.getResources().getColor(R.color.blueGray700));
//                    progressBar.drawRect(0, 0, width, timerActivity.getRecyclerView().findViewHolderForAdapterPosition(indexOfTimer).itemView.getHeight(), paint);
//                    paint.setColor(timerTextView.getResources().getColor(R.color.background));
//                    progressBar.drawRect(width, 0, timerActivity.getRecyclerView().findViewHolderForAdapterPosition(indexOfTimer).itemView.getWidth(), timerActivity.getRecyclerView().findViewHolderForAdapterPosition(indexOfTimer).itemView.getHeight(), paint);
//                    Drawable drawable = new DrawableContainer();
//                    drawable.draw(progressBar);
                    try {
                        if (timePassed >= totalTime) timePassed = ConstantsClass.ZERO_LONG;
                        if (indexOfTimer < DataHolder.getInstance().getListTimerGroup().size())
                            timerActivity.getRecyclerView().findViewHolderForAdapterPosition(indexOfTimer).itemView.setBackgroundResource(R.drawable.add_timer_active);
                    } catch (Exception ignore) {
                    }
                }

                @Override
                public void onFinish() {
                    try {
                        timerActivity.getRecyclerView().findViewHolderForAdapterPosition(indexOfTimer).itemView.setBackgroundResource(R.drawable.add_timer);
                    } catch (Exception ignore) {
                    }
                    timerAnimation.setRotation(-(((float) timePassed * 360) / totalTime) - 90);
                    if (timePassed >= totalTime) timePassed = ConstantsClass.ZERO_LONG;
                    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, ConstantsClass.SOUND_MEDIUM);
                    Vibrator vibrator = (Vibrator) timerActivity.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(ConstantsClass.VIBRATE_MEDIUM);
                    if (!countdownStack.isEmpty())
                        countdownStack.pop();
                    if (countdownStack.isEmpty()) {
                        indexOfTimer++;
                    }
                    if (DataHolder.getInstance().getListTimerGroup().size() <= indexOfTimer) {
                        if (DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getLooped() || reps < DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getReps()) {
                            stopTimer();
                            if (!DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getLooped())
                                reps++;
                            startTimer(ConstantsClass.ZERO.longValue());
                        } else {
                            timerActivity.stopTimer();
                        }
                    } else {
                        cancel();
                        startTimer(ConstantsClass.ZERO.longValue());
                    }
                }
            }.start();
        } else if (!countdownStack.isEmpty() && countdownStack.peek() != null && countdownStack.peek().getTimerGroupType().equals(TimerGroupType.TIMER_GROUP)) {
            String name = countdownStack.peek().getName();
            timerGroup.setListTimerGroup(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(name)).getListTimerGroup());
            for (TimerGroup tg1 : countdownStack) {
                if (tg1 != null && tg1.getTimerGroupType().equals(TimerGroupType.TIMER_GROUP)) {
                    try {
                        for (TimerGroup tg2 : timerGroup.getListTimerGroup()) {
                            if (tg2.getTimerGroupType().equals(TimerGroupType.TIMER_GROUP) && (tg1.getName().equals(tg2.getName()) || name.equals(tg2.getName()))) {
                                timerGroup.getListTimerGroup().remove(tg2);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            for (int i = 0; i < timerGroup.getListTimerGroup().size(); i++) {
                if (timerGroup.getListTimerGroup().get(i).getTimerGroupType().equals(TimerGroupType.TIMER_GROUP) && name.equals(timerGroup.getListTimerGroup().get(i).getName())) {
                    timerGroup.getListTimerGroup().remove(i);
                    i--;
                }
            }
            countdownStack.push(null);
            for (int i = timerGroup.getListTimerGroup().size() - 1; i >= 0; i--) {
                countdownStack.push(timerGroup.getListTimerGroup().get(i));
            }
            if (countdownStack.isEmpty()) {
                indexOfTimer++;
            }
            if (countDownTimer != null) countDownTimer.cancel();
            startTimer(ConstantsClass.ZERO_LONG);
        } else if (countdownStack.isEmpty() && indexOfTimer > DataHolder.getInstance().getListTimerGroup().size()) {
            if (DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getLooped() || reps < DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getReps()) {
                stopTimer();
                if (!DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getLooped())
                    reps++;
                startTimer(ConstantsClass.ZERO_LONG);
            } else timerActivity.stopTimer();
        } else {
            indexOfTimer++;
            if (countDownTimer != null) countDownTimer.cancel();
            startTimer(ConstantsClass.ZERO_LONG);
        }
    }

    public Integer getIndexOfTimer() {
        return indexOfTimer;
    }

    public Long pauseTimer() {
        timerPaused = true;
        if (countDownTimer != null)
            countDownTimer.cancel();
        return timeInMillis;
    }

    public void repsSetOne() {
        reps = ConstantsClass.ONE;
    }

    public void stopTimer() {
        try {
            if (indexOfTimer < DataHolder.getInstance().getListTimerGroup().size())
                timerActivity.getRecyclerView().findViewHolderForAdapterPosition(indexOfTimer).itemView.setBackgroundResource(R.drawable.add_timer);
            else if (DataHolder.getInstance().getListTimerGroup().size() - 1 >= 0)
                timerActivity.getRecyclerView().findViewHolderForAdapterPosition(DataHolder.getInstance().getListTimerGroup().size() - 1).itemView.setBackgroundResource(R.drawable.add_timer);
        } catch (Exception ignore) {
        }
        countdownStack.clear();
        totalTime = ConstantsClass.ZERO_LONG;
        timePassed = ConstantsClass.ZERO_LONG;
        timerPaused = false;
        indexOfTimer = 0;
//        timerActivity.setText(timerGroup.setTimer(0L).toString());
        timerAnimation.setRotation(-90);
        if (indexOfTimerTextView != null) indexOfTimerTextView.setText("");
        if (countDownTimer != null)
            countDownTimer.cancel();
    }
}