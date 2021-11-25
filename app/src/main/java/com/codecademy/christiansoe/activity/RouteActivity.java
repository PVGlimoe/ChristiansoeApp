package com.codecademy.christiansoe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.codecademy.christiansoe.R;
import com.codecademy.christiansoe.helper.RetrofitInitializer;
import com.codecademy.christiansoe.model.BingoBoard;
import com.codecademy.christiansoe.model.Field;
import com.codecademy.christiansoe.model.Route;
import com.codecademy.christiansoe.model.UserBingoBoard;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteActivity extends AppCompatActivity {
    private RetrofitInitializer retrofitInitializer = new RetrofitInitializer();
    private TextView routeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        Intent intent = getIntent();
        String routeName  = intent.getStringExtra("clickedRoute");


        Call<List<Route>> call = retrofitInitializer.getRoute(routeName);

        call.enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {

                if(response.isSuccessful()){

                    List<Route> route = response.body();
                   routeTextView = findViewById(R.id.routeName);
                   routeTextView.setText(route.get(0).getName());

                }
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {

            }
        });

    }
}