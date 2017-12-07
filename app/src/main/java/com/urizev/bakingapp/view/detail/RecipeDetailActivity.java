package com.urizev.bakingapp.view.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.view.common.IdlingResourceActivity;

public class RecipeDetailActivity extends IdlingResourceActivity implements RecipeIdDelegate {
    public static final String EXTRA_RECIPE_ID = "recipeId";

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
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
        RecipeStepListFragment fragment;
        fragment = (RecipeStepListFragment) getSupportFragmentManager().findFragmentById(R.id.step_list_fragment);
        return fragment.getSelectedStepId();
    }

    @Override
    public void setTitle(String title) {
        mActionBar.setTitle(title);
    }

    @Override
    public void showStepDetails(int stepId) {
        RecipeStepDetailFragment fragment;
        fragment = (RecipeStepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.step_detail_fragment);
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
