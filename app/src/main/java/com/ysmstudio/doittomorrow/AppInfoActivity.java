package com.ysmstudio.doittomorrow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.ysmstudio.doittomorrow.databinding.ActivityAppInfoBinding;

import java.util.ArrayList;

public class AppInfoActivity extends AppCompatActivity {

    ActivityAppInfoBinding binding;
    AppInfoRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_app_info);
        binding.setActivity(this);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initAppinfoData();

        binding.recyclerViewAppInfo.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewAppInfo.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    private void initAppinfoData() {
        ArrayList<AppInfoData> appInfoData = new ArrayList<>();
        appInfoData.add(new AppInfoData(
                getString(R.string.str_app_developer_title),
                getString(R.string.str_app_developer_content)
        ));
        appInfoData.add(new AppInfoData(
                getString(R.string.str_app_version_title),
                getString(R.string.str_app_version_content)
        ));
        appInfoData.add(new AppInfoData(
                getString(R.string.str_app_build_title),
                getString(R.string.str_app_build_content)
        ));

        adapter = new AppInfoRecyclerViewAdapter();
        adapter.setList(appInfoData);
    }
}