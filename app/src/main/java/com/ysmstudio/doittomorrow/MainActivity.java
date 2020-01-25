package com.ysmstudio.doittomorrow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ysmstudio.doittomorrow.databinding.ActivityMainBinding;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TodoRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        setSupportActionBar(binding.toolbar);
        binding.content.recyclerEmptyView.textViewEmptySubtitle.setOnClickListener(this::onViewPreviousTodoClock);

        setRecyclerView();
    }

    private void setRecyclerView() {
        adapter = new TodoRecyclerViewAdapter();
        adapter.setList(new ArrayList<TodoData>());
        adapter.setEmptyView(binding.content.recyclerEmptyView.getRoot());
        binding.content.recyclerViewTodoTomorrow.setLayoutManager(
                new LinearLayoutManager(this)
        );
        binding.content.recyclerViewTodoTomorrow.setAdapter(adapter);
    }

    public void onFabClick(View view) {
        final View inflate = View.inflate(this, R.layout.dialog_todo_item_add, null);
        EditText editText = inflate.findViewById(R.id.edit_text_name);
        AlertDialog dialog = new MaterialAlertDialogBuilder(this, R.style.Theme_MaterialComponents_DayNight_Dialog)
                .setTitle("New todo")
                .setView(inflate)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveTodo(editText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                saveTodo(editText.getText().toString());
                dialog.cancel();
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
            adapter.getList().add(new TodoData(s));
            adapter.notifyItemInserted(adapter.getItemCount());
        }
    }

    public void onViewPreviousTodoClock(View view) {
        // TODO: 2020-01-19 previous activity 이동
    }
}
