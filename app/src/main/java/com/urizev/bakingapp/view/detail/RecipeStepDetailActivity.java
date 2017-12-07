package com.urizev.bakingapp.view.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.view.common.IdlingResourceActivity;

public class RecipeStepDetailActivity extends IdlingResourceActivity implements RecipeIdDelegate {
    public static final String EXTRA_RECIPE_ID = "recipeId";
    public static final String EXTRA_STEP_ID = "stepId";

    private int mStepId;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
        }

        mStepId = this.getIntent().getIntExtra(EXTRA_STEP_ID, 0);

        setContentView(R.layout.activity_recipe_step_detail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
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
        return mStepId;
    }

    @Override
    public void setTitle(String title) {
        mActionBar.setTitle(title);
    }

    @Override
    public void showStepDetails(int stepId) {}
}
