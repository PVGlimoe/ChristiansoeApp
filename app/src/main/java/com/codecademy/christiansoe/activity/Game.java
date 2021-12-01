package com.codecademy.christiansoe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codecademy.christiansoe.helper.DownloadImageTask;
import com.codecademy.christiansoe.helper.RetrofitInitializer;
import com.codecademy.christiansoe.model.Field;
import com.codecademy.christiansoe.R;
import com.codecademy.christiansoe.model.BingoBoard;
import com.codecademy.christiansoe.model.UserBingoBoard;

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
    private String userId;

    //Changed onCrate to onResume so that the page reload every time with the updated field information'.
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_game);

        //Gets the unique phone/user id
        userId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);


        //Gets the bingoBoard object from the chosen theme on previous activity.
        Intent intent = getIntent();
        bingoBoard = (BingoBoard) intent.getSerializableExtra("chosenBingoBoard");

        //Fill out information's from the BingoBoard to the activity display.
        bingoBoardTextView = findViewById(R.id.bingoBoardTextView);
        String name = bingoBoard.getName();
        bingoBoardTextView.setText(name);

        Call<BingoBoard> userBingoBoardCall = retrofitInitializer.getUserBingoBoards(bingoBoard.getId(), userId);

        userBingoBoardCall.enqueue(new Callback<BingoBoard>() {
            @Override
            public void onResponse(Call<BingoBoard> call, Response<BingoBoard> response) {

                if(response.isSuccessful()){
                    int bingoBoardId = response.body().getId();
                    getUserFields(bingoBoardId);

                } else if(response.code() == 404){
                    UserBingoBoard userBingoBoard = new UserBingoBoard(false, bingoBoard.getId(), userId);
                    createUserFields(userBingoBoard);
                }
            }

            @Override
            public void onFailure(Call<BingoBoard> call, Throwable t) {

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
        clickedField.setUserId(userId);

        //Sends the user to the next activity with the field object that was clicked on.
        Intent intent = new Intent(this, FieldInformation.class);
        intent.putExtra("clickedField", clickedField);
        startActivity(intent);
    }

    public void getUserFields(int bingoBoardId){

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        //Uses the helper class retrofitInitializer, to make a api call, which returns the fields from the bingoBoard.
        Call<List<Field>> call = retrofitInitializer.getFields(bingoBoardId);

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

                    if(field.isMarked()){
                        imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
                    }



                    counter++;
                }

            }

            @Override
            public void onFailure(Call<List<Field>> call, Throwable t) {

            }
        });

    }

    public void createUserFields(UserBingoBoard userBingoBoard){
       Call<UserBingoBoard> call = retrofitInitializer.createUserBingoBoard(userBingoBoard);

       call.enqueue(new Callback<UserBingoBoard>() {
           @Override
           public void onResponse(Call<UserBingoBoard> call, Response<UserBingoBoard> response) {
               getUserFields(response.body().getId());
           }

           @Override
           public void onFailure(Call<UserBingoBoard> call, Throwable t) {

           }
       });
    }

    public void showDistanceToFerry(View view){

    }
}