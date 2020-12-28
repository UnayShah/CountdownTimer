package com.example.timerApplication.countdowntimer;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.TextView;

import com.example.timerApplication.TimerActivity;
import com.example.timerApplication.common.ConstantsClass;
import com.example.timerApplication.model.DataHolder;
import com.example.timerApplication.timers.Timer;

public class CountdownTimer {
    public static Boolean timerPaused = true;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private Long timeInMillis;
    private TimerActivity timerActivity;
    private Integer indexOfTimer;

    public CountdownTimer(TextView timerTextView, TimerActivity timerActivity) {
        this.timerTextView = timerTextView;
        this.timerActivity = timerActivity;
        this.timerPaused = false;
        indexOfTimer = 0;
        DataHolder.getInstance().setQueueTimers(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getTimersQueue());
        while (!DataHolder.getInstance().getQueueTimers().isEmpty()) {
            System.out.println(DataHolder.getInstance().getQueueTimers().peek().toString());
            DataHolder.getInstance().getQueueTimers().remove();
        }
        DataHolder.getInstance().setQueueTimers(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getTimersQueue());
    }

    public void startTimer(Long pauseTimeInMillis) {
        if (!timerPaused) {
            timeInMillis = DataHolder.getInstance().getQueueTimers().peek().getTimeInMilliseconds();
        } else timeInMillis = pauseTimeInMillis;
        timerPaused = false;
        countDownTimer = new CountDownTimer(timeInMillis, ConstantsClass.ONE_MILLIS_IN_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeInMillis = millisUntilFinished;
                timerTextView.setText(new Timer(millisUntilFinished).toString());
            }

            @Override
            public void onFinish() {
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, ConstantsClass.SOUND_MEDIUM);
                Vibrator vibrator = (Vibrator) timerActivity.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(ConstantsClass.VIBRATE_MEDIUM);
                DataHolder.getInstance().getQueueTimers().remove();
                if (DataHolder.getInstance().getQueueTimers().isEmpty()) {
                    DataHolder.getInstance().setQueueTimers(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getTimersQueue());
                    if (DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getLooped()) {
                        startTimer(ConstantsClass.ZERO.longValue());
                    } else {
                        timerActivity.stopTimer();
                        return;
                    }
                } else {
                    startTimer(ConstantsClass.ZERO.longValue());
                }
            }
        }.start();
    }

//    public void startTimer(Integer indexOfTimer, Long pauseTimeInMillis) {
//        this.indexOfTimer = indexOfTimer;
//        if (!timerPaused) {
//            timeInMillis = DataHolder.getInstance().getListTimerGroup().get(indexOfTimer).getTimer().getTimeInMilliseconds();
////            timeInMillis = TimerActivity.listTimers.getListTimers().get(indexOfTimer).getTimeInMilliseconds();
//        } else
//            timeInMillis = pauseTimeInMillis;
//        timerPaused = false;
//        countDownTimer = new CountDownTimer(timeInMillis, ConstantsClass.ONE_MILLIS_IN_MILLIS) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                timeInMillis = millisUntilFinished;
//                timerTextView.setText(new Timer(millisUntilFinished).toString());
//            }
//
//            @Override
//            public void onFinish() {
//                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
//                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, ConstantsClass.SOUND_MEDIUM);
//                Vibrator vibrator = (Vibrator) timerActivity.getContext().getSystemService(Context.VIBRATOR_SERVICE);
//                vibrator.vibrate(ConstantsClass.VIBRATE_MEDIUM);
//                if (indexOfTimer + 1 == DataHolder.getInstance().getListTimerGroup().size()) {
//                    if (TimerActivity.looped) {
//                        startTimer(ConstantsClass.ZERO, ConstantsClass.ZERO.longValue());
//                    } else {
//                        timerActivity.stopTimer();
//                        return;
//                    }
//                } else {
//                    startTimer(indexOfTimer + ConstantsClass.ONE, ConstantsClass.ZERO.longValue());
//                }
//            }
//        }.start();
//    }

    public Integer getIndexOfTimer() {
        return indexOfTimer;
    }

    public Long pauseTimer() {
        timerPaused = true;
        countDownTimer.cancel();
        return timeInMillis;
    }

    public void stopTimer() {
        timerPaused = false;
        timerTextView.setText(new Timer().toString());
        try {
            countDownTimer.cancel();
        } catch (Exception ignored) {
            ;
        }
    }
}
