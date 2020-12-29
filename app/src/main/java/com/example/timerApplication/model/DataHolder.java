package com.example.timerApplication.model;

import androidx.navigation.NavController;

import com.example.timerApplication.timers.Timer;
import com.example.timerApplication.timers.TimerGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class DataHolder {
    private List<TimerGroup> listTimerGroup;
    private List<TimerGroup> allTimerGroups;
    private Stack<String> stackNavigation;
    private Boolean disableButtonClick;
    private Map<String, Integer> mapTimerGroups;
    private Queue<Timer> queueTimers;

    public List<TimerGroup> getAllTimerGroups() {
        return allTimerGroups;
    }

    public void setAllTimerGroups(List<TimerGroup> allTimerGroups) {
        this.allTimerGroups = allTimerGroups;
    }

    public Queue<Timer> getQueueTimers() {
        return queueTimers;
    }

    public String printAllList(){
        return new Gson().toJson(DataHolder.getInstance().getAllTimerGroups());
    }

    public void setQueueTimers(Queue<Timer> queueTimers) {
        this.queueTimers = queueTimers;
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

    private static final DataHolder dataHolder = new DataHolder();

    public void updateMap(){
        mapTimerGroups.clear();
        for(int i = 0; i<allTimerGroups.size(); i++){
            mapTimerGroups.put(allTimerGroups.get(i).getName(), i);
        }
    }

    public static DataHolder getInstance() {
        return dataHolder;
    }

    public DataHolder() {
        this.listTimerGroup = new ArrayList<>();
        this.stackNavigation = new Stack<>();
        this.disableButtonClick = new Boolean(false);
        this.mapTimerGroups = new HashMap<>();
    }
}
