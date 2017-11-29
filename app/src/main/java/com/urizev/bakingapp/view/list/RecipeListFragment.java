package com.urizev.bakingapp.view.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.view.common.PresenterFragment;
import com.urizev.bakingapp.widget.ErrorView;
import com.urizev.bakingapp.widget.LoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListFragment extends PresenterFragment<RecipeListViewState,RecipeListPresenter> {
    @BindView(R.id.content) RecyclerView contentView;
    @BindView(R.id.loading) LoadingView loadingView;
    @BindView(R.id.error) ErrorView errorView;
    private RecipeListAdapter adapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recipe_list;
    }

    @Override
    protected RecipeListPresenter createPresenter() {
        return new RecipeListPresenter(this.getApp().getRecipeRepository());
    }

    @Override
    protected void renderViewState(RecipeListViewState viewState) {
        contentView.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);

        Throwable error = viewState.error();
        if (viewState.loading()) {
            loadingView.setVisibility(View.VISIBLE);
        }
        else if (error != null) {
            errorView.setVisibility(View.VISIBLE);
            errorView.setMessage(error.getMessage());
        }
        else {
            contentView.setVisibility(View.VISIBLE);
            adapter.update(viewState.recipes());
        }
    }

    @Override
    protected void bindView(View view) {
        ButterKnife.bind(this, view);
        this.adapter = new RecipeListAdapter();
        contentView.setAdapter(adapter);
    }
}
