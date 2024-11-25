package com.ravemaster.recipeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ravemaster.recipeapp.R;
import com.ravemaster.recipeapp.api.getfeed.models.Item2;
import com.ravemaster.recipeapp.clickinterfaces.OnMealPlanClicked;
import com.ravemaster.recipeapp.clickinterfaces.OnRecipeClicked;

import java.util.ArrayList;

public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanAdapter.MealPlanViewHolder> {
    private Context context;
    private ArrayList<Item2> urls;
    OnMealPlanClicked onMealPlanClicked;

    public MealPlanAdapter(Context context, ArrayList<Item2> urls,OnMealPlanClicked onMealPlanClicked) {
        this.context = context;
        this.urls = urls;
        this.onMealPlanClicked = onMealPlanClicked;
    }

    @NonNull
    @Override
    public MealPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MealPlanViewHolder(LayoutInflater.from(context).inflate(R.layout.meal_plan_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanViewHolder holder, int position) {

        Glide
                .with(context)
                .load(urls.get(position).thumbnail_url)
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);
        holder.name.setText(urls.get(position).name);

        String time = String.valueOf(urls.get(holder.getAdapterPosition()).cook_time_minutes);

        int positive = urls.get(position).user_ratings.count_positive;
        int negative = urls.get(position).user_ratings.count_negative;

        int total = positive + negative;

        double percent = ((double) positive/ total)*100;

        String rating = String.format("%.1f%%",percent);

        holder.name.setText(urls.get(position).name);
        holder.name.setSelected(true);
        if (time.equals("0")){
            holder.time.setText("60 min");
        } else {
            holder.time.setText(time+" min");
        }
        holder.time.setSelected(true);
        holder.ratings.setText(rating);
        holder.ratings.setSelected(true);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMealPlanClicked.moveToRecipeDetails(urls.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public static class MealPlanViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, time, ratings;
        CardView cardView;
        public MealPlanViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgMealPlan);
            name = itemView.findViewById(R.id.txtMealPlanName);
            time = itemView.findViewById(R.id.txtMealPlanTime);
            ratings = itemView.findViewById(R.id.txtMealPlanRating);
            cardView = itemView.findViewById(R.id.MealPlanCardView);
        }
    }
}
