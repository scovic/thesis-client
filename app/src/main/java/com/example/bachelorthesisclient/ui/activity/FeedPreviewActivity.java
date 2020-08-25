package com.example.bachelorthesisclient.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.model.Feed;
import com.example.bachelorthesisclient.ui.viewmodel.FeedPreviewViewModel;
import com.example.bachelorthesisclient.wrapper.PicassoWrapper;

import java.text.SimpleDateFormat;

public class FeedPreviewActivity extends AppCompatActivity {
    private FeedPreviewViewModel mViewModel;

    TextView tvUserName;
    TextView tvDate;
    TextView tvContent;
    LinearLayout llImageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_preview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setViews();
        setViewModel();
        handleExtrasFromIntent();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent i = new Intent(FeedPreviewActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViews() {
        tvUserName = findViewById(R.id.text_name);
        tvDate = findViewById(R.id.text_date);
        tvContent = findViewById(R.id.text_post);
        llImageContainer = findViewById(R.id.image_container);
    }

    private void handleExtrasFromIntent() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int postId = extras.getInt("postId", 0);
            mViewModel.loadFeedDetails(postId);
        }
    }

    private void setViewModel() {
        mViewModel = new ViewModelProvider(this).get(FeedPreviewViewModel.class);
        observeViewModel();
    }

    private void observeViewModel() {
        mViewModel.getFeed().observe(this, this.handleFeedChange());
    }

    private Observer<Feed> handleFeedChange() {
        return new Observer<Feed>() {
            @Override
            public void onChanged(Feed feed) {
                if (feed != null) {
                    displayData(feed);
                }
            }
        };
    }

    private void displayData(Feed feed) {
        tvUserName.setText(feed.getAuthor().getFullName());
        tvDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(feed.getPost().getUpdatedAt()));
        tvContent.setText(feed.getPost().getText());

        llImageContainer.removeAllViews();

        for (String attachment : feed.getPost().getAttachmentNames()) {
            ImageView iv = new ImageView(FeedPreviewActivity.this);
            llImageContainer.addView(iv);

            PicassoWrapper.getInstance().loadImage(this.makeImageUrl(feed.getPost().getId(), attachment), iv);
        }
    }

    private String makeImageUrl(int postId, String imageName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/").append(postId);
        stringBuilder.append("/").append(imageName);

        return stringBuilder.toString();
    }

}
