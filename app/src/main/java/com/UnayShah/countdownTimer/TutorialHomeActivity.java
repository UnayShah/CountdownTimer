package com.UnayShah.countdownTimer;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.UnayShah.countdownTimer.common.CustomPagerAdapter;
import com.google.android.material.button.MaterialButton;

public class TutorialHomeActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    ViewPager viewPager;
    MaterialButton tutorialNext;
    MaterialButton tutorialPrev;
    Integer item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);
        item = 0;
        viewPager = findViewById(R.id.tutorial_viewPager);
        viewPager.setAdapter(new CustomPagerAdapter(getApplicationContext()));
        tutorialNext = findViewById(R.id.tutorial_next);
        tutorialPrev = findViewById(R.id.tutorial_previous);
        viewPager.setCurrentItem(item);
        tutorialNext.setOnClickListener(this);
        tutorialPrev.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
        tutorialPrev.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == tutorialNext.getId() && item == 5) onBackPressed();
        if (v.getId() == tutorialNext.getId()) item++;
        else if (v.getId() == tutorialPrev.getId()) item--;
        viewPager.setCurrentItem(item);
//        item = viewPager.getCurrentItem();
//        if (item == 0)
//            tutorialPrev.setVisibility(View.GONE);
//        else
//            tutorialPrev.setVisibility(View.VISIBLE);
        System.out.println("Current" + viewPager.getCurrentItem());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        item = viewPager.getCurrentItem();
        if (item == 0)
            tutorialPrev.setVisibility(View.GONE);
        else
            tutorialPrev.setVisibility(View.VISIBLE);
        if (item == 5)
            tutorialNext.setText(R.string.done);
        else
            tutorialNext.setText(R.string.next);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}

