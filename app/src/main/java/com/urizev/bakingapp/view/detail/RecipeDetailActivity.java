package com.urizev.bakingapp.view.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.urizev.bakingapp.R;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeIdDelegate {
    public static final String EXTRA_RECIPE_ID = "recipeId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
    }

    @Override
    public int getRecipeId() {
        return getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
    }

    @Override
    public int getRecipeStepId() {
        RecipeStepListFragment fragment;
        fragment = (RecipeStepListFragment) getSupportFragmentManager().findFragmentById(R.id.detail);
        return fragment.getSelectedStepId();
    }

    @Override
    public void showStepDetails(int stepId) {
        RecipeStepDetailFragment fragment;
        fragment = (RecipeStepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail);
        if (fragment != null) {
            fragment.showStepId(stepId);
        }
        else {
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra(RecipeStepDetailActivity.EXTRA_RECIPE_ID, getRecipeId());
            intent.putExtra(RecipeStepDetailActivity.EXTRA_STEP_ID, stepId);
            this.startActivity(intent);
        }
    }
}
