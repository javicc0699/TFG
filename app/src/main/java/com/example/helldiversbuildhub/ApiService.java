package com.example.helldiversbuildhub;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/v1/war/campaign")
    Call<List<Planet>> getPlanets();

    @GET("api/v1/war/major-orders")
    Call<List<MajorOrder>> getMajorOrders();
}
