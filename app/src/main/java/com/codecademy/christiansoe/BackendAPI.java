package com.codecademy.christiansoe;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BackendAPI {

    @GET("fields")
    Call<List<Field>> getFields(@Query("bingoboardid") int bingoBordId);

    @GET("bingoboards")
    Call<List<BingoBoard>> getBingoBoards();

}
