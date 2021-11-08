package com.codecademy.christiansoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Game extends AppCompatActivity {

    private BingoBoard bingoBoard;
    private TextView bingoBoardTextView;
    private List<Field> fieldList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        bingoBoard = (BingoBoard) intent.getSerializableExtra("chosenBingoBoard");

        bingoBoardTextView = findViewById(R.id.bingoBoardTextView);
        String name = bingoBoard.getName();
        bingoBoardTextView.setText(name);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://christiansoeapi.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BackendAPI backendAPI = retrofit.create(BackendAPI.class);

        Call<List<Field>> call = backendAPI.getFields(bingoBoard.getId());



        call.enqueue(new Callback<List<Field>>() {
            @Override
            public void onResponse(Call<List<Field>> call, Response<List<Field>> response) {

                if (!response.isSuccessful()) {
                    return;
                }

                List<Field> fields = response.body();
                fieldList = response.body();

                int counter = 1;
                for(Field field : fields){
                    int id = getResources().getIdentifier("imageViewBoard" + counter, "id", getPackageName());
                    ImageView im = findViewById(id);
                    new DownloadImageTask(im).execute((field.getPictureUrl()));
                    counter++;
                }

            }

            @Override
            public void onFailure(Call<List<Field>> call, Throwable t) {

            }
        });
    }
//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }

    public void showFieldInfo(View view){

        int imageIndex = 0;

        int clickedId = view.getId();
        for(int i = 1; i < 10; i++){
            int imageId = getResources().getIdentifier("imageViewBoard"+i, "id", getPackageName());

            if(clickedId == imageId){
                imageIndex = i - 1;
            }
        }

        Field clickedField = fieldList.get(imageIndex);


        Intent intent = new Intent(this, FieldInformation.class);
        intent.putExtra("clickedField", clickedField);
        startActivity(intent);
    }
}