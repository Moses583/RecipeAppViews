package com.ravemaster.recipeapp.api;

import android.content.Context;

import com.ravemaster.recipeapp.api.autocomplete.interfaces.AutoCompleteListener;
import com.ravemaster.recipeapp.api.autocomplete.interfaces.GetAutoComplete;
import com.ravemaster.recipeapp.api.autocomplete.models.AutoCompleteApiResponse;
import com.ravemaster.recipeapp.api.getfeed.feedinterfaces.FeedsListListener;
import com.ravemaster.recipeapp.api.getfeed.feedinterfaces.GetFeed;
import com.ravemaster.recipeapp.api.getfeed.models.FeedsApiResponse;
import com.ravemaster.recipeapp.api.getrecipedetails.interfaces.GetRecipeDetails;
import com.ravemaster.recipeapp.api.getrecipedetails.interfaces.RecipeDetailsListener;
import com.ravemaster.recipeapp.api.getrecipedetails.models.RecipeDetailApiResponse;
import com.ravemaster.recipeapp.api.getrecipelist.interfaces.GetRecipeList;
import com.ravemaster.recipeapp.api.getrecipelist.interfaces.RecipeListListener;
import com.ravemaster.recipeapp.api.getrecipelist.models.RecipeListApiResponse;
import com.ravemaster.recipeapp.api.getsimilarrecipes.interfaces.GetSimilarRecipes;
import com.ravemaster.recipeapp.api.getsimilarrecipes.interfaces.SimilarRecipeListener;
import com.ravemaster.recipeapp.api.getsimilarrecipes.models.SimilarRecipeApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestManager {
    Context context;
    int currentKeyIndex = 0;

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
        Call<FeedsApiResponse> call = getFeed.getFeedList(getApiKey(),"tasty.p.rapidapi.com",5,"+0300",vegetarian,0);
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
    public void getRecipeList(RecipeListListener listener, int from, int size, String query){
        listener.onLoading(true);
        GetRecipeList getRecipeList = retrofit.create(GetRecipeList.class);
        Call<RecipeListApiResponse> call = getRecipeList.getRecipeList(getApiKey(),"tasty.p.rapidapi.com",from,size,query);
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

    public void getRecipeDetails(RecipeDetailsListener listener, int id){
        listener.onLoading(true);
        GetRecipeDetails getDetails = retrofit.create(GetRecipeDetails.class);
        Call<RecipeDetailApiResponse> call = getDetails.getDetails(getApiKey(),"tasty.p.rapidapi.com",id);
        call.enqueue(new Callback<RecipeDetailApiResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailApiResponse> call, Response<RecipeDetailApiResponse> response) {
                listener.onLoading(false);
                if (!response.isSuccessful()){
                    listener.onFailure(String.valueOf(response.code())+" from onResponse");
                    return;
                }
                listener.onResponse(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetailApiResponse> call, Throwable throwable) {
                listener.onLoading(false);
                listener.onFailure(throwable.getMessage()+ " from onFailure");
            }
        });
    }
    public void getSimilarRecipes(SimilarRecipeListener listener, int id){
        listener.onLoading(true);
        GetSimilarRecipes getSimilarRecipes = retrofit.create(GetSimilarRecipes.class);
        Call<SimilarRecipeApiResponse> call = getSimilarRecipes.getSimilarRecipes(getApiKey(),"tasty.p.rapidapi.com",id);
        call.enqueue(new Callback<SimilarRecipeApiResponse>() {
            @Override
            public void onResponse(Call<SimilarRecipeApiResponse> call, Response<SimilarRecipeApiResponse> response) {
                listener.onLoading(false);
                if (!response.isSuccessful()){
                    listener.onFailure(String.valueOf(response.code())+" from onResponse");
                    return;
                }
                listener.onResponse(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<SimilarRecipeApiResponse> call, Throwable throwable) {
                listener.onLoading(false);
                listener.onFailure(throwable.getMessage()+ " from onFailure");
            }
        });
    }

    public void getAutoComplete(AutoCompleteListener listener, String prefix){
        listener.onLoading(true);
        GetAutoComplete getAutoComplete = retrofit.create(GetAutoComplete.class);
        Call<AutoCompleteApiResponse> call = getAutoComplete.getAutoComplete(prefix);
        call.enqueue(new Callback<AutoCompleteApiResponse>() {
            @Override
            public void onResponse(Call<AutoCompleteApiResponse> call, Response<AutoCompleteApiResponse> response) {
                listener.onLoading(false);
                if (!response.isSuccessful()){
                    listener.onError(String.valueOf(response.code())+" from onResponse");
                    return;
                }
                listener.onResponse(response.body(), String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<AutoCompleteApiResponse> call, Throwable throwable) {
                listener.onLoading(false);
                listener.onError(throwable.getMessage()+ " from onFailure");
            }
        });
    }

    private String getApiKey(){
        String apikey = "";
        switch (currentKeyIndex){
            case 0:
               apikey = "ab9a345b1dmsh95573e64e14301dp11f08cjsnbb669399ee87";
               break;
            case 1:
                apikey = "cee03e6aabmsh2fa043ef9ad519ap17e59bjsn695eb9e6b3fb";
                break;
            case 2:
                apikey = "190ecbd632mshf9627c5d4b4afc0p13af00jsnaa0e7ec1b323";
                break;
            case 3:
                apikey = "7a9a8d4846mshcfaa4b403a596e8p1d45b5jsneca71b63bb58";
                break;
            case 4:
                apikey = "9514810a91mshaa1e82f038a194dp192b4djsn80ef1486dc10";
                break;
            case 5:
                apikey = "cf8e818c30mshc3a90bc6ac3c539p10a09bjsn3c104522f764";
                break;
            case 6:
                apikey = "ed4b9641acmshe6e3944254ccdf8p12c249jsn32c2d44e1c9b";
                break;
        }
        currentKeyIndex = (currentKeyIndex + 1) % 7;
        return apikey;
    }
}
