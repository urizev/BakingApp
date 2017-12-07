package com.urizev.bakingapp.view.detail;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.model.Recipe;
import com.urizev.bakingapp.model.RecipeRepository;
import com.urizev.bakingapp.view.common.PresenterFragment;
import com.urizev.bakingapp.ui.ErrorView;
import com.urizev.bakingapp.ui.LoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepListFragment extends PresenterFragment<RecipeStepListViewState,RecipeStepListPresenter> implements RecipeStepListAdapter.RecipeStepListAdapterDelegate {
    private static final String KEY_LIST_STATE = "listState";

    @BindView(R.id.step_list_content) RecyclerView mContentView;
    @BindView(R.id.step_list_loading) LoadingView mLoadingView;
    @BindView(R.id.step_list_error) ErrorView mErrorView;
    private RecipeStepListAdapter mAdapter;
    private boolean mListStateShouldBeRestored;
    private Parcelable mListState;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recipe_step_list;
    }

    @Override
    protected RecipeStepListPresenter createPresenter() {
        RecipeRepository repository = getApp().getmRecipeRepository();
        RecipeIdDelegate delegate = (RecipeIdDelegate) getActivity();
        int recipeId = delegate.getRecipeId();
        return new RecipeStepListPresenter(recipeId, repository);
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
    protected void renderViewState(RecipeStepListViewState viewState) {
        mContentView.setVisibility(View.INVISIBLE);
        mLoadingView.setVisibility(View.INVISIBLE);
        mErrorView.setVisibility(View.INVISIBLE);

        Throwable error = viewState.error();
        Recipe recipe = viewState.recipe();
        if (viewState.loading()) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
        else if (error != null) {
            mErrorView.setVisibility(View.VISIBLE);
            mErrorView.setMessage(error.getMessage());
        }
        else if (recipe != null){
            RecipeIdDelegate delegate = (RecipeIdDelegate) getActivity();
            delegate.setTitle(recipe.name());
            mContentView.setVisibility(View.VISIBLE);
            mAdapter.update(recipe);
            if (mListStateShouldBeRestored) {
                mListStateShouldBeRestored = false;
                mContentView.getLayoutManager().onRestoreInstanceState(mListState);
            }
            setIdlingResourceIdle();
        }
        else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Parcelable listState = mContentView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_LIST_STATE, listState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void bindView(View view) {
        ButterKnife.bind(this, view);
        this.mAdapter = new RecipeStepListAdapter(this, getResources().getBoolean(R.bool.twoPane));
        mContentView.setAdapter(mAdapter);
    }

    public int getSelectedStepId() {
        return getmPresenter().currentViewState().selectedStepId();
    }

    @Override
    public void onStepClicked(int stepId) {
        RecipeIdDelegate delegate = (RecipeIdDelegate) getActivity();
        delegate.showStepDetails(stepId);
        mAdapter.notifyDataSetChanged();
    }
}
