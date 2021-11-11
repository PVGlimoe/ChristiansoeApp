package com.codecademy.christiansoe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codecademy.christiansoe.helper.DownloadImageTask;
import com.codecademy.christiansoe.helper.RetrofitInitializer;
import com.codecademy.christiansoe.model.Field;
import com.codecademy.christiansoe.R;
import com.codecademy.christiansoe.model.BingoBoard;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Game extends AppCompatActivity {

    private BingoBoard bingoBoard;
    private TextView bingoBoardTextView;
    private List<Field> fieldList = new ArrayList<>();
    private RetrofitInitializer retrofitInitializer = new RetrofitInitializer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Gets the bingoBoard object from the chosen theme on previous activity.
        Intent intent = getIntent();
        bingoBoard = (BingoBoard) intent.getSerializableExtra("chosenBingoBoard");

        //Fill out information's from the BingoBoard to the activity display.
        bingoBoardTextView = findViewById(R.id.bingoBoardTextView);
        String name = bingoBoard.getName();
        bingoBoardTextView.setText(name);

        //Uses the helper class retrofitInitializer, to make a api call, which returns the fields from the bingoBoard.
        Call<List<Field>> call = retrofitInitializer.getFields(bingoBoard.getId());

        //Makes the actually call, and checks if the call is valid.
        call.enqueue(new Callback<List<Field>>() {
            @Override
            public void onResponse(Call<List<Field>> call, Response<List<Field>> response) {

                if (!response.isSuccessful()) {
                    return;
                }
                //Looping over the fields, and uses the helper class DownloadImageTask to "draw" the pictures.
                fieldList = response.body();

                int counter = 1;
                for(Field field : fieldList){
                    int id = getResources().getIdentifier("imageViewBoard" + counter, "id", getPackageName());
                    ImageView imageView = findViewById(id);
                    new DownloadImageTask(imageView).execute((field.getPictureUrl()));
                    counter++;
                }

            }

            @Override
            public void onFailure(Call<List<Field>> call, Throwable t) {

            }
        });
    }

    public void showFieldInfo(View view){

        int imageIndex = 0;

        int clickedId = view.getId();

        //Looks for the position of the field that is clicked on
        for(int i = 1; i < 10; i++){
            int imageId = getResources().getIdentifier("imageViewBoard"+i, "id", getPackageName());

            if(clickedId == imageId){
                imageIndex = i - 1;
            }
        }
        //uses the position to get the chosen field.
        Field clickedField = fieldList.get(imageIndex);

        //Sends the user to the next activity with the field object that was clicked on.
        Intent intent = new Intent(this, FieldInformation.class);
        intent.putExtra("clickedField", clickedField);
        startActivity(intent);
    }
}