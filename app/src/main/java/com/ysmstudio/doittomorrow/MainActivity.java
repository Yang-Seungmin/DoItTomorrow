package com.ysmstudio.doittomorrow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ysmstudio.doittomorrow.databinding.ActivityMainBinding;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.view.View;

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
        binding.content.recyclerViewTodoTomorrow.setLayoutManager(
                new LinearLayoutManager(this)
        );
        binding.content.recyclerViewTodoTomorrow.setAdapter(adapter);
    }

    public void onFabClick(View view) {
        if(adapter != null) {
            if(adapter.isAddItemVisibility()) {
                binding.fab.setShowingCheckMark(false);
                adapter.setAddItemVisibility(false);
            } else {
                binding.fab.setShowingCheckMark(true);
                adapter.setAddItemVisibility(true);
            }
        }
    }
}
