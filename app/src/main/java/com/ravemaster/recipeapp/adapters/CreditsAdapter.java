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
import com.ravemaster.recipeapp.api.getrecipedetails.models.Credit;

import java.util.ArrayList;

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.CreditsViewHolder> {

    private Context context;
    private ArrayList<Credit> credits;

    public CreditsAdapter(Context context, ArrayList<Credit> credits) {
        this.context = context;
        this.credits = credits;
    }

    @NonNull
    @Override
    public CreditsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CreditsViewHolder(LayoutInflater.from(context).inflate(R.layout.credit_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CreditsViewHolder holder, int position) {
        Glide
                .with(context)
                .load(credits.get(position).picture_url)
                .placeholder(R.drawable.img)
                .into(holder.imgAuthor);

        holder.name.setText(credits.get(position).name);
        boolean isVerified = credits.get(position).is_verified;

        if (isVerified){
            holder.verified.setVisibility(View.VISIBLE);
        } else {
            holder.verified.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return credits.size();
    }

    public static class CreditsViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAuthor,verified;
        TextView name;
        public CreditsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAuthor = itemView.findViewById(R.id.imgAuthor);
            verified = itemView.findViewById(R.id.imgIsVerified);
            name = itemView.findViewById(R.id.txtAuthorName);
        }
    }
}
