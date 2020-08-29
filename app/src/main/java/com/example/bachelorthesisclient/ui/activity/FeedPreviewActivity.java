package com.example.bachelorthesisclient.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
    TextView tvFeedNotFound;
    LinearLayout llImageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_preview);
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
        tvFeedNotFound = findViewById(R.id.tv_feed_not_found);
        llImageContainer = findViewById(R.id.image_container);
    }

    private void handleExtrasFromIntent() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int postId = extras.getInt("postId", 0);
            if (postId > 0) {
                mViewModel.loadFeedDetails(postId);
            }
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
                    tvFeedNotFound.setVisibility(View.GONE);
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
            iv.setOnClickListener(handleOnClickImageView(feed.getPost().getId(), attachment));
            iv.setPadding(4, 0, 4, 0);
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

    private View.OnClickListener handleOnClickImageView(final int postId, final String imageName) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FeedPreviewActivity.this, ImagePreviewActivity.class);
                i.putExtra("postId", postId);
                i.putExtra("imageName", imageName);
                startActivity(i);
            }
        };
    }

}
