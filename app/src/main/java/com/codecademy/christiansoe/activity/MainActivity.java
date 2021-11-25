package com.codecademy.christiansoe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.codecademy.christiansoe.GpsTracker;
import com.codecademy.christiansoe.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGame (View view)
    {
        Intent intent = new Intent(this, ThemeActivity.class);
        startActivity(intent);
    }

    public void showMaps(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void showGps(View view){
        Intent intent = new Intent(this, GpsTracker.class);
        startActivity(intent);
    }



}