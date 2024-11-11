package com.ravemaster.recipeapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.ravemaster.recipeapp.R;
import com.ravemaster.recipeapp.adapters.RecipeListAdapter;
import com.ravemaster.recipeapp.api.RequestManager;
import com.ravemaster.recipeapp.api.getrecipelist.interfaces.RecipeListListener;
import com.ravemaster.recipeapp.api.getrecipelist.models.RecipeListApiResponse;

public class SearchFragment extends Fragment {

    public ShimmerFrameLayout recipePlaceHolder;
    public LinearLayout recipeLayout;

    RecyclerView recyclerView;
    RecipeListAdapter adapter;

    RequestManager manager;

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

        manager.getRecipeList(recipeListListener,0,100);

        return view;
    }

    private final RecipeListListener recipeListListener = new RecipeListListener() {
        @Override
        public void onResponse(RecipeListApiResponse response, String message) {
            recipePlaceHolder.stopShimmer();
            recipePlaceHolder.setVisibility(View.INVISIBLE);

            showData(response);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(String message) {
            recipePlaceHolder.stopShimmer();
            recipePlaceHolder.setVisibility(View.INVISIBLE);

            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoading(boolean isLoading) {
            if (isLoading){
                recipeLayout.setVisibility(View.INVISIBLE);
                recipePlaceHolder.startShimmer();

            } else {
                recipePlaceHolder.stopShimmer();
                recipePlaceHolder.setVisibility(View.INVISIBLE);
                recipeLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    private void showData(RecipeListApiResponse response) {
        adapter = new RecipeListAdapter(getActivity(),response.results);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recipesRecycler);
        recipeLayout = view.findViewById(R.id.recipesLayout);
        recipePlaceHolder = view.findViewById(R.id.recipesPlaceholderLayout);
    }
}