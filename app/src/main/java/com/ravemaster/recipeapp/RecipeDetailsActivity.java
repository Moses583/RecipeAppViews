package com.ravemaster.recipeapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.ravemaster.recipeapp.adapters.SimilarAdapter;
import com.ravemaster.recipeapp.api.RequestManager;
import com.ravemaster.recipeapp.api.getrecipedetails.interfaces.RecipeDetailsListener;
import com.ravemaster.recipeapp.api.getrecipedetails.models.Component;
import com.ravemaster.recipeapp.api.getrecipedetails.models.Instruction;
import com.ravemaster.recipeapp.api.getrecipedetails.models.RecipeDetailApiResponse;
import com.ravemaster.recipeapp.api.getrecipedetails.models.Section;
import com.ravemaster.recipeapp.api.getsimilarrecipes.interfaces.SimilarRecipeListener;
import com.ravemaster.recipeapp.api.getsimilarrecipes.models.Result;
import com.ravemaster.recipeapp.api.getsimilarrecipes.models.SimilarRecipeApiResponse;
import com.ravemaster.recipeapp.clickinterfaces.OnSimilarClicked;
import com.ravemaster.recipeapp.db.DBHelper;
import com.ravemaster.recipeapp.utilities.PreferenceManager;

import java.util.ArrayList;


public class RecipeDetailsActivity extends AppCompatActivity {

    RequestManager manager;
    CardView goBack, save;
    ImageView imgRecipe,imgSave;
    TextView name, servings, ratings, time, description, ingredients, instructions;
    ShimmerFrameLayout placeholder, similarPlaceHolder;
    LinearLayout layout, chartLayout, similarLayout;
    RecyclerView recyclerView;

    LottieAnimationView lottie;

    SwipeRefreshLayout swipeRefreshLayout;

    SimilarAdapter similarAdapter;

    ExtendedFloatingActionButton btnPlayButton;
    int id;

    PieChart chart;

    String videoUrl;

    PreferenceManager preferenceManager;

    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        preferenceManager = new PreferenceManager(RecipeDetailsActivity.this);
        helper = new DBHelper(RecipeDetailsActivity.this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);

        manager = new RequestManager(this);
        manager.getRecipeDetails(listener,id);
        manager.getSimilarRecipes(similarRecipeListener,id);

