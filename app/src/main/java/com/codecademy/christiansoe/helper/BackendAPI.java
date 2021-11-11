package com.codecademy.christiansoe.helper;

import com.codecademy.christiansoe.model.BingoBoard;
import com.codecademy.christiansoe.model.Field;
import com.codecademy.christiansoe.model.Map;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//This is used by Retrofit to make the call to the API
public interface BackendAPI {

    //Defines if it is a GET/POST method, and the endpoint to use (the rest of the url is specified in RetrofitInitializer)
    @GET("fields")
    //adds a query to the url, in this case https://.../fields?bingoboardid='bingoBoardId'
    Call<List<Field>> getFields(@Query("bingoboardid") int bingoBoardId);

    @GET("bingoboards")
    Call<List<BingoBoard>> getBingoBoards();

    @GET("maps/{mapId}")
    Call<Map> getMaps(@Path(value = "mapId") int mapId);

}
