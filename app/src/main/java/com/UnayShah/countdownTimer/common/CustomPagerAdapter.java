package com.UnayShah.countdownTimer.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.UnayShah.countdownTimer.R;

import java.util.ArrayList;
import java.util.List;

public class CustomPagerAdapter extends PagerAdapter {
    private Context context;
    private List<ViewGroup> containers;

    public CustomPagerAdapter(Context context) {
        super();
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        containers = new ArrayList<>();
        containers.add((ViewGroup) inflater.inflate(R.layout.tutorial_home_screen_empty, null, false));
        containers.add((ViewGroup) inflater.inflate(R.layout.tutorial_home_screen_popup_timername, null, false));
        containers.add((ViewGroup) inflater.inflate(R.layout.tutorial_home_screen, null, false));
        containers.add((ViewGroup) inflater.inflate(R.layout.tutorial_timer_layout_empty, null, false));
        containers.add((ViewGroup) inflater.inflate(R.layout.tutorial_timer_layout_popup_timerpicker, null, false));
        containers.add((ViewGroup) inflater.inflate(R.layout.tutorial_timer_layout, null, false));
    }

    public int containersSize() {
        return containers.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
//        ModelObject modelObject = ModelObject.values()[position];
        container.addView(containers.get(position));
        return containers.get(position);
    }

    @Override
    public int getCount() {
        return containers.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
