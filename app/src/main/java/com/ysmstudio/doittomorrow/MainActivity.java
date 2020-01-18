package com.ysmstudio.doittomorrow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ysmstudio.doittomorrow.databinding.ActivityMainBinding;

import android.content.DialogInterface;
import android.os.Bundle;
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

        setRecyclerView();
    }

    private void setRecyclerView() {
        adapter = new TodoRecyclerViewAdapter();
        adapter.setList(new ArrayList<TodoData>());
        adapter.setEmptyView(binding.content.recyclerEmptyView);
        binding.content.recyclerViewTodoTomorrow.setLayoutManager(
                new LinearLayoutManager(this)
        );
        binding.content.recyclerViewTodoTomorrow.setAdapter(adapter);
    }

    public void onFabClick(View view) {
        final View inflate = View.inflate(this, R.layout.dialog_todo_item_add, null);
        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle("New todo")
                .setView(inflate)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = ((EditText) inflate.findViewById(R.id.edit_text_name));
                        if (editText.getText().toString().length() <= 0)
                            Toast.makeText(MainActivity.this, "You must enter at least one letter.", Toast.LENGTH_SHORT).show();
                        else {
                            adapter.getList().add(new TodoData(
                                    editText.getText().toString()
                            ));
                            adapter.notifyItemInserted(adapter.getItemCount());

                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
        inflate.findViewById(R.id.edit_text_name).requestFocus();
    }
}
