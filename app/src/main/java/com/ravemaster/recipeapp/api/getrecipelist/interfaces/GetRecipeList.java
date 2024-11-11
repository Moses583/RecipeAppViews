package com.ravemaster.recipeapp.api.getrecipelist.interfaces;

import com.ravemaster.recipeapp.api.getfeed.models.FeedsApiResponse;
import com.ravemaster.recipeapp.api.getrecipelist.models.RecipeListApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GetRecipeList {
    @GET("recipes/list")
    Call<RecipeListApiResponse> getRecipeList(
            @Query("from") int from,
            @Query("size") int size ,
            @Header("x-rapidapi-key") String apiKey,
            @Header("x-rapidapi-host") String apiHost
    );
}
