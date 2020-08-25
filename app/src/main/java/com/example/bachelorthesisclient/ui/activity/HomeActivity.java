package com.example.bachelorthesisclient.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.model.Feed;
import com.example.bachelorthesisclient.service.NotificationsMessagingService;
import com.example.bachelorthesisclient.ui.adapter.FeedAdapter;
import com.example.bachelorthesisclient.ui.viewmodel.HomeViewModel;
import com.example.bachelorthesisclient.wrapper.FusedLocationProviderWrapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private HomeViewModel mViewModel;
    private FeedAdapter feedAdapter;

    private ListView feedList;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FusedLocationProviderWrapper.getInstance().askForLocationPermissions(this);

        this.progressBar = findViewById(R.id.progressBar1);
        this.floatingActionButton = findViewById(R.id.fab);
        this.feedList = findViewById(R.id.feed_list);
        this.swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        this.setViewModel();
        this.setListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_show_settings: {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            case R.id.menu_item_warning_notification: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to send warning notification")
                        .setPositiveButton("Yes", handleSendWarningDialog())
                        .setNegativeButton("No", handleSendWarningDialog())
                        .show();

                return true;
            }
            case R.id.menu_item_show_map: {
                Intent i = new Intent(this, MapActivity.class);
                startActivity(i);

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mViewModel != null) {
            mViewModel.getData();
        }
    }

    private DialogInterface.OnClickListener handleSendWarningDialog() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: {
                        sendWarningNotification();
                        showToastMessage("Warning sent successfully");

                        Intent i = new Intent(HomeActivity.this, MapActivity.class);
                        i.putExtra("tag", NotificationsMessagingService.WARNING_TAG);
                        startActivity(i);

                        break;
                    }
                    case DialogInterface.BUTTON_NEGATIVE: {
                        break;
                    }
                }
            }
        };
    }

    private void showToastMessage(String message) {
        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void sendWarningNotification() {
        mViewModel.sendWarningNotification();
    }

    private void setViewModel() {
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        this.observeViewModel();
    }

    private void observeViewModel() {
        mViewModel.getFeeds().observe(this, new Observer<List<Feed>>() {
            @Override
            public void onChanged(List<Feed> feeds) {
                feedAdapter = new FeedAdapter(HomeActivity.this, feeds);
                feedList.setAdapter(feedAdapter);
            }
        });

        mViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {
                if (loading) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setListeners() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, CreateFeedActivity.class);
                startActivity(i);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
