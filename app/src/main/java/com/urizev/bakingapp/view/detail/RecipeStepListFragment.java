package com.urizev.bakingapp.view.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.model.Recipe;
import com.urizev.bakingapp.model.RecipeRepository;
import com.urizev.bakingapp.view.common.PresenterFragment;
import com.urizev.bakingapp.widget.ErrorView;
import com.urizev.bakingapp.widget.LoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepListFragment extends PresenterFragment<RecipeStepListViewState,RecipeStepListPresenter> implements RecipeStepListAdapter.RecipeStepListAdapterDelegate {
    @BindView(R.id.content) RecyclerView contentView;
    @BindView(R.id.loading) LoadingView loadingView;
    @BindView(R.id.error) ErrorView errorView;
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
        }
        else {
            throw new IllegalStateException();
        }
    }

    @Override
    protected void bindView(View view) {
        ButterKnife.bind(this, view);
        this.adapter = new RecipeStepListAdapter(this);
        contentView.setAdapter(adapter);
    }

    public int getSelectedStepId() {
        return getPresenter().currentViewState().selectedStepId();
    }

    @Override
    public void onStepClicked(int stepId) {
        RecipeIdDelegate delegate = (RecipeIdDelegate) getActivity();
        delegate.showStepDetails(stepId);
    }
}
