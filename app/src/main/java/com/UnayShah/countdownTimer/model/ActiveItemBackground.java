package com.UnayShah.countdownTimer.model;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import androidx.core.content.ContextCompat;

import com.UnayShah.countdownTimer.R;

public class ActiveItemBackground extends ShapeDrawable.ShaderFactory {
    Context context;
    int totalTime;
    int timePassed;
    PaintDrawable pd;
    private int[] gradientColors;
    private float[] gradientPositions;

    public ActiveItemBackground(Context context) {
        this.context = context;
        gradientColors = new int[]{DataHolder.getInstance().getAccentColorColor(context), DataHolder.getInstance().getAccentColorColor(context), ContextCompat.getColor(context, R.color.iconTintDark), ContextCompat.getColor(context, R.color.iconTintDark)};
        gradientPositions = new float[]{0, 0.5f, 0.5f, 1};
        pd = new PaintDrawable();
    }

    public PaintDrawable getPd() {
        pd.setShape(new RectShape());
        pd.setCornerRadius(context.getResources().getDimension(R.dimen.padding_small_medium));
        pd.setShaderFactory(this);
        return pd;
    }

    private void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    private void setTimePassed(int timePassed) {
        this.timePassed = timePassed;
    }

    public Shader resize(int timePassed, int totalTime, int width, int height) {
        setTimePassed(timePassed);
        setTotalTime(totalTime);
        gradientPositions[1] = ((float) timePassed / totalTime);
        gradientPositions[2] = ((float) timePassed / totalTime);
        return resize(width, height);
    }


    @Override
    public Shader resize(int width, int height) {
        return new LinearGradient(0, height, width, height, gradientColors, gradientPositions, Shader.TileMode.CLAMP);
    }
}
