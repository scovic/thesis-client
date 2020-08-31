package com.example.bachelorthesisclient.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.model.Performer;
import com.example.bachelorthesisclient.ui.adapter.StagePerformersAdapter;
import com.example.bachelorthesisclient.ui.viewmodel.StagePerformersViewModel;

import java.util.ArrayList;
import java.util.List;

public class StagePerformersActivity extends AppCompatActivity {
    private StagePerformersViewModel mViewModel;
    private StagePerformersAdapter adapter;

    private ListView listView;
    private TextView tvStageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_performers);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setViews();
        setViewModel();
        handleDataFromIntent();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViews() {
        listView = findViewById(R.id.performer_list);
        tvStageName = findViewById(R.id.tv_stage_name);

        adapter = new StagePerformersAdapter(StagePerformersActivity.this, new ArrayList<Performer>());
        listView.setAdapter(adapter);
    }

    private void setViewModel() {
        mViewModel = new ViewModelProvider(this).get(StagePerformersViewModel.class);
        observeViewModel();
    }

    private void observeViewModel() {
        mViewModel.getPerformerMutableLiveData().observe(this, new Observer<List<Performer>>() {
            @Override
            public void onChanged(List<Performer> performers) {
                adapter.setPerformers(performers);
            }
        });
    }

    private void handleDataFromIntent() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            int stageId = bundle.getInt("stageId", 0);
            String stageName = bundle.getString("stageName");

            if (stageId > 0) {
                mViewModel.getStagePerformers(stageId);
                tvStageName.setText(stageName);
            }
        }
    }
}
