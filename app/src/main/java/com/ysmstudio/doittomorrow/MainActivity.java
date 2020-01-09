package com.ysmstudio.doittomorrow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ysmstudio.doittomorrow.databinding.ActivityMainBinding;

import android.os.Bundle;
import android.view.View;

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
        binding.content.recyclerViewTodoTomorrow.setLayoutManager(
                new LinearLayoutManager(this)
        );
        binding.content.recyclerViewTodoTomorrow.setAdapter(adapter);
    }

    public void onFabClick(View view) {

    }
}
