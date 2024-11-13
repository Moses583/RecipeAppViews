package com.ravemaster.recipeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ravemaster.recipeapp.R;
import com.ravemaster.recipeapp.api.getfeed.models.Item2;
import com.ravemaster.recipeapp.clickinterfaces.OnRecipeClicked;

import java.util.ArrayList;

public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanAdapter.MealPlanViewHolder> {
    private Context context;
    private ArrayList<Item2> urls;

    public MealPlanAdapter(Context context, ArrayList<Item2> urls) {
        this.context = context;
        this.urls = urls;
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
                .load(urls.get(0).thumbnail_urls.get(position))
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

//        holder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onClick(holder.imageView,urls.get(holder.getAdapterPosition()).thumbnail_url);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public static class MealPlanViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MealPlanViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.meal_plan_image);
        }
    }
}
