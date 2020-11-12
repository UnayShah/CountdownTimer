package com.example.timerApplication.timers;

import java.util.List;

public interface IListTimers {

    void setListTimers(List<Timer> IListTimers);

    List<Timer> getListTimers();

    void add(Timer timer);

    void remove(int index);

    void remove(Timer timer);

    void set(Integer position, Timer timer);

    void swap(IListTimers listTimers, Integer fromPosition, Integer toPosition);

    Integer size();

    void print();
}
