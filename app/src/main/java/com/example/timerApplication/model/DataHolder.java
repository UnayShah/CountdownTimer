package com.example.timerApplication.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.timerApplication.common.ConstantsClass;
import com.example.timerApplication.timers.Timer;
import com.example.timerApplication.timers.TimerGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class DataHolder {
    private static final DataHolder dataHolder = new DataHolder();
    private final Stack<String> stackNavigation;
    private List<TimerGroup> listTimerGroup;
    private List<TimerGroup> allTimerGroups;
    private Boolean disableButtonClick = false;
    private Map<String, Integer> mapTimerGroups;
    private Queue<Timer> queueTimers;

    public DataHolder() {
        this.listTimerGroup = new ArrayList<>();
        this.stackNavigation = new Stack<>();
        this.disableButtonClick = false;
        this.mapTimerGroups = new HashMap<>();
    }

    public static DataHolder getInstance() {
        return dataHolder;
    }

    public void saveData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstantsClass.HOME_LIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString(ConstantsClass.HOME_LIST, new Gson().toJson(DataHolder.getInstance().getAllTimerGroups()));
        editor.apply();
        editor.commit();
        updateMap();
        updateQueue();
        setListTimerGroup();
    }

    public void loadData(Context context) {
        if (DataHolder.getInstance().getAllTimerGroups() != null && DataHolder.getInstance().getAllTimerGroups().size() > 0) {
            DataHolder.getInstance().setListTimerGroup(DataHolder.getInstance().getAllTimerGroups());
        } else {
            SharedPreferences sharedPreferences = context.getSharedPreferences(ConstantsClass.HOME_LIST, Context.MODE_PRIVATE);
            ArrayList<TimerGroup> list = new Gson().fromJson(sharedPreferences.getString(ConstantsClass.HOME_LIST, new Gson().toJson(new ArrayList<TimerGroup>())), new TypeToken<List<TimerGroup>>() {
            }.getType());
            if (list != null && list.size() > 0) {
                DataHolder.getInstance().setAllTimerGroups(list);
            } else {
                DataHolder.getInstance().setAllTimerGroups(new ArrayList<>());
            }
        }
        updateMap();
        updateQueue();
        setListTimerGroup();
    }

    private void setListTimerGroup() {
        if (stackNavigation.empty())
            listTimerGroup = allTimerGroups;
        else
            listTimerGroup = allTimerGroups.get(mapTimerGroups.get(stackNavigation.peek())).getListTimerGroup();
    }

    private void updateQueue() {
        if (!stackNavigation.empty() && mapTimerGroups.containsKey(stackNavigation.peek()) && mapTimerGroups.get(stackNavigation.peek()) != null)
            queueTimers = allTimerGroups.get(mapTimerGroups.get(stackNavigation.peek())).getTimersQueue();
    }

    public List<TimerGroup> getAllTimerGroups() {
        if (allTimerGroups == null) allTimerGroups = new ArrayList<>();
        return allTimerGroups;
    }

    public void setAllTimerGroups(List<TimerGroup> allTimerGroups) {
        this.allTimerGroups = allTimerGroups;
    }

    public Queue<Timer> getQueueTimers() {
        return queueTimers;
    }

    public void setQueueTimers(Queue<Timer> queueTimers) {
        this.queueTimers = queueTimers;
    }

    public String printAllList() {
        return new Gson().toJson(DataHolder.getInstance().getAllTimerGroups());
    }

    public Map<String, Integer> getMapTimerGroups() {
        return mapTimerGroups;
    }

    public void setMapTimerGroups(Map<String, Integer> mapTimerGroups) {
        this.mapTimerGroups = mapTimerGroups;
    }

    public Boolean getDisableButtonClick() {
        return disableButtonClick;
    }

    public void setDisableButtonClick(Boolean disableButtonClick) {
        this.disableButtonClick = disableButtonClick;
    }

    public List<TimerGroup> getListTimerGroup() {
        return listTimerGroup;
    }

    public void setListTimerGroup(List<TimerGroup> listTimerGroup) {
        this.listTimerGroup = listTimerGroup;
    }

    public Stack<String> getStackNavigation() {
        return stackNavigation;
    }

    public void updateMap() {
        mapTimerGroups.clear();
        for (int i = 0; i < allTimerGroups.size(); i++) {
            mapTimerGroups.put(allTimerGroups.get(i).getName(), i);
        }
    }
}
