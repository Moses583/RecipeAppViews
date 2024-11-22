package com.ravemaster.recipeapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.ravemaster.recipeapp.R;
import com.ravemaster.recipeapp.RecipeDetailsActivity;
import com.ravemaster.recipeapp.adapters.AutoCompleteAdapter;
import com.ravemaster.recipeapp.adapters.RecipeListAdapter;
import com.ravemaster.recipeapp.api.RequestManager;
import com.ravemaster.recipeapp.api.autocomplete.interfaces.AutoCompleteListener;
import com.ravemaster.recipeapp.api.autocomplete.models.AutoCompleteApiResponse;
import com.ravemaster.recipeapp.api.getrecipelist.interfaces.RecipeListListener;
import com.ravemaster.recipeapp.api.getrecipelist.models.RecipeListApiResponse;
import com.ravemaster.recipeapp.api.getrecipelist.models.Result;
import com.ravemaster.recipeapp.clickinterfaces.AutoCompleteClick;
import com.ravemaster.recipeapp.clickinterfaces.OnRecipeClicked;

import java.util.Random;

public class SearchFragment extends Fragment {

    public ShimmerFrameLayout recipePlaceHolder;
    public LinearLayout recipeLayout;
    SearchBar searchBar;
    SearchView searchView;

    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView,autoRecycler;
    RecipeListAdapter adapter;
    AutoCompleteAdapter autoCompleteAdapter;

    RequestManager manager;

    public int count = 0;
    public int offset = 0;
    public String mainQuery = "";

    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);

        manager = new RequestManager(getActivity());

        Random random = new Random();
        offset = random.nextInt(50);


        manager.getRecipeList(recipeListListener,offset,2, mainQuery);
        manager.getAutoComplete(autoCompleteListener,"lasagna");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                offset = random.nextInt(50);
                manager.getRecipeList(recipeListListener,offset,2,"");
                recipeLayout.setVisibility(View.INVISIBLE);
                recipePlaceHolder.setVisibility(View.VISIBLE);
                recipePlaceHolder.startShimmer();
            }
        });

        searchView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                manager.getAutoComplete(autoCompleteListener,s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchView.setupWithSearchBar(searchBar);

        searchView.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    String query = v.getText().toString();

                    mainQuery = query;

                    recipeLayout.setVisibility(View.INVISIBLE);
                    recipePlaceHolder.setVisibility(View.VISIBLE);
                    recipePlaceHolder.startShimmer();
                    manager.getRecipeList(recipeListListener,count,2,mainQuery);

                    searchView.hide();
                    // Perform your search logic here
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private final RecipeListListener recipeListListener = new RecipeListListener() {
        @Override
        public void onResponse(RecipeListApiResponse response, String message) {

            swipeRefreshLayout.setRefreshing(false);

            recipePlaceHolder.stopShimmer();
            recipePlaceHolder.setVisibility(View.INVISIBLE);

            showData(response);
        }

        @Override
        public void onFailure(String message) {
            swipeRefreshLayout.setRefreshing(false);
            recipePlaceHolder.stopShimmer();
            recipePlaceHolder.setVisibility(View.INVISIBLE);
            if (message.contains("timeout")){
                Toast.makeText(getActivity(), "Please check your connection and try again", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoading(boolean isLoading) {
            if (isLoading){
                recipeLayout.setVisibility(View.INVISIBLE);
                recipePlaceHolder.startShimmer();

            } else {
                swipeRefreshLayout.setRefreshing(false);
                recipePlaceHolder.stopShimmer();
                recipePlaceHolder.setVisibility(View.INVISIBLE);
                recipeLayout.setVisibility(View.VISIBLE);
            }
        }
    };
    private final AutoCompleteListener autoCompleteListener = new AutoCompleteListener() {
        @Override
        public void onResponse(AutoCompleteApiResponse response, String message) {
            showAutoCompleteRecycler(response);
        }

        @Override
        public void onError(String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoading(boolean isLoading) {

        }
    };

    private void showAutoCompleteRecycler(AutoCompleteApiResponse response) {
        if (response != null){
            autoCompleteAdapter = new AutoCompleteAdapter(getActivity(),response.results,autoCompleteClick);
            autoRecycler.setAdapter(autoCompleteAdapter);
            autoRecycler.setHasFixedSize(true);
            autoRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

    }

    private void showData(RecipeListApiResponse response) {
        adapter = new RecipeListAdapter(getActivity(),response.results,onRecipeClicked);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
    }

    private final OnRecipeClicked onRecipeClicked = new OnRecipeClicked() {
        @Override
        public void moveToRecipeActivity(Result result) {
            Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
            intent.putExtra("id",result.id);
            startActivity(intent);
        }
    };

    private final AutoCompleteClick autoCompleteClick = new AutoCompleteClick() {
        @Override
        public void search(com.ravemaster.recipeapp.api.autocomplete.models.Result result) {
            searchView.hide();
            recipeLayout.setVisibility(View.INVISIBLE);
            recipePlaceHolder.setVisibility(View.VISIBLE);
            recipePlaceHolder.startShimmer();
            manager.getRecipeList(recipeListListener,0,2,result.display);
        }
    };

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recipesRecycler);
        recipeLayout = view.findViewById(R.id.recipesLayout);
        recipePlaceHolder = view.findViewById(R.id.recipesPlaceholderLayout);
        autoRecycler = view.findViewById(R.id.autoCompleteRecycler);
        swipeRefreshLayout = view.findViewById(R.id.searchRefresh);
        searchBar = view.findViewById(R.id.mySearch_bar);
        searchView = view.findViewById(R.id.mySearchView);
    }
}