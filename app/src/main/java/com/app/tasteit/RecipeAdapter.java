package com.app.tasteit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipeList;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        // Set texto
        holder.title.setText(recipe.getTitle());
        holder.description.setText(recipe.getDescription());

        // Cargar imagen desde URL con Glide
        Glide.with(context)
                .load(recipe.getImageUrl())
                .placeholder(R.drawable.tastel) // imagen por defecto
                .into(holder.image);

        // Click para abrir detalle
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("title", recipe.getTitle());
            intent.putExtra("description", recipe.getDescription());
            intent.putExtra("imageUrl", recipe.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ShapeableImageView image;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipeTitle);
            description = itemView.findViewById(R.id.recipeDescription);
            image = itemView.findViewById(R.id.recipeImage);
        }
    }
}