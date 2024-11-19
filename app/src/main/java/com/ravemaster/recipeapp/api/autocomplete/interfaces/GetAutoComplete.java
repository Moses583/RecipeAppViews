package com.ravemaster.recipeapp.api.autocomplete.interfaces;


import com.ravemaster.recipeapp.api.autocomplete.models.AutoCompleteApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface GetAutoComplete {
    @GET("recipes/auto-complete")
    @Headers({
            "x-rapidapi-key: ab9a345b1dmsh95573e64e14301dp11f08cjsnbb669399ee87",
            "x-rapidapi-host: tasty.p.rapidapi.com"
    })
    Call<AutoCompleteApiResponse> getAutoComplete(
            @Query("prefix") String prefix
    );
}
