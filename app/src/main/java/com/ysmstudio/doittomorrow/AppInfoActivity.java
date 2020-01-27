package com.ysmstudio.doittomorrow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class AppInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

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

        RecyclerView recyclerView = findViewById(R.id.recycler_view_app_info);
        AppInfoRecyclerViewAdapter adapter = new AppInfoRecyclerViewAdapter();
        adapter.setList(appInfoData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}