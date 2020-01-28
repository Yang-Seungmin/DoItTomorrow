package com.ysmstudio.doittomorrow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import com.ysmstudio.doittomorrow.databinding.ActivityCalendarBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * 캘린더의 날짜를 선택하여 그 날짜로부터 24시간 동안의 할 일이 있으면 리스트로 보여주는 액티비티
 */
public class CalendarActivity extends AppCompatActivity {

    private ActivityCalendarBinding binding;

    private SharedPreferences timePreference;

    private Realm todoRealm;
    private RealmResults<TodoData> todoDataRealmResults;

    private TodoRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar);
        binding.setActivity(this);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timePreference = getSharedPreferences("pref_time", MODE_PRIVATE);

        binding.calendarView.setOnDateChangeListener(onDateChangeListener);

        initRealm();
        setRecyclerView();
        initCalendarAndRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * onCreate에서 캘린더의 초기값을 설정하여 바로 보여줄 수 있도록 하는 함수
     */
    private void initCalendarAndRecyclerView() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        binding.calendarView.setDate(calendar.getTimeInMillis());
        onDateChangeListener.onSelectedDayChange(binding.calendarView,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Realm Database의 객체를 불러오는 함수
     */
    private void initRealm() {
        RealmConfiguration todoRealmConfiguration = new RealmConfiguration.Builder()
                .name("todos.realm").build();

        todoRealm = Realm.getInstance(todoRealmConfiguration);
    }

    /**
     * RecyclerView Adapter 객체를 만들고 RecyclerView에 붙인다.
     */
    private void setRecyclerView() {
        adapter = new TodoRecyclerViewAdapter();
        adapter.setList(new ArrayList<TodoData>());
        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );
        binding.recyclerView.setAdapter(adapter);
    }

    /**
     * 위 시간 사이의 모든 Realm 내의 객체를 가져와 리스트로 보여준다.
     * @param startMillis 시작 시간 밀리초(UTC)
     * @param endMillis 끝 시간 밀리초(UTC)
     */
    private void setRecyclerViewItem(long startMillis, long endMillis) {
        if (BuildConfig.DEBUG)
            Log.d("startMillis", startMillis + "~" + endMillis);

        todoDataRealmResults = todoRealm.where(TodoData.class)
                .greaterThanOrEqualTo("createdDate", startMillis)
                .lessThanOrEqualTo("createdDate", endMillis)
                .findAllAsync();
        todoDataRealmResults.addChangeListener(todoDataRealmChangeListener);
    }

    /**
     * CalendarView의 날짜를 바꿀 때마다 RecyclerView의 아이템을 변경하는 작업을 수행
     */
    CalendarView.OnDateChangeListener onDateChangeListener = new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
            Calendar calendarStart = Calendar.getInstance();
            Calendar calendarEnd = Calendar.getInstance();
            calendarStart.set(year, month, dayOfMonth,
                    timePreference.getInt("reset_hour", 6),
                    timePreference.getInt("reset_minute", 0),
                    0);
            calendarEnd.set(year, month, dayOfMonth,
                    timePreference.getInt("reset_hour", 6),
                    timePreference.getInt("reset_minute", 0),
                    0);
            calendarEnd.add(Calendar.DAY_OF_MONTH, 1);

            setRecyclerViewItem(calendarStart.getTimeInMillis(), calendarEnd.getTimeInMillis());

            binding.textView.setText(
                    SimpleDateFormat.getDateTimeInstance().format(calendarStart.getTimeInMillis()) +
                    " ~ " +
                    SimpleDateFormat.getDateTimeInstance().format(calendarEnd.getTimeInMillis()));
        }
    };

    /**
     * Realm의 데이터를 가져왔을 때 작업을 수행
     */
    private RealmChangeListener<RealmResults<TodoData>> todoDataRealmChangeListener = new RealmChangeListener<RealmResults<TodoData>>() {
        @Override
        public void onChange(RealmResults<TodoData> todoData) {
            adapter.setList(new ArrayList<>(todoDataRealmResults));
            adapter.notifyDataSetChanged();
        }
    };
}