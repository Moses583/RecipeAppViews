package com.ravemaster.recipeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ravemaster.recipeapp.R;
import com.ravemaster.recipeapp.api.getrecipelist.models.Result;

import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipesViewHolder> {

    private Context context;
    private ArrayList<Result> results;

    public RecipeListAdapter( Context context, ArrayList<Result> results){
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipesViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        Glide
                .with(context)
                .load(results.get(position).thumbnail_url)
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        String name = results.get(holder.getAdapterPosition()).name;
        String time = String.valueOf(results.get(holder.getAdapterPosition()).cook_time_minutes);

        int positive = results.get(position).user_ratings.count_positive;
        int negative = results.get(position).user_ratings.count_negative;

        int total = positive + negative;

        double percent = ((double) positive/ total)*100;

        String rating = String.format("%.1f%%",percent);

        holder.name.setText(name);
        holder.name.setSelected(true);
        holder.time.setText(time+" min");
        holder.time.setSelected(true);
        holder.ratings.setText(rating);
        holder.ratings.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class RecipesViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, time, ratings;
        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgRecipe);
            name = itemView.findViewById(R.id.txtRecipeName);
            time = itemView.findViewById(R.id.txtRecipeTime);
            ratings = itemView.findViewById(R.id.txtRecipeRating);
        }
    }
}
