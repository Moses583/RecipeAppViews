package com.ravemaster.recipeapp.api.autocomplete.interfaces;


import com.ravemaster.recipeapp.api.autocomplete.models.AutoCompleteApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface GetAutoComplete {
    @GET("recipes/auto-complete")
    @Headers({
            "x-rapidapi-key: 7e3d2f10bdmsh70e6fefa71835adp16c240jsnb41f1b9c1073",
            "x-rapidapi-host: tasty.p.rapidapi.com"
    })
    Call<AutoCompleteApiResponse> getAutoComplete(
            @Query("prefix") String prefix
    );
}
