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
            "x-rapidapi-key: 190ecbd632mshf9627c5d4b4afc0p13af00jsnaa0e7ec1b323",
            "x-rapidapi-host: tasty.p.rapidapi.com"
    })
    Call<AutoCompleteApiResponse> getAutoComplete(
            @Query("prefix") String prefix
    );
}