/*
<ImageView
            android:id="@+id/tutorial_title_image"
            android:layout_width="wrap_content"
            android:layout_height="0dp"
            android:contentDescription="@string/title_image"
            android:forceDarkAllowed="false"
            android:paddingLeft="@dimen/padding_large"
            android:paddingRight="@dimen/padding_large"
            android:src="@drawable/ic_countdown_app_title"
            app:layout_constraintHeight_percent="0.2"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <LinearLayout
            android:id="@+id/tutorial_timerGroupScrollViewRecyclerView"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_marginStart="@dimen/padding_vsmall"
            android:layout_marginEnd="@dimen/padding_vsmall"
            android:orientation="vertical"
            app:layout_constraintHeight_percent="0.6"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toBottomOf="@id/tutorial_title_image">

            <include
                layout="@layout/tutorial_add_timer_1"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_margin="@dimen/padding_vvsmall" />

            <include
                layout="@layout/add_timer"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_margin="@dimen/padding_vvsmall" />
        </LinearLayout>

        <androidx.constraintlayout.widget.ConstraintLayout
            android:id="@+id/tutorial_empty_holder"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:layout_constraintBottom_toTopOf="@id/tutorial_home_buttons_layout"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent">

            <TextView
                android:id="@+id/tutorial_empty_holder_text"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Add new\ntimer group"
                android:textAlignment="center"
                android:textAppearance="@style/TextAppearance.MaterialComponents.Headline5"
                android:textColor="@color/accent"
                app:layout_constraintBottom_toTopOf="@id/tutorial_empty_holder_image"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent" />

            <ImageView
                android:id="@+id/tutorial_empty_holder_image"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:src="@drawable/ic_pointing_arrow"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:tint="@color/iconTint" />
        </androidx.constraintlayout.widget.ConstraintLayout>

        <androidx.constraintlayout.widget.ConstraintLayout
            android:id="@+id/tutorial_settings"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:layout_constraintBottom_toTopOf="@id/tutorial_home_buttons_layout"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent">

            <TextView
                android:id="@+id/tutorial_settings_text"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Settings"
                android:textAppearance="@style/TextAppearance.MaterialComponents.Headline5"
                android:textColor="@color/accent"
                app:layout_constraintBottom_toTopOf="@id/tutorial_settings_image"
                app:layout_constraintRight_toRightOf="parent" />

            <ImageView
                android:id="@+id/tutorial_settings_image"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:src="@drawable/ic_pointing_arrow"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:tint="@color/iconTint" />
        </androidx.constraintlayout.widget.ConstraintLayout>

        <androidx.constraintlayout.widget.ConstraintLayout
            android:id="@+id/tutorial_home_buttons_layout"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHeight_percent="0.15"
            app:layout_constraintStart_toStartOf="parent">


            <com.google.android.material.button.MaterialButton
                android:id="@+id/tutorial_home_button"
                style="@style/Widget.MaterialComponents.Button.Icon"
                android:layout_width="50dp"
                android:layout_height="50dp"
                android:backgroundTint="@color/iconTintDark"
                android:elevation="12dp"
                android:insetLeft="0dp"
                android:insetTop="0dp"
                android:insetRight="0dp"
                android:insetBottom="0dp"
                android:padding="0dp"
                android:stateListAnimator="@null"
                android:transitionName="Return Button Transition"
                app:icon="@drawable/ic_resource_return"
                app:iconGravity="textStart"
                app:iconPadding="0dp"
                app:iconSize="24dp"
                app:iconTint="@color/iconTint"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toLeftOf="@id/tutorial_home_add_button"
                app:layout_constraintTop_toTopOf="parent"
                app:rippleColor="@color/iconTint"
                app:shapeAppearanceOverlay="@style/ShapeAppearanceOverlay.CountdownTimer.Button.Circle" />

            <com.google.android.material.button.MaterialButton
                android:id="@+id/tutorial_home_add_button"
                style="@style/Widget.MaterialComponents.Button.Icon"
                android:layout_width="70dp"
                android:layout_height="70dp"
                android:backgroundTint="@color/iconTintDark"
                android:elevation="12dp"
                android:insetLeft="0dp"
                android:insetTop="0dp"
                android:insetRight="0dp"
                android:insetBottom="0dp"
                android:padding="0dp"
                android:stateListAnimator="@null"
                android:transitionName="timer_name_popup_transition"
                app:icon="@drawable/ic_round_add"
                app:iconGravity="textStart"
                app:iconPadding="0dp"
                app:iconSize="50dp"
                app:iconTint="@color/accent"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toRightOf="@id/tutorial_home_button"
                app:layout_constraintRight_toLeftOf="@id/tutorial_settings_button"
                app:layout_constraintTop_toTopOf="parent"
                app:rippleColor="@color/iconTint"
                app:shapeAppearanceOverlay="@style/ShapeAppearanceOverlay.CountdownTimer.Button.Circle" />

            <com.google.android.material.button.MaterialButton
                android:id="@+id/tutorial_settings_button"
                style="@style/Widget.MaterialComponents.Button.Icon"
                android:layout_width="50dp"
                android:layout_height="50dp"
                android:backgroundTint="@color/iconTintDark"
                android:elevation="12dp"
                android:insetLeft="0dp"
                android:insetTop="0dp"
                android:insetRight="0dp"
                android:insetBottom="0dp"
                android:padding="0dp"
                android:stateListAnimator="@null"
                android:transitionName="Return Button Transition"
                app:icon="@drawable/ic_round_settings_24"
                app:iconGravity="textStart"
                app:iconPadding="0dp"
                app:iconSize="24dp"
                app:iconTint="@color/iconTint"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toRightOf="@id/tutorial_home_add_button"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:rippleColor="@color/iconTint"
                app:shapeAppearanceOverlay="@style/ShapeAppearanceOverlay.CountdownTimer.Button.Circle" />
        </androidx.constraintlayout.widget.ConstraintLayout>
    </androidx.viewpager.widget.ViewPager>
 */