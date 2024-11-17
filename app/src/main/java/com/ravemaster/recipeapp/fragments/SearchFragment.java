package com.ravemaster.recipeapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.ravemaster.recipeapp.R;
import com.ravemaster.recipeapp.RecipeDetailsActivity;
import com.ravemaster.recipeapp.adapters.RecipeListAdapter;
import com.ravemaster.recipeapp.api.RequestManager;
import com.ravemaster.recipeapp.api.getrecipelist.interfaces.RecipeListListener;
import com.ravemaster.recipeapp.api.getrecipelist.models.RecipeListApiResponse;
import com.ravemaster.recipeapp.api.getrecipelist.models.Result;
import com.ravemaster.recipeapp.clickinterfaces.OnRecipeClicked;

public class SearchFragment extends Fragment {

    public ShimmerFrameLayout recipePlaceHolder;
    public LinearLayout recipeLayout;
    public TextInputLayout inputLayout;
    EditText editText;
    public Button button;
    private FloatingActionButton next, previous;

    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;
    RecipeListAdapter adapter;

    RequestManager manager;

    public int count = 0;
    public String query = "";

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
        editText = inputLayout.getEditText();

        manager = new RequestManager(getActivity());

        manager.getRecipeList(recipeListListener,count,20,query);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = editText.getText().toString();
                recipeLayout.setVisibility(View.INVISIBLE);
                recipePlaceHolder.setVisibility(View.VISIBLE);
                recipePlaceHolder.startShimmer();
                manager.getRecipeList(recipeListListener,count = 0,20,query);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count += 20;
                recipeLayout.setVisibility(View.INVISIBLE);
                recipePlaceHolder.setVisibility(View.VISIBLE);
                recipePlaceHolder.startShimmer();
                manager.getRecipeList(recipeListListener,count,20,query);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0){
                    recipeLayout.setVisibility(View.INVISIBLE);
                    recipePlaceHolder.setVisibility(View.VISIBLE);
                    recipePlaceHolder.startShimmer();
                    manager.getRecipeList(recipeListListener,count,20,query);
                } else {
                    count -= 20;
                    recipeLayout.setVisibility(View.INVISIBLE);
                    recipePlaceHolder.setVisibility(View.VISIBLE);
                    recipePlaceHolder.startShimmer();
                    manager.getRecipeList(recipeListListener,count,20,query);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                manager.getRecipeList(recipeListListener,count,20,query);
                recipeLayout.setVisibility(View.INVISIBLE);
                recipePlaceHolder.setVisibility(View.VISIBLE);
                recipePlaceHolder.startShimmer();
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

    private void initViews(View view) {
        inputLayout = view.findViewById(R.id.searchRecipeName);
        recyclerView = view.findViewById(R.id.recipesRecycler);
        recipeLayout = view.findViewById(R.id.recipesLayout);
        recipePlaceHolder = view.findViewById(R.id.recipesPlaceholderLayout);
        button = view.findViewById(R.id.btnSearchRecipe);
        next = view.findViewById(R.id.btnNext);
        previous = view.findViewById(R.id.btnPrevious);
        swipeRefreshLayout = view.findViewById(R.id.searchRefresh);
    }
}