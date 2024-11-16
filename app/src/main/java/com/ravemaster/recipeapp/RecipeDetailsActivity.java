package com.ravemaster.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ravemaster.recipeapp.api.RequestManager;
import com.ravemaster.recipeapp.api.getrecipedetails.interfaces.RecipeDetailsListener;
import com.ravemaster.recipeapp.api.getrecipedetails.models.Component;
import com.ravemaster.recipeapp.api.getrecipedetails.models.Instruction;
import com.ravemaster.recipeapp.api.getrecipedetails.models.RecipeDetailApiResponse;
import com.ravemaster.recipeapp.api.getrecipedetails.models.Section;

import java.util.ArrayList;


public class RecipeDetailsActivity extends AppCompatActivity {

    RequestManager manager;
    CardView goBack, save;
    ImageView imgRecipe;
    TextView name, servings, ratings, time, description, foodItem, ingredients, instructions;
    ShimmerFrameLayout placeholder;
    LinearLayout layout,itemsLayout;

    PieChart chart;

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

        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);

        manager = new RequestManager(this);
        manager.getRecipeDetails(listener,id);

        description.setTextIsSelectable(true);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private final RecipeDetailsListener listener = new RecipeDetailsListener() {
        @Override
        public void onResponse(RecipeDetailApiResponse response, String message) {
            placeholder.stopShimmer();
            placeholder.setVisibility(View.INVISIBLE);
            showData(response);
        }

        @Override
        public void onFailure(String message) {
            placeholder.stopShimmer();
            placeholder.setVisibility(View.INVISIBLE);
            description.setText(message);
        }

        @Override
        public void onLoading(boolean isLoading) {
            if (isLoading){
                layout.setVisibility(View.INVISIBLE);
                placeholder.startShimmer();

            } else {
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

        if (description1.isEmpty()){
            description.setText("Description unavailable");
        } else {
            description.setText(description1);
        }
        ArrayList<Section> items;
        items = response.sections;

        StringBuilder builder = new StringBuilder();
        for (Section item: items){
            builder.append("~").append(item.name).append("\n\n");

        }
        String item = builder.toString();
        if (response.sections.size()>1){
            itemsLayout.setVisibility(View.VISIBLE);
            foodItem.setText(item);
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

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) response.nutrition.calories, "Calories"));
        entries.add(new PieEntry((float) response.nutrition.carbohydrates, "Carbohydrates"));
        entries.add(new PieEntry((float) response.nutrition.fat, "Fat"));
        entries.add(new PieEntry((float) response.nutrition.fiber, "Fiber"));
        entries.add(new PieEntry((float) response.nutrition.protein, "Protein"));
        entries.add(new PieEntry((float) response.nutrition.sugar, "Sugar"));

        PieDataSet dataSet = new PieDataSet(entries, "Nutrition");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(dataSet);
        chart.setData(pieData);

        chart.getDescription().setEnabled(false);
        chart.animateY(1000);
        chart.invalidate();

        StringBuilder builder2 = new StringBuilder();
        String steps = "";
        int count = 1;

        for (Instruction i :
                response.instructions) {
            builder2.append(String.valueOf(count++) +".\t").append(i.display_text).append("\n\n");
        }
        steps = builder2.toString();
        instructions.setText(steps);

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
        foodItem = findViewById(R.id.txtFoodItems);
        ingredients = findViewById(R.id.txtIngredients);
        instructions = findViewById(R.id.txtInstructions);

        placeholder = findViewById(R.id.detailPlaceHolderLayout);
        layout = findViewById(R.id.detailLayout);
        itemsLayout = findViewById(R.id.itemsLayout);

        chart = findViewById(R.id.nutritionChart);
    }
}