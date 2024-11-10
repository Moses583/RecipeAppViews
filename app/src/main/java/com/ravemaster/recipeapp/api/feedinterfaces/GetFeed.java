package com.ravemaster.recipeapp.api.feedinterfaces;


import com.ravemaster.recipeapp.api.models.FeedsApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface GetFeed {

    @GET("feeds/list")
    Call<FeedsApiResponse> getFeedList(
            @Query("size") int size,
            @Query("timezone") String timezone ,
            @Query("vegetarian") boolean vegetarian,
            @Query("from") int from,
            @Header("x-rapidapi-key") String apiKey,
            @Header("x-rapidapi-host") String apiHost
    );

}
