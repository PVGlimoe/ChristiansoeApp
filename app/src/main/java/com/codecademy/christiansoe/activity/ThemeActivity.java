package com.codecademy.christiansoe.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ExpandableListView;

import com.codecademy.christiansoe.helper.BackendAPI;
import com.codecademy.christiansoe.R;
import com.codecademy.christiansoe.activity.Game;
import com.codecademy.christiansoe.helper.MainAdapter;
import com.codecademy.christiansoe.helper.RetrofitInitializer;
import com.codecademy.christiansoe.model.BingoBoard;
//import com.google.android.gms.location.LocationServices;
import com.codecademy.christiansoe.model.Route;
import com.codecademy.christiansoe.model.Theme;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ThemeActivity extends AppCompatActivity {

    private BingoBoard chosenBingoBoard;
    private RetrofitInitializer retrofitInitializer = new RetrofitInitializer();


    MainAdapter adapter;
    ExpandableListView expandableListView;
    HashMap<String, ArrayList<String>> listChild = new HashMap<>();
    List<String> listGroup = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        collapsibleTheme();

        Call<List<BingoBoard>> call = retrofitInitializer.getBingoBoards();

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

    public void collapsibleTheme() {

        Call<List<Theme>> call = retrofitInitializer.getThemes();

        call.enqueue(new Callback<List<Theme>>() {
            @Override
            public void onResponse(Call<List<Theme>> call, Response<List<Theme>> response) {

                if (!response.isSuccessful()) {
                    return;
                }

                List<Theme> themeList = response.body();


                for (int i = 0; i < themeList.size(); i++) {

                    Call<List<Route>> call2 = retrofitInitializer.getRoutes(themeList.get(i).getId());
                    listGroup.add(themeList.get(i).getName());

                    int finalI = i;
                    call2.enqueue(new Callback<List<Route>>() {


                        @Override
                        public void onResponse(Call<List<Route>> call2, Response<List<Route>> response) {

                            if (!response.isSuccessful()) {
                                return;
                            }

                            List<Route> routeList = response.body();

                            //Assign variable
                            expandableListView = findViewById(R.id.exp_list_view);

                            //Use for loop


                            //Initialize arraylist
                            ArrayList<String> arrayList = new ArrayList<>();

                            //Use for loop
                            for (int c = 0; c < routeList.size(); c++) {
                                //Add values to arraylist
                                arrayList.add(routeList.get(c).getName());

                            }

                            //Put values in child list
                            listChild.put(listGroup.get(finalI), arrayList);
                            //Initialize adapter
                            adapter = new MainAdapter(listGroup, listChild);
                            //Set adapter
                            expandableListView.setAdapter(adapter);

                        }

                        @Override
                        public void onFailure(Call<List<Route>> call2, Throwable t) {

                        }

                    });

                }

            }

            @Override
            public void onFailure(Call<List<Theme>> call, Throwable t) {

            }
        });

    }

}