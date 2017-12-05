package com.urizev.bakingapp.view.list;

import android.os.Bundle;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.view.common.IdlingResourceActivity;

public class RecipeListActivity extends IdlingResourceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
    }
}
