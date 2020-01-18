package com.ysmstudio.doittomorrow;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AnimatedFab extends FloatingActionButton {

    private AnimatedVectorDrawable plusToCheck, checkToPlus;
    private boolean isShowingCheckMark;
    public AnimatedFab(@NonNull Context context) {
        super(context);
        init();
    }

    public AnimatedFab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimatedFab(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        isShowingCheckMark = false;
        plusToCheck = (AnimatedVectorDrawable) getContext().getDrawable(R.drawable.ic_fab_plus_to_check);
        checkToPlus = (AnimatedVectorDrawable) getContext().getDrawable(R.drawable.ic_fab_check_to_plus);
        setImageDrawable(plusToCheck);
    }

    public boolean isShowingCheckMark() {
        return isShowingCheckMark;
    }

    public void setShowingCheckMark(boolean showingCheckMark) {
        final AnimatedVectorDrawable drawable = isShowingCheckMark ? checkToPlus : plusToCheck;
        setImageDrawable(drawable);
        drawable.start();
        isShowingCheckMark = showingCheckMark;
    }
}
