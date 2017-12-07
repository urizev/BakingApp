package com.urizev.bakingapp.view.detail;

import com.urizev.bakingapp.model.Recipe;
import com.urizev.bakingapp.model.RecipeRepository;
import com.urizev.bakingapp.model.Step;
import com.urizev.bakingapp.view.common.Presenter;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Creado por jcvallejo en 29/11/17.
 */

class RecipeStepDetailPresenter extends Presenter<RecipeStepDetailViewState> {
    private final int mRecipeId;
    private int mStepId;
    private Disposable mDisposable;
    private Recipe mRecipe;

    RecipeStepDetailPresenter(int recipeId, int stepId, RecipeRepository repository) {
        super(repository);
        this.mRecipeId = recipeId;
        this.mStepId = stepId;

        loadDetails();
    }

    private void loadDetails() {
        if (this.mDisposable != null) {
            this.mDisposable.dispose();
            this.mDisposable = null;
        }

        this.mDisposable = getRepository().getRecipe(mRecipeId)
                .doOnNext(recipe -> this.mRecipe = recipe)
                .flatMap(recipe -> Observable.fromIterable(recipe.steps()))
                .filter(step -> mStepId == step.id())
                .map(step -> currentViewState().withStep(step))
                .onErrorReturn(error -> currentViewState().withError(error))
                .startWith(currentViewState().withLoading())
                .doOnNext(this::publishViewState)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    protected RecipeStepDetailViewState currentViewState() {
        RecipeStepDetailViewState vs = super.currentViewState();
        if (vs == null) {
            vs = RecipeStepDetailViewState.builder().build();
        }
        return vs;
    }

    void onPreviousClicked() {
        this.onChangeStep(-1);
    }

    void onNextClicked() {
        this.onChangeStep(1);
    }

    private void onChangeStep(int inc) {
        if (mRecipe == null) {
            return;
        }

        Step step = currentViewState().step();
        if (step == null) {
            return;
        }

        int stepId = step.id() + inc;
        if (stepId < 0) {
            return;
        }
        if (stepId >= mRecipe.steps().size()) {
            return;
        }

        this.mStepId = stepId;
        this.loadDetails();
    }

    @Override
    protected void dispose() {
        mDisposable.dispose();
        super.dispose();
    }

    void showStepId(int stepId) {
        this.mStepId = stepId;
        this.loadDetails();
    }

    void setPlayerPosition(long currentPosition) {
        publishViewState(currentViewState().withPlayerPosition(currentPosition));
    }
}
