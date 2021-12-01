package com.codecademy.christiansoe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    private TextView routeLength;
    private TextView routeHikingTime;
    private TextView routeDescription;
    private BingoBoard chosenBingoBoard;
    private int mapId;
    private int bingoBoardId;

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

                   routeLength = findViewById(R.id.routeLength);
                   routeLength.setText(String.valueOf(route.get(0).getLength()));

                    routeHikingTime = findViewById(R.id.routeHikingTime);
                    routeHikingTime.setText(String.valueOf(route.get(0).getHikingTime()));

                    routeDescription = findViewById(R.id.routeDescription);
                    routeDescription.setText(route.get(0).getDescription());

                    bingoBoardId = route.get(0).getBingoBoardId();

                    mapId = route.get(0).getMapId();

                    bingoBoardCall();



                }
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {

            }
        });

    }

    public void bingoBoardCall(){

        Call<BingoBoard> bingoBoardCall = retrofitInitializer.getBingoBoard(bingoBoardId);

        bingoBoardCall.enqueue(new Callback<BingoBoard>() {
            @Override
            public void onResponse(Call<BingoBoard> call, Response<BingoBoard> response) {

                if (!response.isSuccessful()) {
                    return;
                }
                BingoBoard bingoBoard = response.body();

                chosenBingoBoard = bingoBoard;
            }

            @Override
            public void onFailure(Call<BingoBoard> call, Throwable t) {

            }
        });

    }

    public void showMaps(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("mapId", mapId);
        startActivity(intent);
    }

    public void selectRoute(View view){

        Intent intent = new Intent(this, Game.class);
        intent.putExtra("chosenBingoBoard", chosenBingoBoard);
        startActivity(intent);
    }
}