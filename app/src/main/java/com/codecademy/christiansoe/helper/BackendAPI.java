package com.codecademy.christiansoe.helper;

import com.codecademy.christiansoe.model.BingoBoard;
import com.codecademy.christiansoe.model.Field;
import com.codecademy.christiansoe.model.Map;
import com.codecademy.christiansoe.model.Route;
import com.codecademy.christiansoe.model.Theme;
import com.codecademy.christiansoe.model.UserBingoBoard;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

//This is used by Retrofit to make the call to the API
public interface BackendAPI {

    //Defines if it is a GET/POST method, and the endpoint to use (the rest of the url is specified in RetrofitInitializer)
    @GET("userfields")
    //adds a query to the url, in this case https://christiansoeapi.azurewebsites.net/api/userFields?userbingoboardId=4
    Call<List<Field>> getFields(@Query("userbingoboardid") int userBingoBoardId);

    @GET("bingoboards")
    Call<List<BingoBoard>> getBingoBoards();

    @GET("maps/{mapId}")
    Call<Map> getMaps(@Path(value = "mapId") int mapId);

    @GET("userbingoboard")
    Call<BingoBoard> getUserBingoBoards(@Query("bingoboardid") int bingoBoardId, @Query("userid") String userId);



    @POST("userbingoboard")
    Call<UserBingoBoard> createUserBingoBoard(@Body UserBingoBoard userBingoBoard);


    @PUT("userfields/{fieldId}")
    Call<Field> updateField(@Path(value = "fieldId") int fieldId, @Body Field field);


    @GET("themes")
    Call<List<Theme>> getThemes();


    @GET("routes")
    Call<List<Route>> getRoutes(@Query("themeid") int themeId);

    @GET("routes")
    Call<List<Route>> getRoute(@Query("name") String routeName);

}
