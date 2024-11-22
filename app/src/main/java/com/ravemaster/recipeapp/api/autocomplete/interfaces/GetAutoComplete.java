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
            "x-rapidapi-key: cee03e6aabmsh2fa043ef9ad519ap17e59bjsn695eb9e6b3fb",
            "x-rapidapi-host: tasty.p.rapidapi.com"
    })
    Call<AutoCompleteApiResponse> getAutoComplete(
            @Query("prefix") String prefix
    );
}
