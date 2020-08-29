package com.example.bachelorthesisclient.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.wrapper.PicassoWrapper;

public class ImagePreviewActivity extends AppCompatActivity {
    ImageView ivEnlargedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ivEnlargedImage = findViewById(R.id.iv_enlarged);
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

    private void handleExtrasFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int postId = extras.getInt("postId");

            if (postId > 0) {
                String imageName = extras.getString("imageName");
                PicassoWrapper.getInstance().loadImageWithoutResize(makeImageUrl(postId, imageName), ivEnlargedImage);
            } else {
                byte[] bitmapBytes = extras.getByteArray("bitmapByteArray");

                if (bitmapBytes != null) {
                    ivEnlargedImage.setImageBitmap(BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length));
                }
            }
        }
    }

    private String makeImageUrl(int postId, String imageName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/").append(postId);
        stringBuilder.append("/").append(imageName);

        return stringBuilder.toString();
    }


}
