package com.example.timerApplication;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.TextView;

import com.example.timerApplication.Timers.Timer;

import java.util.List;

public class CountdownTimer {
    private TextView timerTextView;
    private List<Timer> listTimers;
    private CountDownTimer countDownTimer;
    private Long timeInMillis;
    private TimerActivityRecycler timerActivityRecycler;
    private Boolean timerPaused;
    private Integer indexOfTimer;

    public CountdownTimer(List<Timer> listTimers, TextView timerTextView, TimerActivityRecycler timerActivityRecycler) {
        this.listTimers = listTimers;
        this.timerTextView = timerTextView;
        this.timerActivityRecycler = timerActivityRecycler;
        this.timerPaused = false;
        indexOfTimer = 0;
    }

    public void setListTimers(List<Timer> listTimers) {
        this.listTimers = listTimers;
    }

    public void startTimer(Integer indexOfTimer, Long pauseTimeInMillis) {
        this.indexOfTimer = indexOfTimer;
        if (!timerPaused)
            timeInMillis = listTimers.get(indexOfTimer).getTimeInMilliseconds();
        else
            timeInMillis = pauseTimeInMillis;
        timerPaused = false;
        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeInMillis = millisUntilFinished;
                timerTextView.setText(new Timer(millisUntilFinished).toString());
            }

            @Override
            public void onFinish() {
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,100);
                Vibrator vibrator = (Vibrator) timerActivityRecycler.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(ConstantsClass.VIBRATE_MEDIUM);
                if (indexOfTimer + 1 == listTimers.size()) {
                    timerActivityRecycler.stopTimer();
                    return;
                } else {
                    startTimer(indexOfTimer + 1, 0l);
                }
            }
        }.start();
    }

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
        countDownTimer.cancel();
    }
}
