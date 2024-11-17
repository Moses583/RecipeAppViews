package com.ravemaster.recipeapp.api.getsimilarrecipes.interfaces;


import com.ravemaster.recipeapp.api.getsimilarrecipes.models.SimilarRecipeApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GetSimilarRecipes {
    @GET("recipes/list-similarities")
    Call<SimilarRecipeApiResponse> getSimilarRecipes(
            @Query("recipe_id") int id,
            @Header("x-rapidapi-key") String apiKey,
            @Header("x-rapidapi-host") String apiHost
    );
}
