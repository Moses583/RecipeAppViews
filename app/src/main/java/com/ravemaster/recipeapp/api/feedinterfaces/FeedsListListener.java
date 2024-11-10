package com.ravemaster.recipeapp.api.feedinterfaces;

import com.ravemaster.recipeapp.api.models.FeedsApiResponse;

public interface FeedsListListener {
    void onResponse(FeedsApiResponse response, String message);
    void onError( String message);
    void onLoading( boolean isLoading);
}
