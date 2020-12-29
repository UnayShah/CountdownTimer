package com.example.timerApplication.timers;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TimerGroup {
    String name;
    TimerGroupType timerGroupType;
    Timer timer;
    List<TimerGroup> listTimerGroup;
    Boolean looped;
    Integer internalUsageCount;

    public TimerGroup() {
        setName("");
        looped = false;
        internalUsageCount = 0;
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

    public Integer getInternalUsageCount() {
        return internalUsageCount;
    }

    public void setInternalUsageCount(Integer internalUsageCount) {
        this.internalUsageCount = internalUsageCount;
    }

    public void incrementInternalUsageCount() {
        this.internalUsageCount++;
    }

    public void decrementInternalUsageCount() {
        this.internalUsageCount--;
    }

    public Boolean getLooped() {
        return looped;
    }

    public void setLooped(Boolean looped) {
        this.looped = looped;
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

    public Queue<Timer> getTimersQueue() {
        return getTimersQueue(this);
    }

    public Queue<Timer> getTimersQueue(TimerGroup timerGroup) {
        Queue<Timer> queueTimer = new LinkedList<>();
        for (TimerGroup tg : timerGroup.getListTimerGroup()) {
            if (tg.getTimerGroupType().equals(TimerGroupType.TIMER))
                queueTimer.add(tg.getTimer());
            else {
                if (!tg.getName().equals(this.getName()))
                    queueTimer.addAll(getTimersQueue(tg));
            }
        }
        return queueTimer;
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