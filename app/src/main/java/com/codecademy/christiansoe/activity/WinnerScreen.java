package com.codecademy.christiansoe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.codecademy.christiansoe.R;
import com.codecademy.christiansoe.model.BingoBoard;

public class WinnerScreen extends AppCompatActivity {

    TextView completedRouteName, winnerMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_screen);

        Intent intent = getIntent();
        String routeName = intent.getStringExtra("routeName");

        completedRouteName = findViewById(R.id.completedRouteName);
        completedRouteName.setText(routeName);

        winnerMessage = findViewById(R.id.winnerMessage);
        winnerMessage.setText("Tak for dit besøg, vi håber at du har hygget dig.\n\nHvis du har mere tid så kan du prøve en af de andre ruter");



    }
}