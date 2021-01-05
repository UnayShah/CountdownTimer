package com.example.countdownTimer.countdowntimer;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.TextView;

import com.example.countdownTimer.TimerActivity;
import com.example.countdownTimer.common.ConstantsClass;
import com.example.countdownTimer.model.DataHolder;
import com.example.countdownTimer.timers.TimerGroup;
import com.example.countdownTimer.timers.TimerGroupType;

import java.util.Stack;

public class CountdownTimer {
    private static final TimerGroup timerGroup = new TimerGroup();
    public static Boolean timerPaused = false;
    private final TextView timerTextView;
    private final TimerActivity timerActivity;
    private final Stack<TimerGroup> countdownStack;
    public Long timePassed;
    public Long totalTime;
    private Integer indexOfTimer;
    private CountDownTimer countDownTimer;
    private Long timeInMillis;

    public CountdownTimer(TextView timerTextView, TimerActivity timerActivity) {
        this.timerTextView = timerTextView;
        this.timerActivity = timerActivity;
        timePassed = 0L;
        totalTime = 0L;
        timerPaused = false;
        indexOfTimer = 0;
        countdownStack = new Stack<>();
    }

    public void startTimer(Long pauseTimeInMillis) {
        if (countdownStack.isEmpty() && !DataHolder.getInstance().getStackNavigation().isEmpty() && DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()) && DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup().size() > indexOfTimer) {
            if (!DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup().get(indexOfTimer).toString()) || DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup().get(indexOfTimer).toString())).getListTimerGroup().size() > 0) {
                countdownStack.push(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup().get(indexOfTimer));
            } else {
                indexOfTimer++;
                startTimer(pauseTimeInMillis);
            }
        } else if (DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup().size() <= indexOfTimer) {
            timerActivity.stopTimer();
        }
        if (!countdownStack.isEmpty() && countdownStack.peek() != null && countdownStack.peek().getTimerGroupType().equals(TimerGroupType.TIMER)) {
            if (!timerPaused) {
                timeInMillis = countdownStack.peek().getTimeInMilliseconds();
            } else timeInMillis = pauseTimeInMillis;
            timerPaused = false;
            countDownTimer = new CountDownTimer(timeInMillis, ConstantsClass.ONE_MILLIS_IN_MILLIS) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeInMillis = millisUntilFinished;
                    timerTextView.setText(timerGroup.setTimer(millisUntilFinished).toString());
                }

                @Override
                public void onFinish() {
                    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, ConstantsClass.SOUND_MEDIUM);
                    Vibrator vibrator = (Vibrator) timerActivity.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(ConstantsClass.VIBRATE_MEDIUM);
                    if (!countdownStack.isEmpty())
                        countdownStack.pop();
                    while (!countdownStack.isEmpty() && countdownStack.peek() == null) {
                        countdownStack.pop();
                        if (!countdownStack.isEmpty())
                            countdownStack.pop();
                    }
                    if (countdownStack.isEmpty())
                        indexOfTimer++;
                    if (DataHolder.getInstance().getListTimerGroup().size() <= indexOfTimer) {
                        if (DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getLooped()) {
                            indexOfTimer = 0;
                            startTimer(ConstantsClass.ZERO.longValue());
                        } else {
                            timerActivity.stopTimer();
                        }
                    } else {
                        startTimer(ConstantsClass.ZERO.longValue());
                    }
                }
            }.start();
        } else if (!countdownStack.isEmpty() && countdownStack.peek() != null && countdownStack.peek().getTimerGroupType().equals(TimerGroupType.TIMER_GROUP)) {
            if (countdownStack.peek().getListTimerGroup().size() == 0) countdownStack.pop();
            else {
                timerGroup.setListTimerGroup(countdownStack.peek().getListTimerGroup());
                countdownStack.push(null);
                for (int i = timerGroup.getListTimerGroup().size() - 1; i >= 0; i--) {
                    countdownStack.push(timerGroup.getListTimerGroup().get(i));
                }
            }
            startTimer(timeInMillis);
        } else if (!countdownStack.isEmpty() && countdownStack.peek() == null) {
            countdownStack.pop();
            if (!countdownStack.isEmpty()) countdownStack.pop();
            startTimer(timeInMillis);
        } else {
            stopTimer();
        }

//        if (countdownStack.peek().getTimerGroupType().equals(TimerGroupType.TIMER)) {
//            timeInMillis = countdownStack.peek().getTimeInMilliseconds();
//        } else {

//        }


    }

    public Integer getIndexOfTimer() {
        return indexOfTimer;
    }

    public void setIndexOfTimer(Integer indexOfTimer) {
        this.indexOfTimer = indexOfTimer;
    }

    public Long pauseTimer() {
        timerPaused = true;
        countDownTimer.cancel();
        return timeInMillis;
    }

    public void stopTimer() {
        countdownStack.clear();
        timerPaused = false;
        indexOfTimer = 0;
        timerTextView.setText(timerGroup.setTimer(0L).toString());
        try {
            countDownTimer.cancel();
        } catch (Exception ignored) {
        }
    }
}
