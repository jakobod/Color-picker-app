package com.example.myfirstapp;

import android.graphics.Color;
import android.os.Bundle;

import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;

import com.larswerkman.holocolorpicker.SVBar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener {
    WiFiSocketTask wifiTask = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SVBar svBar = findViewById(R.id.svbar);
        com.larswerkman.holocolorpicker.ColorPicker picker = findViewById(R.id.picker);

        picker.addSVBar(svBar);
        picker.setShowOldCenterColor(false);
        picker.setOnColorChangedListener(this);
    }

    @Override
    public void onColorChanged(int color) {
        String colorMessage = "";

        colorMessage += (String.format(Locale.GERMAN, "%03d", Color.red(color)));
        colorMessage += (String.format(Locale.GERMAN, "%03d", Color.green(color)));
        colorMessage += (String.format(Locale.GERMAN, "%03d", Color.blue(color)));

        wifiTask.queueMessage(colorMessage);
    }

    @Override
    protected void onResume(){
        super.onResume();

        wifiTask = new WiFiSocketTask();
        wifiTask.execute();
    }

    @Override
    protected void onStop(){
        super.onStop();

        if (wifiTask == null) return;

        wifiTask.requestDisconnect();
    }
}