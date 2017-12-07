package com.urizev.bakingapp.model;

import com.google.common.collect.ImmutableList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Creado por jcvallejo en 28/11/17.
 */

public class RecipeRepository {
    private BehaviorSubject<ImmutableList<Recipe>> mRecipes;
    private RecipeService mRecipeService;

    public RecipeRepository(RecipeService recipeService) {
        this.mRecipeService = recipeService;
        this.mRecipes = BehaviorSubject.create();
    }

    public Observable<ImmutableList<Recipe>> getmRecipes() {
        ObservableSource<ImmutableList<Recipe>> apiObservable;
        apiObservable = mRecipeService.getRecipes()
                .doOnNext(mRecipes::onNext);
        Observable<ImmutableList<Recipe>> cacheObservable = mRecipes.hasValue()
                ? Observable.just(mRecipes.getValue())
                : Observable.empty();

        return cacheObservable.switchIfEmpty(apiObservable);
    }

    public Observable<Recipe> getRecipe(final int id) {
        return this.getmRecipes()
                .flatMap(Observable::fromIterable)
                .filter(recipe -> id == recipe.id());
    }
}
