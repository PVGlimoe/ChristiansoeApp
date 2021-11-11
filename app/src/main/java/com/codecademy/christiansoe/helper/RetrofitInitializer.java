package com.codecademy.christiansoe.helper;

import com.codecademy.christiansoe.model.BingoBoard;
import com.codecademy.christiansoe.model.Field;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Builds the call that we use to connect to the API
public class RetrofitInitializer {


    public Call<List<Field>> getFields(int id){
        //This is where we specify the url to the API (minus the endpoint which is chosen at BackendAPI)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://christiansoeapi.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //This gives retrofit knowledge of the endpoints/methods of the calls.
        BackendAPI backendAPI = retrofit.create(BackendAPI.class);

        //Here we select the method from BackendAPI to finalize our call.
        return backendAPI.getFields(id);
    }

    public Call<List<BingoBoard>> getBingoBoards(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://christiansoeapi.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BackendAPI backendAPI = retrofit.create(BackendAPI.class);

        return backendAPI.getBingoBoards();
    }


}
