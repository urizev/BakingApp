package com.urizev.bakingapp.view.recipelist;

import com.urizev.bakingapp.model.RecipeRepository;
import com.urizev.bakingapp.view.common.Presenter;


class RecipeListPresenter extends Presenter<RecipeListViewState> {
    RecipeListPresenter(RecipeRepository repository) {
        super(repository);

        addDisposable(repository.getRecipes()
                .map(recipes -> currentViewState().withRecipes(recipes))
                .doOnNext(this::publishViewState)
                .startWith(currentViewState().withLoading())
                .onErrorReturn(error -> currentViewState().withError(error))
                .subscribe());
    }

    @Override
    protected RecipeListViewState currentViewState() {
        RecipeListViewState vs = super.currentViewState();
        if (vs == null) {
            vs = RecipeListViewState.builder().build();
        }
        return vs;
    }
}
