package com.ravemaster.recipeapp.api.getrecipedetails.interfaces;

import com.ravemaster.recipeapp.api.getrecipedetails.models.RecipeDetailApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface GetRecipeDetails {
    @GET("recipes/get-more-info")
    Call<RecipeDetailApiResponse> getDetails(
            @Query("id") int id,
            @Header("x-rapidapi-key") String apiKey,
            @Header("x-rapidapi-host") String apiHost
    );
}
