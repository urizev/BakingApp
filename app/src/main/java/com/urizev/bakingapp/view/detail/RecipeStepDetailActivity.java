package com.urizev.bakingapp.view.detail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.view.common.IdlingResourceActivity;

public class RecipeStepDetailActivity extends IdlingResourceActivity implements RecipeIdDelegate {
    public static final String EXTRA_RECIPE_ID = "recipeId";
    public static final String EXTRA_STEP_ID = "stepId";
    private int stepId;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        stepId = this.getIntent().getIntExtra(EXTRA_STEP_ID, 0);

        setContentView(R.layout.activity_recipe_step_detail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void setTitle(String title) {
        actionBar.setTitle(title);
    }

    @Override
    public void showStepDetails(int stepId) {}
}
