package com.codecademy.christiansoe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.codecademy.christiansoe.helper.BackendAPI;
import com.codecademy.christiansoe.R;
import com.codecademy.christiansoe.activity.Game;
import com.codecademy.christiansoe.helper.RetrofitInitializer;
import com.codecademy.christiansoe.model.BingoBoard;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Theme extends AppCompatActivity {

    private BingoBoard chosenBingoBoard;
    private RetrofitInitializer retrofitInitializer = new RetrofitInitializer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        Call<List<BingoBoard>> call = retrofitInitializer.getBingoBoards();

        call.enqueue(new Callback<List<BingoBoard>>() {
            @Override
            public void onResponse(Call<List<BingoBoard>> call, Response<List<BingoBoard>> response) {

                if (!response.isSuccessful()) {
                    return;
                }
                List<BingoBoard> bingoBoards = response.body();

                //TO-DO Få fat i det valgte BingoBoard's id og sæt til chosenBingoBoard.
                chosenBingoBoard = bingoBoards.get(3);
            }

            @Override
            public void onFailure(Call<List<BingoBoard>> call, Throwable t) {

            }
        });
    }

    public void openGame (View view)
    {
        Intent intent = new Intent(this, Game.class);
        intent.putExtra("chosenBingoBoard", chosenBingoBoard);
        startActivity(intent);
    }

}