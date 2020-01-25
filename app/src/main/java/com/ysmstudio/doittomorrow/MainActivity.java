package com.ysmstudio.doittomorrow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ysmstudio.doittomorrow.databinding.ActivityMainBinding;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TodoRecyclerViewAdapter adapter;

    private Realm todoRealm;
    private RealmResults<TodoData> todoDataRealmResults;
    private RealmChangeListener<RealmResults<TodoData>> todoDataRealmChangeListener = new RealmChangeListener<RealmResults<TodoData>>() {
        @Override
        public void onChange(RealmResults<TodoData> todoData) {
            setRecyclerView();
            hideRecyclerViewProgressBar();
        }
    };

    private SharedPreferences timePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        timePreference = getSharedPreferences("pref_time", MODE_PRIVATE);

        setSupportActionBar(binding.toolbar);
        binding.content.recyclerEmptyView.getRoot().setOnClickListener(this::onViewPreviousTodoClock);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTodoData();
    }

    private void loadTodoData() {
        showRecyclerViewProgressBar();
        RealmConfiguration todoRealmConfiguration = new RealmConfiguration.Builder()
                .name("todos.realm").build();

        todoRealm = Realm.getInstance(todoRealmConfiguration);
        long[] times = getListTimeMilis();
        todoDataRealmResults = todoRealm.where(TodoData.class)
                .greaterThanOrEqualTo("createdDate", times[0])
                .lessThanOrEqualTo("createdDate", times[1])
                .findAllAsync();
        todoDataRealmResults.addChangeListener(todoDataRealmChangeListener);
    }

    private long[] getListTimeMilis() {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.HOUR_OF_DAY, timePreference.getInt("reset_hour", 6));
        calendarStart.set(Calendar.MINUTE, timePreference.getInt("reset_minute", 0));
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.HOUR_OF_DAY, timePreference.getInt("reset_hour", 6));
        calendarEnd.set(Calendar.MINUTE, timePreference.getInt("reset_minute", 0));
        calendarEnd.set(Calendar.SECOND, 0);
        calendarEnd.set(Calendar.MILLISECOND, 0);
        calendarEnd.add(Calendar.DATE, 1);

        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();

        Log.d("times", "From " + dateFormat.format(calendarStart.getTimeInMillis()) + " End " + dateFormat.format(calendarEnd.getTimeInMillis()));

        return new long[]{calendarStart.getTimeInMillis(), calendarEnd.getTimeInMillis()};
    }

    private void showRecyclerViewProgressBar() {
        binding.content.progressBar.setVisibility(View.VISIBLE);
        binding.content.recyclerViewTodoTomorrow.setVisibility(View.GONE);
    }

    private void hideRecyclerViewProgressBar() {
        binding.content.progressBar.setVisibility(View.GONE);
        binding.content.recyclerViewTodoTomorrow.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.item_view_prev_todo:
                onViewPreviousTodoClock(null);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerView() {
        adapter = new TodoRecyclerViewAdapter();
        adapter.setList(new ArrayList<>(todoDataRealmResults));
        adapter.setEmptyView(binding.content.recyclerEmptyView.getRoot());
        binding.content.recyclerViewTodoTomorrow.setLayoutManager(
                new LinearLayoutManager(this)
        );
        binding.content.recyclerViewTodoTomorrow.setAdapter(adapter);
        binding.content.recyclerViewTodoTomorrow.setOnSwipeListener(onSwipeListener);
    }

    public void onFabClick(View view) {
        showAddDialog();
    }

    private void showAddDialog() {
        final View inflate = View.inflate(this, R.layout.dialog_todo_item_add, null);
        EditText editText = inflate.findViewById(R.id.edit_text_name);
        AlertDialog dialog = new MaterialAlertDialogBuilder(this, R.style.Theme_MaterialComponents_DayNight_Dialog)
                .setTitle("New todo")
                .setView(inflate)
                .setPositiveButton(getString(R.string.dialog_new_todo_create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveTodo(editText.getText().toString());
                    }
                })
                .setNegativeButton(getString(R.string.dialog_button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    saveTodo(editText.getText().toString());
                    dialog.cancel();
                }
                return false;
            }
        });

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
        inflate.findViewById(R.id.edit_text_name).requestFocus();
    }

    private void saveTodo(String s) {
        if (s.length() <= 0)
            Toast.makeText(MainActivity.this, "You must enter at least one letter.", Toast.LENGTH_SHORT).show();
        else {
            TodoData todoData = new TodoData(s);
            adapter.getList().add(todoData);
            todoRealm.beginTransaction();
            todoRealm.copyToRealm(todoData);
            todoRealm.commitTransaction();
            adapter.notifyItemInserted(adapter.getItemCount());
        }
    }

    public void onViewPreviousTodoClock(View view) {
        startActivity(new Intent(this, CalendarActivity.class));
    }

    private SwipeDeleteRecyclerView.OnSwipeListener onSwipeListener = new SwipeDeleteRecyclerView.OnSwipeListener() {
        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, final int position, int direction) {
            if (todoRealm != null) {
                todoRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        todoRealm.beginTransaction();
                        todoRealm.where(TodoData.class)
                                .equalTo("createdDate", adapter.getList().get(position).getCreatedDate())
                                .findAll()
                                .deleteAllFromRealm();
                        todoRealm.commitTransaction();

                        Snackbar.make(binding.container, "삭제되었습니다.", BaseTransientBottomBar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {

                    }
                });
            }
        }
    };
}
