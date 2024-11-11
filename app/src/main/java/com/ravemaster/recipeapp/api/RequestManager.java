package com.ravemaster.recipeapp.api;

import android.content.Context;

import com.ravemaster.recipeapp.api.getfeed.feedinterfaces.FeedsListListener;
import com.ravemaster.recipeapp.api.getfeed.feedinterfaces.GetFeed;
import com.ravemaster.recipeapp.api.getfeed.models.FeedsApiResponse;
import com.ravemaster.recipeapp.api.getrecipelist.interfaces.GetRecipeList;
import com.ravemaster.recipeapp.api.getrecipelist.interfaces.RecipeListListener;
import com.ravemaster.recipeapp.api.getrecipelist.models.RecipeListApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestManager {
    Context context;

    public RequestManager(Context context) {
        this.context = context;
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://tasty.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void getFeedList(FeedsListListener listener, boolean vegetarian){

        listener.onLoading(true);

        GetFeed getFeed = retrofit.create(GetFeed.class);
        Call<FeedsApiResponse> call = getFeed.getFeedList(5,"+0300",vegetarian,0,"cf8e818c30mshc3a90bc6ac3c539p10a09bjsn3c104522f764","tasty.p.rapidapi.com");
        call.enqueue(new Callback<FeedsApiResponse>() {
            @Override
            public void onResponse(Call<FeedsApiResponse> call, Response<FeedsApiResponse> response) {
                listener.onLoading(false);
                if (!response.isSuccessful()){
                    listener.onError(String.valueOf(response.code())+" from onResponse");
                    return;
                }
                listener.onResponse(response.body(), response.message());

            }

            @Override
            public void onFailure(Call<FeedsApiResponse> call, Throwable throwable) {
                listener.onLoading(false);
                listener.onError(throwable.getMessage()+ " from onFailure");

            }
        });
    }
    public void getRecipeList(RecipeListListener listener, int from, int size){
        listener.onLoading(true);
        GetRecipeList getRecipeList = retrofit.create(GetRecipeList.class);
        Call<RecipeListApiResponse> call = getRecipeList.getRecipeList(from,size,"cf8e818c30mshc3a90bc6ac3c539p10a09bjsn3c104522f764","tasty.p.rapidapi.com");
        call.enqueue(new Callback<RecipeListApiResponse>() {
            @Override
            public void onResponse(Call<RecipeListApiResponse> call, Response<RecipeListApiResponse> response) {
                listener.onLoading(false);
                if (!response.isSuccessful()){
                    listener.onFailure(String.valueOf(response.code())+" from onResponse");
                    return;
                }
                listener.onResponse(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeListApiResponse> call, Throwable throwable) {
                listener.onLoading(false);
                listener.onFailure(throwable.getMessage()+ " from onFailure");
            }
        });
    }
}
