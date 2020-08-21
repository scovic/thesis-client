package com.example.bachelorthesisclient.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.config.Values;
import com.example.bachelorthesisclient.ui.viewmodel.SettingsViewModel;
import com.google.android.material.slider.Slider;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class SettingsActivity extends AppCompatActivity {
    private SettingsViewModel mViewModel;

    private Switch switchReceiveNotifs;
    private Slider sliderDistance;
    private TextView textViewSliderLabel;

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

        this.sliderDistance.setValueFrom(Values.MIN_DISTANCE);
        this.sliderDistance.setValueTo(Values.MAX_DISTANCE);

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
