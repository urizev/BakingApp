package com.urizev.bakingapp.view.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.urizev.bakingapp.R;
import com.urizev.bakingapp.model.Recipe;
import com.urizev.bakingapp.view.detail.RecipeDetailActivity;
import com.urizev.bakingapp.widget.RecipeWidgetService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Creado por jcvallejo en 29/11/17.
 */

class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {
    private ImmutableList<Recipe> recipes;

    void update(ImmutableList<Recipe> recipes) {
        this.recipes = recipes;
        this.notifyDataSetChanged();
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes != null ? recipes.size() : 0;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final int[] colors;
        @BindView(R.id.recipe_image) ImageView image;
        @BindView(R.id.recipe_title) TextView title;
        @BindView(R.id.recipe_description) TextView description;
        private int recipeId;

        RecipeViewHolder(View itemView) {
            super(itemView);
            colors = itemView.getResources().getIntArray(R.array.recipeColors);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Recipe recipe) {
            this.recipeId = recipe.id();
            this.image.setBackgroundColor(colors[recipeId % colors.length]);
            this.title.setText(recipe.name());
            this.description.setText(itemView.getResources().getString(R.string.d_servings, recipe.servings()));
        }

        @Override
        public void onClick(View view) {
            Context context = itemView.getContext();
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipeId);
            context.startActivity(intent);

            intent = new Intent(context, RecipeWidgetService.class);
            intent.putExtra(RecipeWidgetService.EXTRA_RECIPE_ID, recipeId);
            context.startService(intent);
        }
    }
}
