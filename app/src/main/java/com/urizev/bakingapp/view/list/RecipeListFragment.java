package com.urizev.bakingapp.view.list;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.view.common.PresenterFragment;
import com.urizev.bakingapp.ui.ErrorView;
import com.urizev.bakingapp.ui.LoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListFragment extends PresenterFragment<RecipeListViewState, RecipeListPresenter> {
    private static final String KEY_LIST_STATE = "mListState";

    @BindView(R.id.recipe_list_content) RecyclerView mContentView;
    @BindView(R.id.recipe_list_loading) LoadingView mLoadingView;
    @BindView(R.id.recipe_list_error) ErrorView mErrorView;
    private RecipeListAdapter mAdapter;
    private Parcelable mListState;
    private boolean mListStateShouldBeRestored;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recipe_list;
    }

    @Override
    protected RecipeListPresenter createPresenter() {
        return new RecipeListPresenter(this.getApp().getmRecipeRepository());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_LIST_STATE)) {
            this.mListState = savedInstanceState.getParcelable(KEY_LIST_STATE);
            this.mListStateShouldBeRestored = true;
        }
    }

    @Override
     public void onSaveInstanceState(Bundle outState) {
        Parcelable listState = mContentView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_LIST_STATE, listState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void renderViewState(RecipeListViewState viewState) {
        mContentView.setVisibility(View.INVISIBLE);
        mLoadingView.setVisibility(View.INVISIBLE);
        mErrorView.setVisibility(View.INVISIBLE);

        Throwable error = viewState.error();
        if (viewState.loading()) {
            mLoadingView.setVisibility(View.VISIBLE);
        } else if (error != null) {
            mErrorView.setVisibility(View.VISIBLE);
            mErrorView.setMessage(error.getMessage());
        } else {
            mContentView.setVisibility(View.VISIBLE);
            mAdapter.update(viewState.recipes());
            if (mListStateShouldBeRestored) {
                mListStateShouldBeRestored = false;
                mContentView.getLayoutManager().onRestoreInstanceState(mListState);
            }
            setIdlingResourceIdle();
        }
    }

    @Override
    protected void bindView(View view) {
        ButterKnife.bind(this, view);
        this.mAdapter = new RecipeListAdapter();
        mContentView.setAdapter(mAdapter);
    }
}
