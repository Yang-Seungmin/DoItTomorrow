package com.ysmstudio.doittomorrow;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SquareCalendarView extends CalendarView {

    String measureFit;

    public SquareCalendarView(@NonNull Context context) {
        super(context);
    }

    public SquareCalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SquareCalendarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public SquareCalendarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        measureFit = context.obtainStyledAttributes(attrs, R.styleable.SquareCalendarView)
                .getNonResourceString(R.styleable.SquareCalendarView_fit);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(measureFit.equals("width")) super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        else if(measureFit.equals("height")) super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        else super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
