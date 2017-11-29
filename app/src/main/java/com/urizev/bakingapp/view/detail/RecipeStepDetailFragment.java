package com.urizev.bakingapp.view.detail;

import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.urizev.bakingapp.R;
import com.urizev.bakingapp.model.RecipeRepository;
import com.urizev.bakingapp.model.Step;
import com.urizev.bakingapp.view.common.PresenterFragment;
import com.urizev.bakingapp.widget.ErrorView;
import com.urizev.bakingapp.widget.LoadingView;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeStepDetailFragment extends PresenterFragment<RecipeStepDetailViewState,RecipeStepDetailPresenter> {
    @BindView(R.id.content) View contentView;
    @BindView(R.id.loading) LoadingView loadingView;
    @BindView(R.id.error) ErrorView errorView;
    @BindView(R.id.player) SimpleExoPlayerView playerView;
    @Nullable @BindView(R.id.description) TextView descriptionView;
    @Nullable @BindView(R.id.previous) View prevView;
    @Nullable @BindView(R.id.next) View nextView;
    @Nullable @BindView(R.id.navigation) View navigation;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recipe_step_detail;
    }

    @Override
    protected RecipeStepDetailPresenter createPresenter() {
        RecipeRepository repository = getApp().getRecipeRepository();
        RecipeIdDelegate delegate = (RecipeIdDelegate) getActivity();
        int recipeId = delegate.getRecipeId();
        int stepId = delegate.getRecipeStepId();
        return new RecipeStepDetailPresenter(recipeId, stepId, repository);
    }

    @Override
    protected void renderViewState(RecipeStepDetailViewState viewState) {
        contentView.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);

        Throwable error = viewState.error();
        Step step = viewState.step();
        if (viewState.loading()) {
            loadingView.setVisibility(View.VISIBLE);
        }
        else if (error != null) {
            errorView.setVisibility(View.VISIBLE);
            errorView.setMessage(error.getMessage());
        }
        else if (step != null) {
            contentView.setVisibility(View.VISIBLE);
            if (descriptionView != null) {
                descriptionView.setText(step.description());
            }
        }
        else {
            throw new IllegalStateException();
        }
    }

    @Override
    protected void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.previous)
    public void onPreviousClicked() {
        getPresenter().onPreviousClicked();
    }

    @OnClick(R.id.next)
    public void onNextClicked() {
        getPresenter().onNextClicked();
    }

    public void showStepId(int stepId) {
        getPresenter().showStepId(stepId);
    }
}
