package com.ysmstudio.doittomorrow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ysmstudio.doittomorrow.databinding.ActivityMainBinding;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        showRecyclerViewProgressBar();
        loadTodoData();
    }

    private void loadTodoData() {
        RealmConfiguration todoRealmConfiguration = new RealmConfiguration.Builder()
                .name("todos.realm").build();

        todoRealm = Realm.getInstance(todoRealmConfiguration);
        long[] times = getListTimeMilis();
        todoDataRealmResults = todoRealm.where(TodoData.class)
                .greaterThan("createdDate", times[0])
                .lessThan("createdDate", times[1])
                .findAllAsync();
        todoDataRealmResults.addChangeListener(todoDataRealmChangeListener);
    }

    private long[] getListTimeMilis() {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.HOUR_OF_DAY, timePreference.getInt("reset_hour", 6));
        calendarStart.set(Calendar.MINUTE, timePreference.getInt("reset_minute", 0));
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);
        calendarStart.add(Calendar.DATE, -1);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.HOUR_OF_DAY, timePreference.getInt("reset_hour", 6));
        calendarEnd.set(Calendar.MINUTE, timePreference.getInt("reset_minute", 0));
        calendarEnd.set(Calendar.SECOND, 0);
        calendarEnd.set(Calendar.MILLISECOND, 0);

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
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerView() {
        adapter = new TodoRecyclerViewAdapter();

        adapter.setList(new ArrayList<>(todoDataRealmResults));

        binding.content.recyclerViewTodoTomorrow.setLayoutManager(
                new LinearLayoutManager(this)
        );
        binding.content.recyclerViewTodoTomorrow.setAdapter(adapter);
    }

    public void onFabClick(View view) {
        showAddDialog();
    }

    private void showAddDialog() {
        final View inflate = View.inflate(this, R.layout.dialog_todo_item_add, null);
        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.dialog_new_todo_title))
                .setView(inflate)
                .setPositiveButton(getString(R.string.dialog_new_todo_create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = ((EditText) inflate.findViewById(R.id.edit_text_name));
                        if (editText.getText().toString().length() <= 0)
                            Toast.makeText(MainActivity.this, "You must enter at least one letter.", Toast.LENGTH_SHORT).show();
                        else {
                            TodoData newTodoData = new TodoData(editText.getText().toString());
                            adapter.getList().add(newTodoData);
                            adapter.notifyItemInserted(adapter.getItemCount());
                            todoRealm.beginTransaction();
                            todoRealm.copyToRealm(newTodoData);
                            todoRealm.commitTransaction();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.dialog_button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
        inflate.findViewById(R.id.edit_text_name).requestFocus();
    }
}
