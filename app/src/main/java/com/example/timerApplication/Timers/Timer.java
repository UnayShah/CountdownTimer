package com.example.timerApplication.Timers;

import androidx.annotation.NonNull;

import com.example.timerApplication.ConstantsClass;

public class Timer {
    Integer hours;
    Integer minutes;
    Integer seconds;

    public Timer(Integer hours, Integer minutes, Integer seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Timer() {
        this(ConstantsClass.NUMBER_PICKER_HOURS_START, ConstantsClass.NUMBER_PICKER_MINUTES_START, ConstantsClass.NUMBER_PICKER_SECONDS_START);
    }

    public Timer(String timer) {
        this(Integer.valueOf(timer.split(":")[0]), Integer.valueOf(timer.split(":")[1]), Integer.valueOf(timer.split(":")[2]));
    }

    public Timer(Long timeInMillis) {
        this((int)(timeInMillis.doubleValue()/3600000), (int)((double)((timeInMillis/1000l)%60l)/60.0), ((int)Math.ceil(timeInMillis.doubleValue() / 1000.0)) % 60);
    }

    public Integer getHours() {
        return hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public Long getTimeInMilliseconds() {
        return (long)(((getHours() * 3600) + (getMinutes() * 60) + getSeconds()) * 1000);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%" + 2 + "s", this.getHours()).replace(' ', '0') + ":" + String.format("%" + 2 + "s", this.getMinutes()).replace(' ', '0') + ":" + String.format("%" + 2 + "s", this.getSeconds()).replace(' ', '0');
    }
}
