package com.example.bachelorthesisclient.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.ui.viewmodel.CreateFeedViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CreateFeedActivity extends AppCompatActivity {
    private CreateFeedViewModel mViewModel;

    private Button uploadImage;
    private Button publishPost;
    private TextInputEditText textEditor;
    private LinearLayout imageContainer;
    private CheckBox cbxSendNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feed);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatTextView tv = findViewById(R.id.tv_action_bar_title);
        tv.setText("Create New Feed");

        setViews();
        setAndObserveViewModel();
        recreateImageViews();
        setOnClickListeners();
        setOnTextChangeListener();
        setOnCheckedListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                this.mViewModel.addImage((Bitmap) data.getExtras().get("data"));
            } else if (requestCode == 1) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);

                    this.mViewModel.addImage(BitmapFactory.decodeStream(imageStream));
                } catch (IOException e) {
                    Log.i("TAG", "Couldn't get image " + e);
                }
            }
        }
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
        uploadImage = findViewById(R.id.btn_upload_images);
        publishPost = findViewById(R.id.btn_publish_post);
        imageContainer = findViewById(R.id.ly_image_container);
        textEditor = findViewById(R.id.text_input_content);
        cbxSendNotification = findViewById(R.id.checkbox_send_info_notif);
    }

    private void setAndObserveViewModel() {
        mViewModel = new ViewModelProvider(this).get(CreateFeedViewModel.class);
        observeViewModel();
    }


    private void setOnClickListeners() {
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        publishPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.createFeed();
            }
        });
    }

    private void setOnTextChangeListener() {
        textEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.setContent(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setOnCheckedListener() {
        cbxSendNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mViewModel.setSendNotification(isChecked);
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Take Photo")) {
                    Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePhoto, 0);
                } else if (options[which].equals("Choose from Gallery")) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                    startActivityForResult(photoPickerIntent, 1);
                } else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }


    private void observeViewModel() {
        mViewModel.getImages().observe(this, new Observer<List<Bitmap>>() {
            @Override
            public void onChanged(List<Bitmap> bitmaps) {
                recreateImageViews();
            }
        });

        mViewModel.getSuccessfulCreated().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean successfulCreated) {
                if (successfulCreated) {
                    Toast.makeText(CreateFeedActivity.this, "Feed Created", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        mViewModel.getSendNotification().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean sendNotification) {
                cbxSendNotification.setChecked(sendNotification);
            }
        });

        mViewModel.getEmptyContentField().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean emptyContent) {
                if (emptyContent) {
                    Toast.makeText(CreateFeedActivity.this, "Content field must not be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void recreateImageViews() {
        imageContainer.removeAllViews();

        for (Bitmap bitmap : mViewModel.getImages().getValue()) {
            this.addPhoto(bitmap);
        }
    }

    private void addPhoto(Bitmap photo) {
        ImageView iv = new ImageView(this);

        imageContainer.addView(iv);
        iv.getLayoutParams().height = 400;
        iv.getLayoutParams().width = 300;

        iv.setImageBitmap(photo);
    }
}
