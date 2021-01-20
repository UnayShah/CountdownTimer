package com.example.countdownTimer.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;

import androidx.core.content.ContextCompat;

import com.example.countdownTimer.R;
import com.example.countdownTimer.common.ConstantsClass;
import com.example.countdownTimer.timers.TimerGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class DataHolder {
    private static final DataHolder dataHolder = new DataHolder();
    private final Stack<String> stackNavigation;
    private final Map<String, Integer> mapTimerGroups = new HashMap<>();
    private List<TimerGroup> listTimerGroup;
    private List<TimerGroup> allTimerGroups;
    private Boolean disableButtonClick = false;

    public DataHolder() {
        this.listTimerGroup = new ArrayList<>();
        this.stackNavigation = new Stack<>();
        this.disableButtonClick = false;
    }

    public static DataHolder getInstance() {
        return dataHolder;
    }

    public void saveData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstantsClass.HOME_LIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstantsClass.HOME_LIST, new Gson().toJson(DataHolder.getInstance().getAllTimerGroups()));
        editor.apply();
        editor.commit();
        updateMap();
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
        setListTimerGroup();
    }

    public ColorStateList getAccentColor(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstantsClass.HOME_LIST, Context.MODE_PRIVATE);
        return ColorStateList.valueOf(sharedPreferences.getInt(ConstantsClass.ACCENT_COLOR, ContextCompat.getColor(context, R.color.accent)));
    }

    public int getAccentColorColor(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstantsClass.HOME_LIST, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(ConstantsClass.ACCENT_COLOR, ContextCompat.getColor(context, R.color.accent));
    }

    public boolean getVibration(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstantsClass.HOME_LIST, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(ConstantsClass.VIBRATION_BOOL, true);
    }

    public void setVibration(Context context, boolean bool) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstantsClass.HOME_LIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ConstantsClass.VIBRATION_BOOL, bool);
        editor.apply();
        editor.commit();
    }

    public void setAccentColor(Context context, int color) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstantsClass.HOME_LIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ConstantsClass.ACCENT_COLOR, color);
        editor.apply();
        editor.commit();
    }

    public void updateName(int position, String newName) {
        for (TimerGroup timerGroup : allTimerGroups) {
            for (TimerGroup tg : timerGroup.getListTimerGroup()) {
                if (tg.getName().equals(allTimerGroups.get(position).getName())) {
                    tg.setName(newName);
                }
            }
        }
        allTimerGroups.get(position).setName(newName);
        updateMap();
    }

    private void setListTimerGroup() {
        if (stackNavigation.empty())
            listTimerGroup = allTimerGroups;
        else
            listTimerGroup = allTimerGroups.get(mapTimerGroups.get(stackNavigation.peek())).getListTimerGroup();
    }

    public List<TimerGroup> getAllTimerGroups() {
        if (allTimerGroups == null) allTimerGroups = new ArrayList<>();
        return allTimerGroups;
    }

    public void setAllTimerGroups(List<TimerGroup> allTimerGroups) {
        this.allTimerGroups = allTimerGroups;
    }

    public String printAllList() {
        return new Gson().toJson(DataHolder.getInstance().getAllTimerGroups());
    }

    public Map<String, Integer> getMapTimerGroups() {
        return mapTimerGroups;
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
