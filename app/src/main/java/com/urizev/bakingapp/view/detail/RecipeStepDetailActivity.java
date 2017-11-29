package com.urizev.bakingapp.view.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.urizev.bakingapp.R;

public class RecipeStepDetailActivity extends AppCompatActivity implements RecipeIdDelegate {
    public static final String EXTRA_RECIPE_ID = "recipeId";
    public static final String EXTRA_STEP_ID = "stepId";
    private int stepId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stepId = this.getIntent().getIntExtra(EXTRA_STEP_ID, 0);

        setContentView(R.layout.activity_recipe_step_detail);
    }

    @Override
    public int getRecipeId() {
        return getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
    }

    @Override
    public int getRecipeStepId() {
        return stepId;
    }

    @Override
    public void showStepDetails(int stepId) {}
}
