package com.urizev.bakingapp.view.detail;

import com.urizev.bakingapp.model.RecipeRepository;
import com.urizev.bakingapp.view.common.Presenter;

import io.reactivex.schedulers.Schedulers;

/**
 * Creado por jcvallejo en 29/11/17.
 */

class RecipeStepListPresenter extends Presenter<RecipeStepListViewState> {
    private final int recipeId;

    RecipeStepListPresenter(int recipeId, RecipeRepository repository) {
        super(repository);
        this.recipeId = recipeId;

        addDisposable(repository.getRecipe(recipeId)
                .map(recipe -> currentViewState().withRecipe(recipe))
                .onErrorReturn(error -> currentViewState().withError(error))
                .startWith(currentViewState().withLoading())
                .doOnNext(this::publishViewState)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe());
    }


    @Override
    protected RecipeStepListViewState currentViewState() {
        RecipeStepListViewState vs = super.currentViewState();
        if (vs == null) {
            vs = RecipeStepListViewState.builder().build();
        }
        return vs;
    }
}
