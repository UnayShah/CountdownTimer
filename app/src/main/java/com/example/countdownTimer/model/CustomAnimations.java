package com.example.countdownTimer.model;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.view.View;

import com.example.countdownTimer.common.ConstantsClass;

public class CustomAnimations {

    public void slideUp(View view) {
        view.animate().translationY(Resources.getSystem().getDisplayMetrics().heightPixels / 2.0f).alpha(0).setDuration(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.animate().translationY(0).alpha(1f).setDuration(ConstantsClass.VIBRATE_MEDIUM_LONG);
                return;
            }
        });
    }

    public void slideDown(View view) {
        view.animate().translationY(0).alpha(1f).setDuration(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.animate().translationY(Resources.getSystem().getDisplayMetrics().heightPixels / 2.0f).alpha(0).setDuration(ConstantsClass.VIBRATE_MEDIUM_LONG);
                return;
            }
        });
    }

}
