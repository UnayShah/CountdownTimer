package com.UnayShah.countdownTimer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.UnayShah.countdownTimer.common.ConstantsClass;
import com.UnayShah.countdownTimer.common.DataHolder;
import com.UnayShah.countdownTimer.countdowntimer.CountdownTimer;
import com.UnayShah.countdownTimer.countdowntimer.CountdownTimerFactory;
import com.UnayShah.countdownTimer.model.CustomAnimations;
import com.UnayShah.countdownTimer.popupactivity.TimePickerPopup;
import com.UnayShah.countdownTimer.timers.Timer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener, IStartDragListener {
    public static Boolean timerRunning = false;
    public static MediaPlayer mediaPlayer;
    static CountdownTimer countdownTimer;
    static RecyclerView recyclerView;
    AudioManager audioManager;
    ConstraintLayout emptyHolder;
    MaterialButton addTimerButton;
    MaterialButton volumeUpButton;
    MaterialButton volumeDownButton;
    MaterialButton startPauseTimerButton;
    MaterialButton stopTimerButton;
    MaterialButton homeButton;
    Slider volumeSlider;
    RecyclerAdapter recyclerAdapter;
    ItemTouchHelper itemTouchHelper;
    MaterialTextView timerTextView;
    Long pauseTimeInMillis;
    Float mediaVolume;
    Integer indexOfTimer;
    View timerLayout;
    AdView adView;
    MaterialToolbar timerToolbar;

    public static void autoScroll(int position) {
        recyclerView.smoothScrollToPosition(position);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_layout);
        init();
//        MobileAds.initialize(this);
    }

    @Override
    protected void onResume() {
        loadData();
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        adView = findViewById(R.id.adView_timer);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        DataHolder.getInstance().setOriginalSystemVolume(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), getApplicationContext());
        mediaVolume = (float) DataHolder.getInstance().getAppVolume(getApplicationContext());
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mediaVolume.intValue(), 0);
        volumeSlider = findViewById(R.id.volume_slider);
        volumeSlider.setValue(mediaVolume);

        recyclerView = findViewById(R.id.timers_scrollView_recyclerView);
        recyclerAdapter = new RecyclerAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recyclerAdapter);
        timerTextView = findViewById(R.id.timer_textView);
        addTimerButton = findViewById(R.id.home_add_button);
        volumeUpButton = findViewById(R.id.volume_up);
        volumeDownButton = findViewById(R.id.volume_down);
        startPauseTimerButton = findViewById(R.id.start_pause_button);
        homeButton = findViewById(R.id.home_button);
        stopTimerButton = findViewById(R.id.stop_button);
        emptyHolder = findViewById(R.id.empty_holder);
        timerToolbar = findViewById(R.id.timer_toolbar);
        timerLayout = findViewById(R.id.timer_layout);

        ItemTouchHelper.Callback callback = new ItemMoveCallback(recyclerAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        startPauseTimerButton.setOnClickListener(this);
        stopTimerButton.setOnClickListener(this);
        stopTimerButton.setVisibility(View.INVISIBLE);
        addTimerButton.setOnClickListener(this);
        volumeUpButton.setOnClickListener(this);
        volumeDownButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);

        countdownTimer = CountdownTimerFactory.getInstance(this);
        timerRunning = false;

        pauseTimeInMillis = ConstantsClass.ZERO_LONG;
        indexOfTimer = ConstantsClass.ZERO;

        stopTimerButton.setVisibility(View.GONE);
        homeButton.setVisibility(View.VISIBLE);
        startPauseTimerButton.setVisibility(View.VISIBLE);
        addTimerButton.setVisibility(View.VISIBLE);

        if (!DataHolder.getInstance().getStackNavigation().isEmpty()) {
            timerToolbar.setTitle(DataHolder.getInstance().getStackNavigation().peek());
            if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()) && DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek()) != null)
                DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup());
            else DataHolder.getInstance().setListTimerGroup(new ArrayList<>());
            emptyHolderVisibility();
            recyclerAdapter.notifyDataSetChanged();
        }

        volumeSlider.setThumbTintList(DataHolder.getInstance().getAccentColor(getApplicationContext()));
        volumeSlider.setHaloTintList(DataHolder.getInstance().getAccentColorTransparent(getApplicationContext(), 150));
        volumeSlider.setTrackInactiveTintList(DataHolder.getInstance().getAccentColorTransparent(getApplicationContext(), 50));
        volumeSlider.setTrackActiveTintList(DataHolder.getInstance().getAccentColor(getApplicationContext()));
        volumeUpButton.setIconTint(DataHolder.getInstance().getAccentColor(getApplicationContext()));
        volumeDownButton.setIconTint(DataHolder.getInstance().getAccentColor(getApplicationContext()));
        startPauseTimerButton.setIconTint(DataHolder.getInstance().getAccentColor(getApplicationContext()));
        setSupportActionBar(timerToolbar);
        initTransitionAnimations(recyclerView, startPauseTimerButton);
        DataHolder.getInstance().setDisableButtonClick(false);
        timerToolbar.setNavigationOnClickListener(v -> returnButton());
        mediaPlayer = MediaPlayer.create(this, DataHolder.getInstance().getRingtone(getApplicationContext()));
        if (mediaPlayer == null)
            mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(ConstantsClass.ZERO);
        timerStarted(false);
        stopTimer();

        volumeSlider.addOnChangeListener((slider, value, fromUser) -> {
            mediaVolume = value;
            DataHolder.getInstance().setAppVolume(mediaVolume.intValue(), this);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) value, 0);
            mediaPlayer.setVolume(mediaVolume / 100, mediaVolume / 100);
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            volumeSlider.setValue(Math.min(volumeSlider.getValue() + 10, 100));
            DataHolder.getInstance().setOriginalSystemVolume(Math.min(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), 100), getApplicationContext());
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volumeSlider.setValue(Math.max(volumeSlider.getValue() - 10, 0));
            DataHolder.getInstance().setOriginalSystemVolume(Math.max(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), 0), getApplicationContext());
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setText(String s) {
        timerTextView.setText(s);
    }

    private void initTransitionAnimations(View... view) {
        for (View v : view) {
            new CustomAnimations().slideUp(v);
        }
    }

    private void endTransitionAnimations(View... view) {
        for (View v : view) {
            new CustomAnimations().slideDown(v);
        }
    }

    private void emptyHolderVisibility() {
        if (DataHolder.getInstance().getListTimerGroup().size() <= 0)
            emptyHolder.setVisibility(View.VISIBLE);
        else emptyHolder.setVisibility(View.GONE);
    }

    public void requestDrag(RecyclerAdapter.ListItemViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    /**
     * Override onCLick
     * Call functions based on button click
     *
     * @param view of button clicked
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == volumeUpButton.getId())
            volumeSlider.setValue(Math.min(volumeSlider.getValue() + 10, 100));
        if (view.getId() == volumeDownButton.getId())
            volumeSlider.setValue(Math.max(volumeSlider.getValue() - 10, 0));
        if (!DataHolder.getInstance().getDisableButtonClick()) {
            DataHolder.getInstance().setDisableButtonClick(true);
            if (view.getId() == addTimerButton.getId()) addTimer();
            else if (view.getId() == startPauseTimerButton.getId()) {
                if (DataHolder.getInstance().getListTimerGroup().size() > 0)
                    startPauseTimer();
                else DataHolder.getInstance().setDisableButtonClick(false);
            } else if (view.getId() == stopTimerButton.getId()) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
                stopTimer();
            } else if (view.getId() == homeButton.getId())
                homeButton();
            else {
                DataHolder.getInstance().setDisableButtonClick(false);
            }
        }
    }

    /**
     * Create and add new timer in list
     */
    private void addTimer() {
        View timePickerPopupWindowView = getLayoutInflater().inflate(R.layout.timer_picker_popup, (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), false);
        PopupWindow timePickerPopupWindow = new TimePickerPopup(timePickerPopupWindowView, recyclerAdapter);
        timePickerPopupWindow.showAtLocation(findViewById(R.id.timer_layout), Gravity.CENTER, 0, 0);
    }

    private void startPauseTimer() {
        if (timerRunning) {
            setTimerPaused();
        } else {
            timerStarted(false);
        }
    }

    private void timerStarted(boolean playTone) {
        timerRunning = true;
        startPauseTimerButton.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_round_pause));
        stopTimerButton.setVisibility(View.VISIBLE);
        addTimerButton.setVisibility(View.GONE);
        countdownTimer.startTimer(pauseTimeInMillis, playTone);
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    private void setTimerPaused() {
        startPauseTimerButton.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_round_play_arrow));
        stopTimerButton.setVisibility(View.VISIBLE);
        addTimerButton.setVisibility(View.GONE);
        pauseTimeInMillis = countdownTimer.pauseTimer();
        indexOfTimer = countdownTimer.getIndexOfTimer();
        timerRunning = false;
        DataHolder.getInstance().setDisableButtonClick(false);
    }

    @Override
    public void onBackPressed() {
        returnButton();
    }

    public void stopTimer() {
        countdownTimer.repsSetOne();
        addTimerButton.setVisibility(View.VISIBLE);
        startPauseTimerButton.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_round_play_arrow));
        stopTimerButton.setVisibility(View.GONE);
        pauseTimeInMillis = ConstantsClass.ZERO_LONG;
        indexOfTimer = ConstantsClass.ZERO;
        countdownTimer.stopTimer();
        recyclerAdapter.notifyDataSetChanged();
        timerRunning = false;
        new CountDownTimer(ConstantsClass.TWO_SECOND_IN_MILLIS, ConstantsClass.FIFTY_MILLIS_IN_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                try {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(ConstantsClass.ZERO);
                    }
                } catch (Exception ignore) {
                }


            }
        }.start();
        DataHolder.getInstance().setDisableButtonClick(false);
        setText(new Timer().toString());
    }

    private void homeButton() {
        stopTimer();
        DataHolder.getInstance().getStackNavigation().clear();
        endTransitionAnimations(recyclerView, startPauseTimerButton);
        DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups());
        super.onBackPressed();
    }

    private void returnButton() {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(ConstantsClass.ZERO);
            }
            DataHolder.getInstance().setAppVolume(mediaVolume.intValue(), getApplicationContext());
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, DataHolder.getInstance().getOriginalSystemVolume(getApplicationContext()), 0);
        } catch (Exception ignore) {
        }
        if (!DataHolder.getInstance().getStackNavigation().empty()) {
            stopTimer();
            DataHolder.getInstance().getStackNavigation().pop();
            if (DataHolder.getInstance().getStackNavigation().empty()) {
                endTransitionAnimations(recyclerView, startPauseTimerButton);
                DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups());
                super.onBackPressed();
            } else {
                endTransitionAnimations(recyclerView);
                if (DataHolder.getInstance().getMapTimerGroups().containsKey(DataHolder.getInstance().getStackNavigation().peek()))
                    DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups().get(DataHolder.getInstance().getMapTimerGroups().get(DataHolder.getInstance().getStackNavigation().peek())).getListTimerGroup());
                else DataHolder.getInstance().setListTimerGroup(new ArrayList<>());
                if (findViewById(R.id.loop_button) != null) {
                    findViewById(R.id.loop_button).callOnClick();
                    findViewById(R.id.loop_button).callOnClick();
                }
                if (findViewById(R.id.increase_reps) != null && findViewById(R.id.decrease_reps) != null) {
                    findViewById(R.id.increase_reps).callOnClick();
                    findViewById(R.id.decrease_reps).callOnClick();
                }
                timerToolbar.setTitle(DataHolder.getInstance().getStackNavigation().peek());
                initTransitionAnimations(recyclerView);
                recyclerAdapter.notifyDataSetChanged();
                emptyHolderVisibility();
            }
        }
        DataHolder.getInstance().setDisableButtonClick(false);
        try {
            mediaPlayer.release();
        } catch (Exception ignore) {
        }
    }

    private void loadData() {
        if (DataHolder.getInstance().getThemeMode() != AppCompatDelegate.getDefaultNightMode())
            AppCompatDelegate.setDefaultNightMode(DataHolder.getInstance().getThemeMode());
        getWindow().setStatusBarColor(DataHolder.getInstance().getAccentColorColor(getApplicationContext()));
        recyclerView.setEdgeEffectFactory(DataHolder.getInstance().recyclerViewEdgeEffectFactory(getApplicationContext()));
    }

}
