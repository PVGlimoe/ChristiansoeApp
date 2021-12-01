package com.codecademy.christiansoe.helper;

import com.codecademy.christiansoe.model.BingoBoard;
import com.codecademy.christiansoe.model.Field;
import com.codecademy.christiansoe.model.Map;
import com.codecademy.christiansoe.model.Route;
import com.codecademy.christiansoe.model.Theme;
import com.codecademy.christiansoe.model.UserBingoBoard;

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

    public Call<BingoBoard> getBingoBoard(int bingoBoardId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://christiansoeapi.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BackendAPI backendAPI = retrofit.create(BackendAPI.class);

        return backendAPI.getBingoBoard(bingoBoardId);
    }

    public Call<Map> getMaps(int mapId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://christiansoeapi.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BackendAPI backendAPI = retrofit.create(BackendAPI.class);

        return backendAPI.getMaps(mapId);
    }

    public Call<UserBingoBoard> getUserBingoBoards(int bingoBoardId, String userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://christiansoeapi.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BackendAPI backendAPI = retrofit.create(BackendAPI.class);

        return backendAPI.getUserBingoBoards(bingoBoardId, userId);
    }

    public Call<UserBingoBoard> createUserBingoBoard(UserBingoBoard userBingoBoard){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://christiansoeapi.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BackendAPI backendAPI = retrofit.create(BackendAPI.class);
        //OBS Change to dynamic mapId
        return backendAPI.createUserBingoBoard(userBingoBoard);
    }

    public Call<Field> updateField(Field field){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://christiansoeapi.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BackendAPI backendAPI = retrofit.create(BackendAPI.class);
        return backendAPI.updateField(field.getId(), field);
    }


    public Call<List<Theme>> getThemes() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://christiansoeapi.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BackendAPI backendAPI = retrofit.create(BackendAPI.class);

        return backendAPI.getThemes();
    }


    public Call<List<Route>> getRoutes(int id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://christiansoeapi.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BackendAPI backendAPI = retrofit.create(BackendAPI.class);

        return backendAPI.getRoutes(id);
    }

    public Call<List<Route>> getRoute(String routeName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://christiansoeapi.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BackendAPI backendAPI = retrofit.create(BackendAPI.class);
        return backendAPI.getRoute(routeName);
    }

    public Call<UserBingoBoard> resetBingoBoard(UserBingoBoard userBingoBoard) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://christiansoeapi.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BackendAPI backendAPI = retrofit.create(BackendAPI.class);

        return backendAPI.resetBingoBoard(userBingoBoard.getId() ,userBingoBoard);
    }






}
