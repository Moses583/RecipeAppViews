package com.ravemaster.recipeapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.ravemaster.recipeapp.R;
import com.ravemaster.recipeapp.RecipeDetailsActivity;
import com.ravemaster.recipeapp.adapters.MealPlanAdapter;
import com.ravemaster.recipeapp.adapters.TrendingAdapter;
import com.ravemaster.recipeapp.api.RequestManager;
import com.ravemaster.recipeapp.api.getfeed.feedinterfaces.FeedsListListener;
import com.ravemaster.recipeapp.api.getfeed.models.FeedsApiResponse;
import com.ravemaster.recipeapp.api.getfeed.models.Item2;
import com.ravemaster.recipeapp.clickinterfaces.OnTrendingClicked;

public class FeedFragment extends Fragment {

    public ShimmerFrameLayout featurePlaceHolder, mealPlanPlaceHolder,trendingPlaceHolder;
    public LinearLayout featureLayout,mealPlanLayout,trendingLayout;
    ImageView imgFeature;
    TextView txtFeatureName, txtFeatureRating, txtFeatureTime, txtFeatureServings,txtMealPlanTitle;
    RecyclerView mealPlanRecycler,trendingRecycler;

    RequestManager manager;
    MealPlanAdapter mealPlanAdapter;
    TrendingAdapter trendingAdapter;
    public int id = 0;

    public FeedFragment() {
        // Required empty public constructor
    }
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        initViews(view);

        manager = new RequestManager(getActivity());

        manager.getFeedList(feedsListListener,false);

        imgFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });



        return view;
    }

    private final FeedsListListener feedsListListener = new FeedsListListener() {
        @Override
        public void onResponse(FeedsApiResponse response, String message) {

            featurePlaceHolder.stopShimmer();
            featurePlaceHolder.setVisibility(View.INVISIBLE);
            mealPlanPlaceHolder.stopShimmer();
            mealPlanPlaceHolder.setVisibility(View.INVISIBLE);
            trendingPlaceHolder.stopShimmer();
            trendingPlaceHolder.setVisibility(View.INVISIBLE);

            response = response;

            showData(response);

        }

        @Override
        public void onError(String message) {

            featurePlaceHolder.stopShimmer();
            featurePlaceHolder.setVisibility(View.INVISIBLE);
            mealPlanPlaceHolder.stopShimmer();
            mealPlanPlaceHolder.setVisibility(View.INVISIBLE);
            trendingPlaceHolder.stopShimmer();
            trendingPlaceHolder.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onLoading(boolean isLoading) {
            if (isLoading){
                featureLayout.setVisibility(View.INVISIBLE);
                featurePlaceHolder.startShimmer();
                mealPlanLayout.setVisibility(View.INVISIBLE);
                mealPlanPlaceHolder.startShimmer();
                trendingLayout.setVisibility(View.INVISIBLE);
                trendingPlaceHolder.startShimmer();
            } else {
                featurePlaceHolder.stopShimmer();
                featurePlaceHolder.setVisibility(View.INVISIBLE);
                featureLayout.setVisibility(View.VISIBLE);
                mealPlanPlaceHolder.stopShimmer();
                mealPlanPlaceHolder.setVisibility(View.INVISIBLE);
                mealPlanLayout.setVisibility(View.VISIBLE);
                trendingPlaceHolder.stopShimmer();
                trendingPlaceHolder.setVisibility(View.INVISIBLE);
                trendingLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    private void showData(FeedsApiResponse response) {
        Glide.with(getActivity())
                .load(response.results.get(0).item.thumbnail_url)
                .placeholder(R.drawable.placeholder)
                .into(imgFeature);
        id = response.results.get(0).item.id;
        String name = response.results.get(0).item.name;
        int positive = response.results.get(0).item.user_ratings.count_positive;
        int negative = response.results.get(0).item.user_ratings.count_negative;
        int total = positive + negative;
        double percent = ((double) positive / total ) * 100;
        String ratings = String.format("%.1f%%",percent);
        String time = String.valueOf(response.results.get(0).item.cook_time_minutes)+" min";
        String servings = String.valueOf(response.results.get(0).item.num_servings)+ " people";
        txtFeatureName.setText("Featured: "+name);
        txtFeatureName.setSelected(true);
        txtFeatureRating.setText(ratings);
        if (time.equals("0")){
            txtFeatureTime.setText("60 min");
        } else {
            txtFeatureTime.setText(time);
        }
        txtFeatureServings.setText(servings);

        showMealPlanAdapter(response);
        showTrendingRecipes(response);
    }

    private void showTrendingRecipes(FeedsApiResponse response) {
        trendingAdapter = new TrendingAdapter(getActivity(),response.results.get(4).items,onTrendingClicked);
        trendingRecycler.setAdapter(trendingAdapter);
        trendingRecycler.setHasFixedSize(true);
        trendingRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
    }

    private void showMealPlanAdapter(FeedsApiResponse response) {
        txtMealPlanTitle.setText("Plans: "+response.results.get(1).name);
        mealPlanAdapter = new MealPlanAdapter(getActivity(),response.results.get(1).items);
        mealPlanRecycler.setAdapter(mealPlanAdapter);
    }

    private final OnTrendingClicked onTrendingClicked = new OnTrendingClicked() {
        @Override
        public void moveToRecipeDetails(Item2 item) {
            Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
            intent.putExtra("id",item.id);
            startActivity(intent);
        }
    };

    private void initViews(View view) {
        featurePlaceHolder = view.findViewById(R.id.featurePlaceholderLayout);
        mealPlanPlaceHolder = view.findViewById(R.id.mealPlanPlaceholderLayout);
        trendingPlaceHolder = view.findViewById(R.id.trendingPlaceholderLayout);
        featureLayout = view.findViewById(R.id.featureLayout);
        mealPlanLayout = view.findViewById(R.id.mealPlanLayout);
        trendingLayout = view.findViewById(R.id.trendingLayout);
        imgFeature = view.findViewById(R.id.imgFeature);
        txtFeatureName = view.findViewById(R.id.txtFeatureRecipeName);
        txtFeatureTime = view.findViewById(R.id.txtFeatureTime);
        txtFeatureRating = view.findViewById(R.id.txtFeatureRating);
        txtFeatureServings = view.findViewById(R.id.txtFeatureServing);
        mealPlanRecycler = view.findViewById(R.id.mealPlanRecycler);
        txtMealPlanTitle = view.findViewById(R.id.txtMealPlanTitle);
        trendingRecycler = view.findViewById(R.id.trendingRecycler);
    }
}