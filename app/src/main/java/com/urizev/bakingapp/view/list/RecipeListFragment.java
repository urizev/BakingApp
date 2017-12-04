package com.urizev.bakingapp.view.list;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.view.common.PresenterFragment;
import com.urizev.bakingapp.widget.ErrorView;
import com.urizev.bakingapp.widget.LoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListFragment extends PresenterFragment<RecipeListViewState, RecipeListPresenter> {
    private static final String KEY_LIST_STATE = "listState";

    @BindView(R.id.content)
    RecyclerView contentView;
    @BindView(R.id.loading)
    LoadingView loadingView;
    @BindView(R.id.error)
    ErrorView errorView;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_LIST_STATE)) {
            Parcelable listState = savedInstanceState.getParcelable(KEY_LIST_STATE);
            contentView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
     public void onSaveInstanceState(Bundle outState) {
        Parcelable listState = contentView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_LIST_STATE, listState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void renderViewState(RecipeListViewState viewState) {
        contentView.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);

        Throwable error = viewState.error();
        if (viewState.loading()) {
            loadingView.setVisibility(View.VISIBLE);
        } else if (error != null) {
            errorView.setVisibility(View.VISIBLE);
            errorView.setMessage(error.getMessage());
        } else {
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
