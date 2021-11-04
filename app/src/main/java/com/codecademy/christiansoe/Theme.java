package com.codecademy.christiansoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Theme extends AppCompatActivity {

    private BingoBoard chosenBingoBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://christiansoeapi.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BackendAPI backendAPI = retrofit.create(BackendAPI.class);

        Call<List<BingoBoard>> call = backendAPI.getBingoBoards();

        call.enqueue(new Callback<List<BingoBoard>>() {
            @Override
            public void onResponse(Call<List<BingoBoard>> call, Response<List<BingoBoard>> response) {

                if (!response.isSuccessful()) {
                    return;
                }
                List<BingoBoard> bingoBoards = response.body();

                //TO-DO Få fat i det valgte BingoBoard's id og sæt til chosenBingoBoard.
                chosenBingoBoard = bingoBoards.get(0);
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