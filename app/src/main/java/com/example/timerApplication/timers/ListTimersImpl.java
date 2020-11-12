package com.example.timerApplication.timers;

import com.example.timerApplication.timers.IListTimers;
import com.example.timerApplication.timers.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListTimersImpl implements IListTimers {
    List<Timer> listTimers;

    public ListTimersImpl() {
        this(new ArrayList<>());
    }

    public ListTimersImpl(List<Timer> listTimers) {
        this.listTimers = listTimers;
    }

    @Override
    public List<Timer> getListTimers() {
        return listTimers;
    }

    @Override
    public void setListTimers(List<Timer> listTimers) {
        this.listTimers = listTimers;
    }

    @Override
    public void add(Timer timer) {
        listTimers.add(timer);
    }

    @Override
    public void remove(int index) {
        System.out.println("REMOVED? " + listTimers.remove(index).toString());
        System.out.println(listTimers.size());
    }

    @Override
    public void remove(Timer timer) {
        System.out.println("REMOVED? " + listTimers.remove(timer));
        System.out.println(listTimers.size());
    }

    @Override
    public void set(Integer position, Timer timer) {
        listTimers.set(position, timer);
    }

    @Override
    public void swap(IListTimers listTimers, Integer fromPosition, Integer toPosition) {
        Collections.swap(listTimers.getListTimers(), fromPosition, toPosition);
    }

    @Override
    public Integer size() {
        return listTimers.size();
    }

    @Override
    public void print() {
        System.out.println("START:");
        for (Timer t : this.getListTimers()) {
            System.out.println(t.toString());
        }
        System.out.println("END");
    }
}
