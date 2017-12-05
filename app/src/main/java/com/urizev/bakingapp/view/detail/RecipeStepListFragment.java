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

    @BindView(R.id.step_list_content) RecyclerView contentView;
    @BindView(R.id.step_list_loading) LoadingView loadingView;
    @BindView(R.id.step_list_error) ErrorView errorView;
    private RecipeStepListAdapter adapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recipe_step_list;
    }

    @Override
    protected RecipeStepListPresenter createPresenter() {
        RecipeRepository repository = getApp().getRecipeRepository();
        RecipeIdDelegate delegate = (RecipeIdDelegate) getActivity();
        int recipeId = delegate.getRecipeId();
        return new RecipeStepListPresenter(recipeId, repository);
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
    protected void renderViewState(RecipeStepListViewState viewState) {
        contentView.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);

        Throwable error = viewState.error();
        Recipe recipe = viewState.recipe();
        if (viewState.loading()) {
            loadingView.setVisibility(View.VISIBLE);
        }
        else if (error != null) {
            errorView.setVisibility(View.VISIBLE);
            errorView.setMessage(error.getMessage());
        }
        else if (recipe != null){
            RecipeIdDelegate delegate = (RecipeIdDelegate) getActivity();
            delegate.setTitle(recipe.name());
            contentView.setVisibility(View.VISIBLE);
            adapter.update(recipe);
            setIdlingResourceIdle();
        }
        else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Parcelable listState = contentView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_LIST_STATE, listState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void bindView(View view) {
        ButterKnife.bind(this, view);
        this.adapter = new RecipeStepListAdapter(this, getResources().getBoolean(R.bool.twoPane));
        contentView.setAdapter(adapter);
    }

    public int getSelectedStepId() {
        return getPresenter().currentViewState().selectedStepId();
    }

    @Override
    public void onStepClicked(int stepId) {
        RecipeIdDelegate delegate = (RecipeIdDelegate) getActivity();
        delegate.showStepDetails(stepId);
        adapter.notifyDataSetChanged();
    }
}
