package com.example.bachelorthesisclient.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.config.Values;
import com.example.bachelorthesisclient.model.User;
import com.example.bachelorthesisclient.ui.viewmodel.SettingsViewModel;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

public class SettingsActivity extends AppCompatActivity {
    private SettingsViewModel mViewModel;

    private Switch switchReceiveNotifs;
    private Slider sliderDistance;
    private TextView textViewSliderLabel;
    private TextInputLayout etFirstName;
    private TextInputLayout etLastName;
    private TextInputLayout etEmail;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setActionBar();
        setViewModel();
        setViews();
        setListeners();
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

    private void setActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatTextView tv = findViewById(R.id.tv_action_bar_title);
        tv.setText(R.string.toolbar_settings_title);
    }

    private void setViews() {
        this.switchReceiveNotifs = findViewById(R.id.switch_receive_info);
        this.sliderDistance = findViewById(R.id.slider_distance);
        this.textViewSliderLabel = findViewById(R.id.label_slider_settings);
        this.etFirstName = findViewById(R.id.et_settings_first_name);
        this.etLastName = findViewById(R.id.et_settings_last_name);
        this.etEmail = findViewById(R.id.et_settings_email);
        this.btnUpdate = findViewById(R.id.btn_settings_update);

        this.sliderDistance.setValueFrom(Values.MIN_DISTANCE_INFO_NOTIFICATIONS);
        this.sliderDistance.setValueTo(Values.MAX_DISTANCE_INFO_NOTIFICATIONS);

        this.switchReceiveNotifs.setChecked(
                mViewModel.getReceiveInfoNotifications().getValue()
        );

        this.sliderDistance.setValue(
                mViewModel.getCurrentDistance().getValue()
        );

        this.setSliderLabel(mViewModel.getCurrentDistance().getValue());
    }

    private void setViewModel() {
        this.mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        observeViewModel();
    }

    private void observeViewModel() {
        this.mViewModel.getUserDetails().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    etFirstName.getEditText().setText(user.getFirstName());
                    etLastName.getEditText().setText(user.getLastName());
                    etEmail.getEditText().setText(user.getEmail());
                }
            }
        });

        this.mViewModel.getCanUpdate().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean canUpdate) {
                btnUpdate.setEnabled(canUpdate);
            }
        });
    }

    private void setListeners() {
        this.switchReceiveNotifs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mViewModel.setReceiveInfoNotifications(isChecked);
            }
        });

        this.sliderDistance.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                mViewModel.setCurrentDistance(slider.getValue());
                setSliderLabel(slider.getValue());
            }
        });

        this.sliderDistance.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                setSliderLabel(value);
            }
        });

        this.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.updateUserDetails(
                        etFirstName.getEditText().getText().toString(),
                        etLastName.getEditText().getText().toString()
                );
            }
        });

        this.etFirstName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mViewModel.getUserDetails().getValue().getFirstName().equals(s.toString()) || s.toString().equals("")) {
                    return;
                }

                mViewModel.setCanUpdate(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.etLastName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mViewModel.getUserDetails().getValue().getLastName().equals(s.toString()) || s.toString().equals("")) {
                    return;
                }

                mViewModel.setCanUpdate(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setSliderLabel(float value) {
        DecimalFormat df = new DecimalFormat("0");
        this.textViewSliderLabel.setText(
                String.format(
                        "%s: %s meters",
                        getString(R.string.label_settings_slider),
                        df.format(value)
                )
        );
    }
}
