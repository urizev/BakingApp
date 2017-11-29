package com.urizev.bakingapp.view.list;

import com.urizev.bakingapp.model.RecipeRepository;
import com.urizev.bakingapp.view.common.Presenter;

import io.reactivex.schedulers.Schedulers;


class RecipeListPresenter extends Presenter<RecipeListViewState> {
    RecipeListPresenter(RecipeRepository repository) {
        super(repository);

        addDisposable(repository.getRecipes()
                .map(recipes -> currentViewState().withRecipes(recipes))
                .onErrorReturn(error -> currentViewState().withError(error))
                .startWith(currentViewState().withLoading())
                .doOnNext(this::publishViewState)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
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
