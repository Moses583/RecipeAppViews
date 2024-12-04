package com.ravemaster.recipeapp.api.autocomplete.interfaces;


import com.ravemaster.recipeapp.api.autocomplete.models.AutoCompleteApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface GetAutoComplete {
    @GET("recipes/auto-complete")
    @Headers({
            "x-rapidapi-key: ed4b9641acmshe6e3944254ccdf8p12c249jsn32c2d44e1c9b",
            "x-rapidapi-host: tasty.p.rapidapi.com"
    })
    Call<AutoCompleteApiResponse> getAutoComplete(
            @Query("prefix") String prefix
    );
}
