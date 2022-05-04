package com.example.textgeneratorfinalversion;

import android.annotation.SuppressLint;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.RangeSlider;

public class SettingsActivity extends AppCompatActivity {
    public static RangeSlider temperatureSlider, topKSlider;
    public static Float tempVal;
    public static Integer topKVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        temperatureSlider = findViewById(R.id.temperature);
        topKSlider = findViewById(R.id.topk);

        temperatureSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                tempVal = value;
            }
        });

        topKSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                topKVal = Math.round(value);
            }
        });
    }
}
