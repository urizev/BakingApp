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
    private final int recipeId;
    private int stepId;
    private Disposable disposable;
    private Recipe recipe;

    RecipeStepDetailPresenter(int recipeId, int stepId, RecipeRepository repository) {
        super(repository);
        this.recipeId = recipeId;
        this.stepId = stepId;

        loadDetails();
    }

    private void loadDetails() {
        if (this.disposable != null) {
            this.disposable.dispose();
            this.disposable = null;
        }

        this.disposable = getRepository().getRecipe(recipeId)
                .doOnNext(recipe -> this.recipe = recipe)
                .flatMap(recipe -> Observable.fromIterable(recipe.steps()))
                .filter(step -> stepId == step.id())
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
        if (recipe == null) {
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
        if (stepId >= recipe.steps().size()) {
            return;
        }

        this.stepId = stepId;
        this.loadDetails();
    }

    @Override
    protected void dispose() {
        disposable.dispose();
        super.dispose();
    }

    void showStepId(int stepId) {
        this.stepId = stepId;
        this.loadDetails();
    }

    void setPlayerPosition(long currentPosition) {
        publishViewState(currentViewState().withPlayerPosition(currentPosition));
    }
}