        description.setTextIsSelectable(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                manager.getRecipeDetails(listener,id);
                manager.getSimilarRecipes(similarRecipeListener,id);

                similarLayout.setVisibility(View.INVISIBLE);
                similarPlaceHolder.setVisibility(View.VISIBLE);
                similarPlaceHolder.startShimmer();

                layout.setVisibility(View.INVISIBLE);
                placeholder.setVisibility(View.VISIBLE);
                placeholder.startShimmer();

                lottie.setVisibility(View.INVISIBLE);

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(RecipeDetailsActivity.this);
                View view = LayoutInflater.from(RecipeDetailsActivity.this).inflate(R.layout.bottom_sheet_android,null);
                RelativeLayout selectShare = view.findViewById(R.id.selectShare);
                RelativeLayout selectSave = view.findViewById(R.id.selectSave);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();
                selectShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RecipeDetailsActivity.this, "Share will be implemented soon!", Toast.LENGTH_SHORT).show();
                    }
                });
                selectSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToDatabase();
                    }
                });
            }
        });

        btnPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(RecipeDetailsActivity.this, VideoActivity.class);
                intent1.putExtra("videoUrl",videoUrl);
                startActivity(intent1);

            }
        });

    }

    private void addToDatabase() {
        String s1 = name.getText().toString();
        String s2 = description.getText().toString();
        String s3 = ingredients.getText().toString();
        String s4 = instructions.getText().toString();
        boolean insertData = helper.insertData(s1,s2,s3,s4);
        if (insertData){
            Toast.makeText(RecipeDetailsActivity.this, "Added to offline library.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RecipeDetailsActivity.this, "Unable to add to library.", Toast.LENGTH_SHORT).show();
        }
    }

    private final SimilarRecipeListener similarRecipeListener = new SimilarRecipeListener() {
        @Override
        public void onResponse(SimilarRecipeApiResponse response, String message) {

            swipeRefreshLayout.setRefreshing(false);

            similarPlaceHolder.stopShimmer();
            similarPlaceHolder.setVisibility(View.INVISIBLE);
            similarLayout.setVisibility(View.VISIBLE);

            showSimilarRecipes(response);
        }

        @Override
        public void onFailure(String message) {

            swipeRefreshLayout.setRefreshing(false);

            similarPlaceHolder.stopShimmer();
            similarPlaceHolder.setVisibility(View.INVISIBLE);
            similarLayout.setVisibility(View.INVISIBLE);

            if (message.contains("timeout")||message.contains("429")||message.contains("unable")){
                Toast.makeText(RecipeDetailsActivity.this, "Unable to similar recipes!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoading(boolean isLoading) {
            if (isLoading){
                similarLayout.setVisibility(View.INVISIBLE);
                similarPlaceHolder.startShimmer();
            } else {

                swipeRefreshLayout.setRefreshing(false);

                similarPlaceHolder.stopShimmer();
                similarPlaceHolder.setVisibility(View.INVISIBLE);
                similarLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    private void showSimilarRecipes(SimilarRecipeApiResponse response) {
        similarAdapter = new SimilarAdapter(this,response.results,similarClicked);
        recyclerView.setAdapter(similarAdapter);
    }

    private final OnSimilarClicked similarClicked = new OnSimilarClicked() {
        @Override
        public void moveToRecipeDetails(Result result) {
            finish();
            Intent intent3 = new Intent(RecipeDetailsActivity.this, RecipeDetailsActivity.class);
            intent3.putExtra("id",result.id);
            startActivity(intent3);
        }
    };

    private final RecipeDetailsListener listener = new RecipeDetailsListener() {
        @Override
        public void onResponse(RecipeDetailApiResponse response, String message) {
            swipeRefreshLayout.setRefreshing(false);
            placeholder.stopShimmer();
            placeholder.setVisibility(View.INVISIBLE);
            layout.setVisibility(View.VISIBLE);
            lottie.setVisibility(View.INVISIBLE);
            showData(response);
        }

        @Override
        public void onFailure(String message) {
            swipeRefreshLayout.setRefreshing(false);
            placeholder.stopShimmer();
            placeholder.setVisibility(View.INVISIBLE);
            layout.setVisibility(View.INVISIBLE);
            description.setText(message);
            if (message.contains("timeout")||message.contains("429")||message.contains("unable")){
                lottie.setVisibility(View.VISIBLE);
                lottie.animate();
            }
        }

        @Override
        public void onLoading(boolean isLoading) {
            if (isLoading){
                layout.setVisibility(View.INVISIBLE);
                placeholder.startShimmer();

            } else {

                swipeRefreshLayout.setRefreshing(false);

                placeholder.stopShimmer();
                placeholder.setVisibility(View.INVISIBLE);
                layout.setVisibility(View.VISIBLE);
            }
        }
    };

    private void showData(RecipeDetailApiResponse response) {
        Glide
                .with(this)
                .load(response.thumbnail_url)
                .placeholder(R.drawable.placeholder)
                .into(imgRecipe);
        String name1 = response.name;
        int positive = response.user_ratings.count_positive;
        int negative = response.user_ratings.count_negative;
        int total = positive + negative;
        double percent = ((double) positive / total ) * 100;
        String ratings1 = String.format("%.1f%%",percent);
        String time1 = String.valueOf(response.cook_time_minutes)+" min";
        String servings1 = String.valueOf(response.num_servings)+ " people";
        String description1 = response.description;

        name.setText(name1);
        ratings.setText(ratings1);
        servings.setText(servings1);

        if (time1.equals("0")){
            time.setText("60 min");
        } else {
            time.setText(time1);
        }

        if (description1==null){
            description.setText("Description unavailable");
        } else {
            description.setText(description1);
        }

        ArrayList<Integer> positions = new ArrayList<>();

        for (Section i :
                response.sections) {
            positions.add(i.position-1);
        }

        StringBuilder builder1 = new StringBuilder();
        String ingredientItem = "";
        for (int i :
                positions) {
            for (Component c :
                    response.sections.get(i).components) {
                builder1.append("~ ").append(c.raw_text).append("\n");
            }
        }
        ingredientItem = builder1.toString();
        ingredients.setText(ingredientItem);

        if (response.nutrition != null){
            chartLayout.setVisibility(View.VISIBLE);

            ArrayList<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry((float) response.nutrition.calories, "Calories"));
            entries.add(new PieEntry((float) response.nutrition.carbohydrates, "Carbohydrates"));
            entries.add(new PieEntry((float) response.nutrition.fat, "Fat"));
            entries.add(new PieEntry((float) response.nutrition.fiber, "Fiber"));
            entries.add(new PieEntry((float) response.nutrition.protein, "Protein"));
            entries.add(new PieEntry((float) response.nutrition.sugar, "Sugar"));

            PieDataSet dataSet = new PieDataSet(entries,"");

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.BLUE);
            colors.add(Color.RED);
            colors.add(Color.GREEN);
            colors.add(Color.YELLOW);
            colors.add(Color.MAGENTA);
            colors.add(Color.GRAY);
            dataSet.setColors(colors);
            dataSet.setDrawValues(false);

            PieData pieData = new PieData(dataSet);
            chart.setData(pieData);

            chart.setDrawEntryLabels(false);

            chart.getDescription().setEnabled(false);
            chart.getLegend().setEnabled(true);
            chart.animateY(1000);
            chart.invalidate();
        }

        StringBuilder builder2 = new StringBuilder();
        String steps = "";
        int count = 1;

        for (Instruction i :
                response.instructions) {
            builder2.append(String.valueOf(count++) +".\t").append(i.display_text).append("\n\n");
        }
        steps = builder2.toString();
        instructions.setText(steps);

        videoUrl = (String) response.video_url;

        if (!(videoUrl == null)){
            btnPlayButton.setVisibility(View.VISIBLE);
        }

    }

    private void initViews() {
        goBack = findViewById(R.id.cardViewGoBack);
        save = findViewById(R.id.cardViewSaveRecipe);

        imgRecipe = findViewById(R.id.imgRecipeDetails);

        name = findViewById(R.id.txtRecipeDetailName);
        description = findViewById(R.id.txtDetailDescription);
        ratings = findViewById(R.id.txtDetailRating);
        time = findViewById(R.id.txtDetailTime);
        servings = findViewById(R.id.txtDetailServings);
        ingredients = findViewById(R.id.txtIngredients);
        instructions = findViewById(R.id.txtInstructions);

        placeholder = findViewById(R.id.detailPlaceHolderLayout);
        similarPlaceHolder = findViewById(R.id.similarPlaceHolderLayout);
        layout = findViewById(R.id.detailLayout);
        similarLayout = findViewById(R.id.similarLayout);
        chartLayout = findViewById(R.id.chartLayout);

        recyclerView = findViewById(R.id.similarRecycler);

        chart = findViewById(R.id.nutritionChart);

        btnPlayButton = findViewById(R.id.btnPlayVideo);

        swipeRefreshLayout = findViewById(R.id.recipeRefresh);

        lottie = findViewById(R.id.noInternetAnimation4);
    }
}