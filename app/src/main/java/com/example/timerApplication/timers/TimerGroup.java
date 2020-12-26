package com.example.timerApplication.timers;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TimerGroup {
    String name;
    TimerGroupType timerGroupType;
    Timer timer;
    List<TimerGroup> listTimerGroup;
    Boolean looped;

    public Boolean getLooped() {
        return looped;
    }

    public void setLooped(Boolean looped) {
        this.looped = looped;
    }

    public TimerGroup() {
        setName("");
        looped = false;
        listTimerGroup = new ArrayList<>();
    }

    public TimerGroup(TimerGroupType timerGroupType) {
        this();
        this.setTimerGroupType(timerGroupType);
        if (timerGroupType.equals(TimerGroupType.TIMER)) setTimer(new Timer());
    }

    public TimerGroup(Timer timer) {
        this();
        this.timer = timer;
        timerGroupType = TimerGroupType.TIMER;
    }

    public TimerGroup(String text, TimerGroupType timerGroupType) {
        this();
        this.timerGroupType = timerGroupType;
        if (timerGroupType == TimerGroupType.TIMER && text.matches("[0-9][0-9]:[0-5][0-9]:[0-5][0-9]")) {
            this.timer = new Timer(text);
        } else {
            this.name = text;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TimerGroupType getTimerGroupType() {
        return timerGroupType;
    }

    public void setTimerGroupType(TimerGroupType timerGroupType) {
        this.timerGroupType = timerGroupType;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void setTimer(String timerString) {
        this.timer = new Timer(timerString);
    }

    public List<TimerGroup> getListTimerGroup() {
        return listTimerGroup;
    }

    public void setListTimerGroup(List<TimerGroup> listTimerGroup) {
        this.listTimerGroup = listTimerGroup;
    }

    @NonNull
    @Override
    public String toString() {
        if (this.getTimerGroupType().equals(TimerGroupType.TIMER)) return timer.toString();
        else return name;
    }
}