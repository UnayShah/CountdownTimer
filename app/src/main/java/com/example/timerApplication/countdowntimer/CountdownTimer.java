package com.example.timerApplication.countdowntimer;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.TextView;

import com.example.timerApplication.TimerActivity;
import com.example.timerApplication.timers.Timer;
import com.example.timerApplication.common.ConstantsClass;

public class CountdownTimer {
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private Long timeInMillis;
    private TimerActivity timerActivity;
    public static Boolean timerPaused;
    private Integer indexOfTimer;

    public CountdownTimer(TextView timerTextView, TimerActivity timerActivity) {
        this.timerTextView = timerTextView;
        this.timerActivity = timerActivity;
        this.timerPaused = false;
        indexOfTimer = 0;
    }

    public void startTimer(Integer indexOfTimer, Long pauseTimeInMillis) {
        this.indexOfTimer = indexOfTimer;
        if (!timerPaused)
            timeInMillis = TimerActivity.listTimers.getListTimers().get(indexOfTimer).getTimeInMilliseconds();
        else
            timeInMillis = pauseTimeInMillis;
        timerPaused = false;
        countDownTimer = new CountDownTimer(timeInMillis, ConstantsClass.ONE_SECOND_IN_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeInMillis = millisUntilFinished;
                timerTextView.setText(new Timer(millisUntilFinished).toString());
            }

            @Override
            public void onFinish() {
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,ConstantsClass.SOUND_MEDIUM);
                Vibrator vibrator = (Vibrator) timerActivity.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(ConstantsClass.VIBRATE_MEDIUM);
                if (indexOfTimer + 1 == TimerActivity.listTimers.size()) {
                    timerActivity.stopTimer();
                    return;
                } else {
                    startTimer(indexOfTimer + ConstantsClass.ONE, ConstantsClass.ZERO.longValue());
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
