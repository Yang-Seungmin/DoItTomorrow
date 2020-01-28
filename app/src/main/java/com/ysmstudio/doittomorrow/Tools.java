package com.ysmstudio.doittomorrow;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Tools {

    /**
     * 오늘 날짜, reset_hour, reset_minute를 기준으로 24시간동안의 시간 배열을 반환
     * @param hour 시간 오프셋
     * @param minute 분 오프셋
     * @param offsetDay 날짜 오프셋(ex. 0일 경우 오늘~내일, -1일 경우 어제~오늘)
     * @return long[]{(시작 시간 밀리초(UTC), 끝 시간 밀리초(UTC)}
     */
    public static long[] getListTimeMilis(int hour, int minute, int offsetDay) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.HOUR_OF_DAY, hour);
        calendarStart.set(Calendar.MINUTE, minute);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);
        calendarStart.add(Calendar.DATE, offsetDay);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.HOUR_OF_DAY, hour);
        calendarEnd.set(Calendar.MINUTE, minute);
        calendarEnd.set(Calendar.SECOND, 0);
        calendarEnd.set(Calendar.MILLISECOND, 0);
        calendarEnd.add(Calendar.DATE, offsetDay + 1);

        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();

        Log.d("times", "From " + dateFormat.format(calendarStart.getTimeInMillis()) + " End " + dateFormat.format(calendarEnd.getTimeInMillis()));

        return new long[]{calendarStart.getTimeInMillis(), calendarEnd.getTimeInMillis()};
    }
}
